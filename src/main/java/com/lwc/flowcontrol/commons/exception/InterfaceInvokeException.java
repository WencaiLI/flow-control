package com.lwc.flowcontrol.commons.exception;

import lombok.ToString;

/**
 * @Auther: liwencai
 * @Date: 2022/7/18 23:06
 * @Description:
 */
@ToString
public class InterfaceInvokeException extends RuntimeException{
    private String message;
    private Throwable cause;
    public InterfaceInvokeException(){
        super();
    }
    public InterfaceInvokeException(String message){
        super(message);
    }
    public InterfaceInvokeException(String message,Throwable cause){
        super(message,cause);
    }
}
