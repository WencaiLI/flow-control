package com.lwc.flowcontrol.aop;

import java.lang.annotation.*;

/**
 * @Auther: liwencai
 * @Date: 2022/7/21 10:46
 * @Description:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface InterfaceStreamControl {
    String desc() default "";
}
