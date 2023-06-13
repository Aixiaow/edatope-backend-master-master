package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.Point;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.RectifySubmitPlanCmd;
import com.csbaic.edatope.app.model.command.SubmitPlanCmd;
import com.csbaic.edatope.app.model.dto.BlockImportCheckResult;
import com.csbaic.edatope.app.model.dto.PointCountDTO;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 点位结构化数据详情 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-25
 */
public interface IPointService extends IService<Point> {

    public Result<List<BlockImportCheckResult>> importPoint(MultipartFile file, String blockWorkStageId);

    public Result<UploadFileVO> uploadPlan(MultipartFile file, String type, String blockWorkStageId);

    public void deletePlan(String fileId, String blockWorkStageId);

    public void submit(SubmitPlanCmd cmd);

    public void rectifySubmit(RectifySubmitPlanCmd cmd);

    public void adjustSubmit(SubmitPlanCmd cmd);

    /**
     * 查询指定地块工作阶段下各样点类型数量
     *
     * @param blockWorkStage
     * @return
     */
    PointCountDTO groupByPointType(String blockWorkStage);
}
