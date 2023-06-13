package com.csbaic.edatope.app.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.mapper.PointMapper;
import com.csbaic.edatope.app.model.command.RectifySubmitPlanCmd;
import com.csbaic.edatope.app.model.command.SubmitPlanCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.file.constants.FileConstants;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.model.command.UploadFileCmd;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
import com.csbaic.edatope.file.service.IAppFileService;
import com.csbaic.edatope.option.service.IOptionService;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Wrapper;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.csbaic.edatope.app.service.impl.BlockImportService.createCheckResult;

/**
 * <p>
 * 点位结构化数据详情 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-25
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements IPointService {

    private static Pattern LNG = Pattern.compile("([0-9]+\\.[0-9]+)");
    private static Pattern LAT = Pattern.compile("([0-9]+\\.[0-9]+)");

    @Autowired
    private IPointFileService pointFileService;

    @Autowired
    private IBlockService blockService;

    @Autowired
    private IWorkStageService workStageService;

    @Autowired
    private IBlockWorkStageService blockWorkStageService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private IDetectionTargetClassifyService targetClassifyService;

    @Autowired
    private IAppFileService fileService;

    @Autowired
    private IOptionService optionService;

    @Autowired
    private IPointUserTasksService pointUserTasksService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IPointTasksRecordService pointTasksRecordService;

    @Autowired
    private IPointAuditRecordService pointAuditRecordService;

    @Autowired
    private ISurveyTasksService surveyTasksService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IQualityControlTasksService qualityControlTasksService;

    @Override
    @Transactional
    public Result<List<BlockImportCheckResult>> importPoint(MultipartFile file, String blockWorkStageId) {
        try {

            // 表头信息验证
            List<ExcelTableWrapper<PointImportHeadVO>> headWrappers = new ArrayList<>();
            EasyExcel.read(file.getInputStream(), new ReadListener<PointImportHeadVO>() {
                @Override
                public void invoke(PointImportHeadVO data, AnalysisContext context) {
                    headWrappers.add(new ExcelTableWrapper<>(context.readRowHolder().getRowIndex(), context.readRowHolder().getCellMap(), data));
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {

                }
            }).headRowNumber(1).head(PointImportHeadVO.class).ignoreEmptyRow(false).doReadAllSync();

            String block = headWrappers.get(0).getData().getValue();
            if (StringUtils.isEmpty(block)) {
                return Result.error("导入数据失败，请填写地块编码");
            }

            String stage = headWrappers.get(1).getData().getValue();
            if (StringUtils.isEmpty(stage)) {
                return Result.error("导入数据失败，请填写阶段名称");
            }

            BlockWorkStage blockWorkStage = blockWorkStageService.getById(blockWorkStageId);
            BlockVO vo = blockService.getDetailById(blockWorkStage.getBlockId());
            if (!block.equals(vo.getCode())) {
                return Result.error("导入数据失败，地块编码信息错误");
            }

            if (!stage.equals(blockWorkStage.getName())) {
                return Result.error("导入数据失败，阶段名称不存在");
            }

            List<ExcelTableWrapper<PointImportVO>> wrappers = new ArrayList<>();
            EasyExcel.read(file.getInputStream(), new ReadListener<PointImportVO>() {
                @Override
                public void invoke(PointImportVO data, AnalysisContext context) {
                    wrappers.add(new ExcelTableWrapper<>(context.readRowHolder().getRowIndex(), context.readRowHolder().getCellMap(), data));
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {

                }
            }).headRowNumber(4).head(PointImportVO.class).ignoreEmptyRow(false).doReadAllSync();

            //检查excel
            List<BlockImportCheckResult> resultList = new ArrayList<>();
            for (int i = 0; i < wrappers.size() - 1; i++) {
                checkImportPoint(wrappers.get(i), resultList);
            }
//            wrappers.forEach(pointImportVOExcelTableWrapper -> checkImportPoint(pointImportVOExcelTableWrapper, resultList));
            if (CollectionUtils.isNotEmpty(resultList)) {
                return Result.error(resultList);
            }

//            List<Point> points = new ArrayList<>();
            // 写入信息前删除之前数据
            remove(Wrappers.<Point>query().eq(Point.BLOCK_WORK_STAGE_ID, blockWorkStageId));
            for (int i = 0; i < wrappers.size() - 1; i++) {
                String area = wrappers.get(i).getData().getPointAreaNumber();
                createPoint(wrappers.get(i).getData(), blockWorkStageId, block, area);
            }
//            wrappers.forEach(pointImportVOExcelTableWrapper -> points.add(createBlock(pointImportVOExcelTableWrapper.getData())));

            /*
            transactionTemplate.execute((TransactionCallback<Void>) status -> {
                saveBatch(points);
                return null;
            });*/

            UploadFileCmd fileCmd = new UploadFileCmd();
            fileCmd.setBizType("布点");
            fileCmd.setFile(file);
            WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
            String ext = Files.getFileExtension(file.getOriginalFilename());
            if (!Objects.isNull(workStage)) {
                fileCmd.setFileName(block + "-" + workStage.getName() + "点位结构化数据" + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
            } else {
                fileCmd.setFileName(block + "-点位结构化数据" + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
            }
            UploadFileVO uploadFileVO = fileService.uploadFile(fileCmd);
            //TODO 保存文件
            pointFileService.remove(Wrappers.<PointFile>query()
                    .eq(PointFile.BLOCK_WORK_STAGE_ID, blockWorkStageId)
                    .eq(PointFile.TYPE, UploadPlanTypeEnum.plan.name())
            );

            PointFile pointFile = new PointFile();
            pointFile.setFileId(uploadFileVO.getId());
            pointFile.setBlockWorkStageId(blockWorkStageId);
            pointFile.setType(UploadPlanTypeEnum.plan.name());
            pointFileService.save(pointFile);

            PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query().eq(PointUserTasks.BLOCK_WORK_STAGE_ID, blockWorkStageId).eq(PointUserTasks.DELETED, 0));
            long count = pointFileService.count(Wrappers.<PointFile>query().eq(PointFile.BLOCK_WORK_STAGE_ID, blockWorkStageId).eq(PointFile.DELETED, 0));
            if (count >= 4) {
                if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.maintenance.getValue())) {
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.stay_submit.getValue());
                }
            } else {
                if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.untreated.getValue())) {
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.maintenance.getValue());
                }
            }
            pointUserTasksService.updateById(pointUserTasks);

            return Result.ok();
        } catch (
                IOException e) {
            log.error("读取文件失败", e);
            return Result.error("读取文件失败: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public Result<UploadFileVO> uploadPlan(MultipartFile file, String type, String blockWorkStageId) {
        BlockWorkStage blockWorkStage = blockWorkStageService.getById(blockWorkStageId);
        if (Objects.isNull(blockWorkStage)) {
            Result.error("地块工作阶段不存在");
        }
        UploadFileCmd fileCmd = new UploadFileCmd();
        fileCmd.setBizType("布点");
        fileCmd.setFile(file);
        Block block = blockService.getById(blockWorkStage.getBlockId());
        WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
        String ext = Files.getFileExtension(file.getOriginalFilename());

        if (type.equals(UploadPlanTypeEnum.planText.name())) {
            fileCmd.setFileName(block.getCode() + "-" + workStage.getName() + UploadPlanTypeEnum.planText.getValue() + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
        } else if (type.equals(UploadPlanTypeEnum.planAttach)) {
            fileCmd.setFileName(block.getCode() + "-" + workStage.getName() + UploadPlanTypeEnum.planAttach.getValue() + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
        } else if (type.equals(UploadPlanTypeEnum.selfOpinion)) {
            fileCmd.setFileName(block.getCode() + "-" + workStage.getName() + UploadPlanTypeEnum.selfOpinion.getValue() + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
        } else {
            fileCmd.setFileName(block.getCode() + "-" + workStage.getName() + "方案附件" + (StringUtils.isNotEmpty(ext) ? "." + ext : ""));
        }

        UploadFileVO uploadFileVO = fileService.uploadFile(fileCmd);

        // 保存文件钱删除当前上传文件类型数据，再记录
        pointFileService.remove(Wrappers.<PointFile>query()
                .eq(PointFile.BLOCK_WORK_STAGE_ID, blockWorkStageId)
                .eq(PointFile.TYPE, type)
        );
        PointFile pointFile = new PointFile();
        pointFile.setFileId(uploadFileVO.getId());
        pointFile.setBlockWorkStageId(blockWorkStageId);
        pointFile.setType(type);
        pointFileService.save(pointFile);

        PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query().eq(PointUserTasks.BLOCK_WORK_STAGE_ID, blockWorkStageId).eq(PointUserTasks.DELETED, 0));
        long count = pointFileService.count(Wrappers.<PointFile>query().eq(PointFile.BLOCK_WORK_STAGE_ID, blockWorkStageId).eq(PointFile.DELETED, 0));
        if (count >= 4) {
            if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.maintenance.getValue())) {
                pointUserTasks.setDeployPointStatus(PointStatusEnum.stay_submit.getValue());
            }
        } else {
            if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.untreated.getValue())) {
                pointUserTasks.setDeployPointStatus(PointStatusEnum.maintenance.getValue());
            }
        }
        pointUserTasksService.updateById(pointUserTasks);

        return Result.ok(uploadFileVO);
    }

    @Override
    @Transactional
    public void deletePlan(String fileId, String blockWorkStageId) {
        AppFile file = fileService.getById(fileId);
        if (Objects.isNull(file)) {
            throw new BizRuntimeException("文件已被删除");
        }

        pointFileService.remove(Wrappers.<PointFile>query()
                .eq(PointFile.BLOCK_WORK_STAGE_ID, blockWorkStageId)
                .eq(PointFile.FILE_Id, fileId)
        );


        String baseDir = optionService.getValueByKey(FileConstants.STORAGE_PATH);
        File files = new File(baseDir + "/" + file.getFileName());
        files.delete();
        fileService.removeById(fileId);
    }

    @Override
    @Transactional
    public void submit(SubmitPlanCmd cmd) {
        cmd.getBlockWorkStageId().stream().forEach(t -> {
            long count = pointFileService.count(Wrappers.<PointFile>query().eq(PointFile.BLOCK_WORK_STAGE_ID, t).eq(PointFile.DELETED, 0));
            if (count < 4) {
                throw new BizRuntimeException("文件未上传完整请检查相关文件");
            }

            OperateItemsEnum operateItemsEnum = OperateItemsEnum.SUBMIT_TASK;

            PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query().eq(PointUserTasks.BLOCK_WORK_STAGE_ID, t).eq(PointUserTasks.DELETED, 0));
            if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.stay_submit.getValue())) {
                pointUserTasks.setDeployPointStatus(PointStatusEnum.submit.getValue());
            }

            pointUserTasksService.updateById(pointUserTasks);


            // 将文件信息记录到json中
            Map<String, Object> hashMap = new HashMap<String, Object>() {{
                put("pointFile", pointFileService.listByBlockWorkStageId(t));
            }};

            UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
            // 记录操作记录
            Organization organization = organizationService.getById(details.getOrgId());
            pointTasksRecordService.save(new PointTasksRecord(t, details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    operateItemsEnum.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                    JSONObject.toJSONString(hashMap),PointStatusEnum.submit.getValue(),null
            ));

            String serviceOrgId = "";
            String serviceOrgName = "";
            // 获取创建布点任务的人员所在的单位
            SurveyTasks idAndType = surveyTasksService.
                    getByBlockWorkStageIdAndType(t, OrganizationBizTypeEnum.LAYOUT.getValue());
            if (idAndType != null) {

                Organization organizationA = organizationService
                        .getById(userService.getById(idAndType.getCreateBy()).getOrgId());
                serviceOrgName = organizationA.getName();
                serviceOrgId = organizationA.getId();
            }

            // 记录审核记录
            pointAuditRecordService.save(
                    new PointAuditRecord(t,
                            details.getOrgId(), details.getId(),
                            organization.getName(), details.getNickName(),
                            OperateTypeEnum.POINT_TASK.getDesc(),
                            ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                            serviceOrgId, serviceOrgName,
                            operateItemsEnum.getDesc(),
                            JSONObject.toJSONString(hashMap))
            );
        });

    }

    @Override
    @Transactional
    public void rectifySubmit(RectifySubmitPlanCmd cmd) {
        cmd.getQualityControlTasksId().stream().forEach(t -> {
            QualityControlTasks byId = qualityControlTasksService.getById(t);
            if (Objects.isNull(byId)) {
                throw new BizRuntimeException("质控任务不存在");
            }
            long count = pointFileService.count(Wrappers.<PointFile>query().eq(PointFile.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId()).eq(PointFile.DELETED, 0));
            if (count < 4) {
                throw new BizRuntimeException("文件未上传完整请检查相关文件");
            }

            OperateItemsEnum operateItemsEnum = OperateItemsEnum.SUBMIT_TASK;

            if (byId.getAuditStatus().equals(PlanAuditStatusEnum.BACK_PERFECT.getValue())) {
                byId.setAuditStatus(PlanAuditStatusEnum.PERFECT_REVIEW.getValue());
            } else if (byId.getAuditStatus().equals(PlanAuditStatusEnum.BACK_RETRIAL.getValue())) {
                byId.setAuditStatus(PlanAuditStatusEnum.RETRIAL_REVIEW.getValue());
            }
            qualityControlTasksService.updateById(byId);

            List<String> level = new ArrayList<>();
            if (byId.getQualityType().equals(ServiceLevelEnum.CITY_LEVEL)) {
                level.add(ServiceLevelEnum.PROVINCE_LEVEL.name());
                level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
            } else if (byId.getQualityType().equals(ServiceLevelEnum.PROVINCE_LEVEL)) {
                level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
            }
            //level size>0表示为市级或者省级。则查询是否有上级质控，没有则更新布点方案状态
            if (level.size() > 0) {
                List<QualityControlTasks> qualityControlTasks = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                        .in(QualityControlTasks.QUALITY_TYPE, level));

                if (qualityControlTasks.size() <= 0) {
                    PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                            .eq(PointUserTasks.DELETED, 0));
                    if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.back_perfect.getValue())) {
                        operateItemsEnum = OperateItemsEnum.REFORM_SUBMIT_TASK;
                        pointUserTasks.setDeployPointStatus(PointStatusEnum.perfect_review.getValue());
                    } else if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.back_retrial.getValue())) {
                        operateItemsEnum = OperateItemsEnum.REFORM_SUBMIT_TASK;
                        pointUserTasks.setDeployPointStatus(PointStatusEnum.retrial_review.getValue());
                    }

                    pointUserTasksService.updateById(pointUserTasks);
                }
            } else {
                // 否则为国家级则最高级别则无需查询，直接更新布点方案状态
                PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                        .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                        .eq(PointUserTasks.DELETED, 0));
                if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.back_perfect.getValue())) {
                    operateItemsEnum = OperateItemsEnum.REFORM_SUBMIT_TASK;
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.perfect_review.getValue());
                } else if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.back_retrial.getValue())) {
                    operateItemsEnum = OperateItemsEnum.REFORM_SUBMIT_TASK;
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.retrial_review.getValue());
                }

                pointUserTasksService.updateById(pointUserTasks);
            }


            // 将文件信息记录到json中
            Map<String, Object> hashMap = new HashMap<String, Object>() {{
                put("pointFile", pointFileService.listByBlockWorkStageId(t));
            }};

            UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
            // 记录操作记录
            Organization organization = organizationService.getById(details.getOrgId());
            pointTasksRecordService.save(new PointTasksRecord(t, details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    operateItemsEnum.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                    JSONObject.toJSONString(hashMap),null,null
            ));

            String serviceOrgId = "";
            String serviceOrgName = "";
            // 获取创建布点任务的人员所在的单位
            SurveyTasks idAndType = surveyTasksService.
                    getByBlockWorkStageIdAndType(t, OrganizationBizTypeEnum.LAYOUT.getValue());
            if (idAndType != null) {

                Organization organizationA = organizationService
                        .getById(userService.getById(idAndType.getCreateBy()).getOrgId());
                serviceOrgName = organizationA.getName();
                serviceOrgId = organizationA.getId();
            }

            // 记录审核记录
            pointAuditRecordService.save(
                    new PointAuditRecord(t,
                            details.getOrgId(), details.getId(),
                            organization.getName(), details.getNickName(),
                            OperateTypeEnum.POINT_TASK.getDesc(),
                            ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                            serviceOrgId, serviceOrgName,
                            operateItemsEnum.getDesc(),
                            JSONObject.toJSONString(hashMap))
            );
        });
    }

    @Override
    @Transactional
    public void adjustSubmit(SubmitPlanCmd cmd) {
        cmd.getBlockWorkStageId().stream().forEach(t -> {
            long count = pointFileService.count(Wrappers.<PointFile>query().eq(PointFile.BLOCK_WORK_STAGE_ID, t).eq(PointFile.DELETED, 0));
            if (count < 4) {
                throw new BizRuntimeException("文件未上传完整请检查相关文件");
            }

            OperateItemsEnum operateItemsEnum = OperateItemsEnum.SUBMIT_TASK;

            PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query().eq(PointUserTasks.BLOCK_WORK_STAGE_ID, t).eq(PointUserTasks.DELETED, 0));
            if (pointUserTasks.getDeployPointStatus().equals(PointStatusEnum.back_maintain.getValue())) {
                operateItemsEnum = OperateItemsEnum.DEFEND_SUBMIT_TASK;
                pointUserTasks.setDeployPointStatus(PointStatusEnum.maintain_audit.getValue());
                // TODO 需要修改对应的布点质控任务的方案审核状态由“退回维护”变为“待审核”

                List<QualityControlTasks> qualityControlTasks = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, t)
                        .eq(QualityControlTasks.DEADLINE, 0));

                qualityControlTasks.stream().forEach(quality ->{
                    if (quality.getAuditStatus().equals(PlanAuditStatusEnum.BACK_MAINTAIN.name())) {
                        quality.setAuditStatus(PlanAuditStatusEnum.STAY_AUDIT.getValue());
                        qualityControlTasksService.updateById(quality);
                    }
                });
            }

            pointUserTasksService.updateById(pointUserTasks);


            // 将文件信息记录到json中
            Map<String, Object> hashMap = new HashMap<String, Object>() {{
                put("pointFile", pointFileService.listByBlockWorkStageId(t));
            }};

            UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
            // 记录操作记录
            Organization organization = organizationService.getById(details.getOrgId());
            pointTasksRecordService.save(new PointTasksRecord(t, details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    operateItemsEnum.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                    JSONObject.toJSONString(hashMap),null,null
            ));

            String serviceOrgId = "";
            String serviceOrgName = "";
            // 获取创建布点任务的人员所在的单位
            SurveyTasks idAndType = surveyTasksService.
                    getByBlockWorkStageIdAndType(t, OrganizationBizTypeEnum.LAYOUT.getValue());
            if (idAndType != null) {

                Organization organizationA = organizationService
                        .getById(userService.getById(idAndType.getCreateBy()).getOrgId());
                serviceOrgName = organizationA.getName();
                serviceOrgId = organizationA.getId();
            }

            // 记录审核记录
            pointAuditRecordService.save(
                    new PointAuditRecord(t,
                            details.getOrgId(), details.getId(),
                            organization.getName(), details.getNickName(),
                            OperateTypeEnum.POINT_TASK.getDesc(),
                            ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                            serviceOrgId, serviceOrgName,
                            operateItemsEnum.getDesc(),
                            JSONObject.toJSONString(hashMap))
            );
        });
    }

    /**
     * 查询指定地块工作阶段下各样点类型数量
     *
     * @param blockWorkStage
     * @return
     */
    @Override
    public PointCountDTO groupByPointType(String blockWorkStage) {
        List<Map<String, Object>> list = getBaseMapper().selectMaps(Wrappers.<Point>query()
                .select("count(1) count,point_type type")
                .eq(Point.BLOCK_WORK_STAGE_ID, blockWorkStage)
                .groupBy(Point.POINT_TYPE));
        PointCountDTO countDTO = new PointCountDTO();
        list.forEach(a -> {
            switch (a.get("type") + "") {
                case "土壤点位":
                    countDTO.setSoilCount(Integer.valueOf(a.get("count") + ""));
                    break;
                case "地下水点位":
                    countDTO.setWaterCount(Integer.valueOf(a.get("count") + ""));
                    break;
                case "土水复合点位":
                    countDTO.setSoleWaterCount(Integer.valueOf(a.get("count") + ""));
                    break;
                default:
                    break;
            }
        });
        return countDTO;
    }

    /**
     * 创建点位数据
     *
     * @return
     */
    @Transactional
    public Point createPoint(PointImportVO pointImportVO, String blockWorkStageId, String block, String area) {
        Point point = new Point();

        BeanCopyUtils.copyNonNullProperties(pointImportVO, point);
        point.setBlockWorkStageId(blockWorkStageId);

        if (StringUtils.isNotEmpty(pointImportVO.getSurfaceTargetName())) {
            List<String> surfaceTarget = Arrays.asList(pointImportVO.getSurfaceTargetName().split("、"));
            List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().in(DetectionTargetClassify.NAME, surfaceTarget));
            List<String> id = list.stream().map(DetectionTargetClassify::getId).collect(Collectors.toList());
            String ids = String.join(",", id);
            point.setSurfaceTarget(ids);
        }

        if (StringUtils.isNotEmpty(pointImportVO.getDeepTargetName())) {
            List<String> deepTarget = Arrays.asList(pointImportVO.getDeepTargetName().split("、"));
            List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().in(DetectionTargetClassify.NAME, deepTarget));
            List<String> id = list.stream().map(DetectionTargetClassify::getId).collect(Collectors.toList());
            String ids = String.join(",", id);
            point.setDeepTarget(ids);
        }

        if (StringUtils.isNotEmpty(pointImportVO.getWaterTargetName())) {
            List<String> waterTarget = Arrays.asList(pointImportVO.getWaterTargetName().split("、"));
            List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().in(DetectionTargetClassify.NAME, waterTarget));
            List<String> id = list.stream().map(DetectionTargetClassify::getId).collect(Collectors.toList());
            String ids = String.join(",", id);
            point.setWaterTarget(ids);
        }

        point.setLatitude(new BigDecimal(pointImportVO.getLatitude()));
        point.setLongitude(new BigDecimal(pointImportVO.getLongitude()));

        /*Matcher lat = LAT.matcher(pointImportVO.getLatitude());
        if(lat.matches()){
            point.setLatitude(new BigDecimal(lat.group(2)));
        }

        Matcher lng  = LNG.matcher(pointImportVO.getLongitude());
        if(lng.matches()){
            point.setLongitude(new BigDecimal(lng.group(2)));
        }*/
        point.setPointNumber(genPointCode(point, block, area));
        save(point);
        return point;
    }

    private void checkImportPoint(ExcelTableWrapper<PointImportVO> wrapper, List<BlockImportCheckResult> resultList) {
        PointImportVO pointImportVO = wrapper.getData();
        if (StringUtils.isEmpty(pointImportVO.getPointAreaNumber())) {
            resultList.add(createCheckResult(0, wrapper, "布点区域编号不能为空"));
        }

        if (StringUtils.isEmpty(pointImportVO.getSelectBasis())) {
            resultList.add(createCheckResult(1, wrapper, "筛选依据不能为空"));
        }

        if (StringUtils.isEmpty(pointImportVO.getLocation())) {
            resultList.add(createCheckResult(3, wrapper, "位置不能为空"));
        }

        if (StringUtils.isEmpty(pointImportVO.getLongitude())) {
            resultList.add(createCheckResult(4, wrapper, "地块经度不能为空"));
        } else {
            if (!LNG.matcher(pointImportVO.getLongitude()).matches()) {
                resultList.add(createCheckResult(4, wrapper, "地块经度格式不正确"));
            }
        }

        if (StringUtils.isEmpty(pointImportVO.getLatitude())) {
            resultList.add(createCheckResult(5, wrapper, "地块纬度不能为空"));
        } else {
            if (!LAT.matcher(pointImportVO.getLatitude()).matches()) {
                resultList.add(createCheckResult(5, wrapper, "地块纬度格式不正确"));
            }
        }

        if (StringUtils.isEmpty(pointImportVO.getPointType())) {
            resultList.add(createCheckResult(6, wrapper, "样点类型不能为空"));
        } else {
            if (pointImportVO.getPointType().equals("土壤点位")) {
                if (StringUtils.isEmpty(pointImportVO.getSurfaceTargetName())) {
                    resultList.add(createCheckResult(8, wrapper, "表层土壤检测指标分类不能为空"));
                } else {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getSurfaceTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "表层土壤检测指标分类信息不存在"));
                    }
                }

                if (StringUtils.isNotEmpty(pointImportVO.getDeepTargetName())) {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getDeepTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "深层土壤检测指标分类信息不存在"));
                    }
                }

                if (StringUtils.isNotEmpty(pointImportVO.getWaterTargetName())) {
                    resultList.add(createCheckResult(8, wrapper, "地下水检测指标分类不可填"));
                }
            } else if (pointImportVO.getPointType().equals("地下水点位")) {
                if (StringUtils.isNotEmpty(pointImportVO.getSurfaceTargetName()) || StringUtils.isNotEmpty(pointImportVO.getDeepTargetName())) {
                    resultList.add(createCheckResult(8, wrapper, "表层土壤检测指标分类和深层土壤检测指标分类不可填"));
                }

                if (StringUtils.isEmpty(pointImportVO.getWaterTargetName())) {
                    resultList.add(createCheckResult(10, wrapper, "地下水检测指标分类不能为空"));
                } else {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getWaterTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "地下水检测指标分类信息不存在"));
                    }
                }
            } else if (pointImportVO.getPointType().equals("土水复合点位")) {
                if (StringUtils.isEmpty(pointImportVO.getSurfaceTargetName())) {
                    resultList.add(createCheckResult(8, wrapper, "表层土壤检测指标分类不能为空"));
                } else {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getSurfaceTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "表层土壤检测指标分类信息不存在"));
                    }
                }

                if (StringUtils.isNotEmpty(pointImportVO.getDeepTargetName())) {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getDeepTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "深层土壤检测指标分类信息不存在"));
                    }
                }

                if (StringUtils.isEmpty(pointImportVO.getWaterTargetName())) {
                    resultList.add(createCheckResult(10, wrapper, "地下水检测指标分类不能为空"));
                } else {
                    boolean a = false;
                    List<String> target = Arrays.asList(pointImportVO.getWaterTargetName().split("、"));
                    for (String str : target) {
                        List<DetectionTargetClassify> list = targetClassifyService.list(Wrappers.<DetectionTargetClassify>query().eq(DetectionTargetClassify.NAME, str));
                        if (list.size() <= 0) {
                            a = true;
                            break;
                        }
                    }
                    if (a) {
                        resultList.add(createCheckResult(8, wrapper, "地下水检测指标分类信息不存在"));
                    }
                }
            } else {
                resultList.add(createCheckResult(6, wrapper, "样点类型错误"));
            }
        }

        if (StringUtils.isEmpty(pointImportVO.getDepth())) {
            resultList.add(createCheckResult(7, wrapper, "计划钻探深度不能为空"));
        }
    }

    public static BlockImportCheckResult createCheckResult(Integer cellIndex, ExcelTableWrapper<PointImportVO> wrapper, String msg) {
        Map<Integer, Cell> cellMap = wrapper.getCellMap();
        Cell cell = cellMap.get(cellIndex);
        if (cell == null) {
            ReadCellData readCellData = new ReadCellData<>();
            readCellData.setRowIndex(wrapper.getRow());
            readCellData.setStringValue("");
            readCellData.setColumnIndex(cellIndex);
            cell = readCellData;
        }
        return new BlockImportCheckResult(cell.getRowIndex() == null ? wrapper.getRow() : cell.getRowIndex(), cell.getColumnIndex(), cell instanceof CellData ? ((CellData) cell).getStringValue() : "", msg);
    }

    public String genPointCode(Point point, String block, String area) {
        long index = 0;
        if (StringUtils.isNotEmpty(point.getBlockWorkStageId())) {
            index = count(
                    Wrappers.<Point>query().eq(Point.POINT_AREA_NUMBER, area).eq(Point.BLOCK_WORK_STAGE_ID, point.getBlockWorkStageId())
            );
        }
        StringBuilder sb = new StringBuilder();
        sb.append(block).append(area).append(String.valueOf(++index + 100).substring(1));

        return sb.toString();
    }
}
