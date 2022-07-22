package com.lwc.flowcontrol.aop;

import java.lang.annotation.*;

/**
 * @Auther: WenCaiLi
 * @Date: 2022/7/18 15:48
 * @Description: 接口调用自定义注解
 */
@Deprecated
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})

public @interface InterfaceInvokeLog {
    // 接口名
    String interfaceName() default "";
//    // 所在类名
//    String className = null;
//    // ip地址
}
