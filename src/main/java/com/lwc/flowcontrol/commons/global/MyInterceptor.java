package com.lwc.flowcontrol.commons.global;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: liwencai
 * @Date: 2022/7/20 14:47
 * @Description: 拦截器方法执行顺序为
 * preHandle – Controller方法 – postHandle – afterCompletion ，
 * 所以拦截器实际上可以对 Controller 方法执行前后进行拦截监控。
 */
public class MyInterceptor implements HandlerInterceptor {// 实现HandlerInterceptor接口
    @Autowired
    

    /**
     * 访问控制器方法前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println(new Date() + "--preHandle:" + request.getRequestURL());
        return true;
    }

    /**
     * 访问控制器方法后执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("request.getRemoteAddr():::::::::::"+request.getRemoteAddr());
    }

    /**
     * postHandle方法执行完成后执行，一般用于释放资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println(new Date() + "--afterCompletion:" + request.getRequestURL());
    }
}

