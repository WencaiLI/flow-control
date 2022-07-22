package com.lwc.flowcontrol.aop;


import com.lwc.flowcontrol.commons.Constant;
import com.lwc.flowcontrol.commons.utils.HttpUtil;
import com.lwc.flowcontrol.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: liwencai
 * @Date: 2022/7/21 10:43
 * @Description:
 */
@Aspect
@Slf4j
@Component
public class InterfaceInvokeStreamControlAop {
    private final static Long FLOW_CONTROLLER_NUMBER = 1001L;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Pointcut(value = "@annotation(com.lwc.flowcontrol.aop.InterfaceStreamControl)")
    public void cutService() {
    }

    @Around("cutService()")
    @Transactional
    public ResponseEntity<JsonResult> record(ProceedingJoinPoint point) throws Throwable {
        JsonResult jsonResult = new JsonResult();
        log.info("===== 流控开始 =====");
        String remoteAddr = HttpUtil.getRequest().getRemoteAddr();
        log.info("访问地址：{}",remoteAddr);
        if(redisTemplate.opsForSet().isMember(Constant.FLOW_BLACKLIST,remoteAddr)){
            jsonResult.setStatus("error");
            jsonResult.setDescription("该IP已经被加入黑名单!");
            log.info("===== 流控结束 =====");
            return ResponseEntity.ok(jsonResult);
        }
        Long increment = redisTemplate.opsForValue().increment(remoteAddr, 1);
        log.info("IP：{}，当前周期的访问次数：{}",remoteAddr,increment);
        if ( increment == 1 ){
            jsonResult.setStatus("error");
            jsonResult.setDescription("成功访问!");
            redisTemplate.expire(remoteAddr, 10, TimeUnit.SECONDS);
        }
        if(increment >= FLOW_CONTROLLER_NUMBER) {
            jsonResult.setStatus("error");
            jsonResult.setDescription("请勿频繁访问!");
            redisTemplate.delete(remoteAddr);
            /* 并将 IP放入访问黑名单 */
            redisTemplate.opsForSet().add(Constant.FLOW_BLACKLIST,remoteAddr);
        }
        Object result = point.proceed();
        jsonResult.setData(result);

        log.info("===== 流控结束 =====");
        return ResponseEntity.ok(jsonResult);
    }
}
