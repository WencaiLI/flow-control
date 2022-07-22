package com.lwc.flowcontrol.aop;


import com.lwc.flowcontrol.commons.Constant;
import com.lwc.flowcontrol.commons.exception.InterfaceInvokeException;
import com.lwc.flowcontrol.commons.utils.HttpUtil;
import com.lwc.flowcontrol.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Auther: liwencai
 * @Date: 2022/7/18 23:05
 * @Description:
 */
@Component
@Aspect
@Slf4j
public class InterfaceInvokeAop {
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void cutService() {
    }

    @Around("cutService()")
    @Transactional
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        log.info("======开始耗时计算=======");
        Object result;
        try {
            result = handle(point);
            long endTime = System.currentTimeMillis();
            log.info("======结束耗时计算======");
            log.info("总耗时：{}",(endTime-beginTime)+"ms");
            return result;
        } catch (Exception e) {
            log.info("======结束耗时计算======");
            log.error("记录出错!", e);
        }
        return null;
    }

    public ResponseEntity<JsonResult> handle(ProceedingJoinPoint point) throws Throwable {
        JsonResult jsonResult = new JsonResult();

        Object result;
        /* 判断拦截的是否为方法 */
        Signature sig = point.getSignature();
        MethodSignature msig;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        /* 获取拦截方法信息 */
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(sig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();
        log.info("调用方法："+methodName);

        /* 获取IP */
        HttpServletRequest request = HttpUtil.getRequest();
        String remoteAddr = request.getRemoteAddr();
        log.info("请 求IP："+remoteAddr);

        /* 判断是否执行 */
        /* 开启redis事务 */
        Integer number = (Integer)redisTemplate.opsForHash().get(Constant.ACCESS_CONTROLLER,remoteAddr);
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.watch(Constant.ACCESS_CONTROLLER);
//        redisTemplate.multi();
        log.info("次数：{}",number);
        if (number != null && number > 0){
            try {
                result = point.proceed();
            }catch (Exception e){
                log.error("接口调用异常", e);
                throw new InterfaceInvokeException(e.getMessage(),e.getCause());
            }
            redisTemplate.opsForHash().increment(Constant.ACCESS_CONTROLLER, remoteAddr, -1);
            jsonResult.setStatus("success");
            jsonResult.setData(result);
            /* 获取注解保存的信息 如：接口名称、接口类型 */
            RequestMapping annotation = currentMethod.getAnnotation(RequestMapping.class);
            String values = String.join(",", annotation.value());
            String methods = String.join(",", Arrays.toString(annotation.method()));
            String annotationType = annotation.annotationType().toString();
            log.info("接口路径："+values);
            log.info("接口类型："+methods);
            log.info("注解全称："+annotationType);
        }else {
            redisTemplate.opsForHash().delete(Constant.ACCESS_CONTROLLER, remoteAddr);
            log.warn("ip:{}无访问端口权限",remoteAddr);
            jsonResult.setStatus("error");
            jsonResult.setData("您无权访问该接口");
        }
        /* 提交事务操作，结束redis事务 */
//        redisTemplate.exec();
        return ResponseEntity.ok(jsonResult);
    }
}
