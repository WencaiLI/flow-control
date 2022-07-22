package com.lwc.flowcontrol.controller;

//import com.thtf.stream_controller.aop.InterfaceInvokeLog;
import com.lwc.flowcontrol.aop.InterfaceStreamControl;
import com.lwc.flowcontrol.commons.Constant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @Auther: WenCaiLi
 * @Date: 2022/7/18 15:32
 * @Description:
 */
@RestController
@RequestMapping("/interface_statistics")
public class Controller {
    @Resource
    RedisTemplate<String,Object> redisTemplate;

//    @InterfaceLimit(key = "limit2", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前排队人数较多，请稍后再试！")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Object test(){
        int p = 0;
//        int[] j = new int[10];
        for (int i = 1; i < 10000; i++) {
            p = p + i;
//            j[i] = i;
        }
        return p;
    }

    @GetMapping("/test2")
    @InterfaceStreamControl(desc = "对IP访问所有注释的接口限流")
    public Object test2(){
        int p = 0;
        for (int i = 1; i < 1000; i++) {
            p = p + i;
        }
        return p;
    }

    @GetMapping("/init")
    public Object init(){
        // 排除掉ip
        redisTemplate.opsForHash().put(Constant.ACCESS_CONTROLLER,Constant.IP,Constant.ACCESS_NUMBER);
        return "ccccccccc";
    }
}
