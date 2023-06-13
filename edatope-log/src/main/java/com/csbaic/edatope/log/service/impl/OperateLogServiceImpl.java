package com.csbaic.edatope.log.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.context.OperateContext;
import com.csbaic.edatope.log.entity.OperateLog;
import com.csbaic.edatope.log.enums.OperateType;
import com.csbaic.edatope.log.mapper.OperateLogMapper;
import com.csbaic.edatope.log.model.dto.OperateLogDTO;
import com.csbaic.edatope.log.model.dto.OperateLogListItemDTO;
import com.csbaic.edatope.log.model.query.OperateLogQuery;
import com.csbaic.edatope.log.service.IOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.log.service.OperateLogHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingPropertyAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.function.Function;


/**
 * <p>
 * 业务操作日志 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-18
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService, OperateLogHandler {

    @Override
    public IPage<OperateLogListItemDTO> listOperateLog(OperateLogQuery query) {
        return getBaseMapper().listOperateLog(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(log -> {
                    OperateLogListItemDTO itemDTO = new OperateLogListItemDTO();
                    BeanCopyUtils.copyNonNullProperties(log, itemDTO);
                    return itemDTO;
                });
    }

    @Override
    public OperateLogDTO getOperateLog(String id) {
        OperateLog operateLog = getById(id);
        if (operateLog == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到日志：" + id);
        }
        return convertDTO(operateLog);
    }

    @Override
    public void handle(OperateContext context) {
        OperateLog operateLog = new OperateLog();
        BeanCopyUtils.copyNonNullProperties(context, operateLog);
        if (StringUtils.isNotBlank(context.getRemark())) {

            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext evaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding()
                    .withRootObject(context)
                    .build();
            Expression expression = parser.parseExpression(context.getRemark(), new TemplateParserContext());
            operateLog.setRemark(expression.getValue(evaluationContext, String.class));
        }
        //设置操作对象
        if (context.getTarget() instanceof BaseEntity) {
            if (StringUtils.isBlank(operateLog.getTargetId())) {
                operateLog.setTargetId(((BaseEntity) context.getTarget()).getId());
            }
            if (StringUtils.isBlank(operateLog.getTargetName())) {
                operateLog.setTargetName(context.getTarget().getClass().getName());
            }
        }
        save(operateLog);
    }

    /**
     * 实体转换
     *
     * @param log
     * @return
     */
    public static OperateLogDTO convertDTO(OperateLog log) {
        OperateLogDTO operateLogDTO = new OperateLogDTO();
        BeanCopyUtils.copyNonNullProperties(log, operateLogDTO);
        return operateLogDTO;
    }
}
