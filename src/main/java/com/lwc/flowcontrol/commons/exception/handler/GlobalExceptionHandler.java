package com.lwc.flowcontrol.commons.exception.handler;


import com.lwc.flowcontrol.commons.exception.InterfaceInvokeException;
import com.lwc.flowcontrol.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice("com.thtf.stream_controller.controller")
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * @Author: liwencai
     * @Description: 缺参处理器
     * @Date: 2022/7/18
     * @Param e:
     * @return: org.springframework.http.ResponseEntity<com.thtf.stream_controller.dto.JsonResult>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<JsonResult> parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
        logger.error("", e);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        jsonResult.setData(e);
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * @Author: liwencai
     * @Description: 缺少请求体异常处理器
     * @Date: 2022/7/18
     * @Param e:
     * @return: com.thtf.stream_controller.dto.ResultModel
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonResult> parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        jsonResult.setData(e);
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * @Author: liwencai
     * @Description: 参数校验异常处理器
     * @Date: 2022/7/18
     * @Param e:
     * @return: org.springframework.http.ResponseEntity<com.thtf.stream_controller.dto.JsonResult>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult> parameterExceptionHandler(MethodArgumentNotValidException e) {
        logger.error("", e);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                List<String> errorInfo = e
                        .getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                log.error(errorInfo.toString());

                jsonResult.setStatus("error");
                jsonResult.setData(errorInfo.toString());
                return ResponseEntity.ok(jsonResult);
                /*// 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                fieldError.getField()+fieldError.getDefaultMessage()*/
            }
        }
        jsonResult.setStatus("success");
        jsonResult.setData(e);
        return ResponseEntity.ok(jsonResult);
    }


    /**
     * @Author: liwencai
     * @Description: 约束验证异常处理器
     * @Date: 2022/7/18
     * @Param e:
     * @return: com.thtf.stream_controller.dto.ResultModel
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<JsonResult> validationParamErrorHandler(ConstraintViolationException e) {
        List<String> errorInfo = e
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        log.error(errorInfo.toString());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        jsonResult.setData(e);
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * @Author: liwencai
     * @Description: 其他异常
     * @Date: 2022/7/18
     * @Param e:
     * @return: org.springframework.http.ResponseEntity<com.thtf.stream_controller.dto.JsonResult>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<JsonResult> otherExceptionHandler(Exception e) {
        logger.error("其他异常", e);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (!StringUtils.isEmpty(e.getMessage())) {
            jsonResult.setData(e.getMessage());
            return ResponseEntity.ok(jsonResult);
        }
        jsonResult.setData(e);
        return ResponseEntity.ok(jsonResult);
    }

    /* 自定义异常处理器 */

    /**
     * @Author: liwencai
     * @Description: 接口调用异常处理器
     * @Date: 2022/7/18
     * @Param e:
     * @return: org.springframework.http.ResponseEntity<com.thtf.stream_controller.dto.JsonResult>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InterfaceInvokeException.class})
    public Object interfaceInvokeExceptionHandler(Exception e) {
//        logger.error("接口调用异常", e);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("error");
        if (!StringUtils.isEmpty(e.getStackTrace())) {
            jsonResult.setData(getExceptionSrintStackTrace(e));
            return jsonResult;
        }
        jsonResult.setData(e);
        return jsonResult;
    }

    /* 获取自定义堆栈信息 */
    public static Object getExceptionSrintStackTrace(Throwable e) {
        Map<String,Object> map = new HashMap<>();
        map.put("exception",e.getClass()+"："+e.getMessage());
        map.put("detail",Stream.of(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
        return map;
    }

}