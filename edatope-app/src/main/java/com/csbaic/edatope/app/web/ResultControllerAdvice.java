package com.csbaic.edatope.app.web;

import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ResultControllerAdvice {

    @Autowired
    private Environment environment;

    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result> handleFileSizeLimitExceededException(MaxUploadSizeExceededException biz) {
        log.error("handleBizRuntimeException: ", biz);
        return ResponseEntity.ok(
                Result.error("上传的文件超过限制,当前最大允许: " + environment.getProperty("spring.servlet.multipart.max-file-size"))
        );
    }

    @ResponseBody
    @ExceptionHandler(BizRuntimeException.class)
    public ResponseEntity<Result> handleBizRuntimeException(BizRuntimeException biz) {
        log.error("handleBizRuntimeException: ", biz);
        return ResponseEntity.ok(
                Result.error(biz.getCode(), biz.getMessage(), biz.getData())
        );
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException biz) {
        log.error("handleIllegalArgumentException: ", biz);
        return ResponseEntity.ok(
                Result.error(ResultCode.BAD_REQUEST.getCode(), biz.getMessage())
        );
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result> handleHttpMessageNotReadableException(HttpMessageNotReadableException biz) {
        log.error("HttpMessageNotReadableException: ", biz);
        return ResponseEntity.ok(
                Result.error(ResultCode.BAD_REQUEST.getCode(), biz.getMessage())
        );
    }


    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result> handleMissingServletRequestParameterException(MissingServletRequestParameterException biz) {
        log.error("handleMissingServletRequestParameterException: ", biz);
        return ResponseEntity.ok(
                Result.error(ResultCode.BAD_REQUEST.getCode(), biz.getMessage())
        );
    }


    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Result> handleNullPointerException(NullPointerException biz) {
        log.error("handleNullPointerException: ", biz);
        return ResponseEntity.ok(
                Result.error(ResultCode.ERROR.getCode(), biz.getMessage())
        );
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Result> handleThrowable(Throwable biz) {
        log.error("handleThrowable: ", biz);

        return ResponseEntity.ok(Result.error());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.info("参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());

        return ResponseEntity.ok(
                Result.error(fieldError.getDefaultMessage())
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result> handleBindException(BindException ex) {
        //校验 除了 requestbody 注解方式的参数校验 对应的 bindingresult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();

        return ResponseEntity.ok(
                Result.error(fieldError.getDefaultMessage())
        );
    }


//    @ResponseBody
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException ex){
//        return ResponseEntity.status(
//                HttpStatus.FORBIDDEN
//        ).body(
//                Result.error(ResultCode.FORBIDDEN.getCode(), "缺少访问权限")
//        );
//    }
}
