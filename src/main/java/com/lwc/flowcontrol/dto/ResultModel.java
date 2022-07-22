package com.lwc.flowcontrol.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Deprecated
public class ResultModel<T> {
    int code;
    String message;
    T content;

    public ResultModel(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResultModel(int code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }
    public static <T> ResultModel<T> success (T content){
        return new ResultModel(200,"success",content);
    }
    public static <T> ResultModel<T> success (String message, T content){
        return new ResultModel(200,message,content);
    }
    public static <T> ResultModel<T> success (int code, String message, T content){
        return new ResultModel(code,message,content);
    }

    public static <T> ResultModel<T> error (){
        return new ResultModel(500,"error");
    }
    public static <T> ResultModel<T> error (int code, String message){
        return new ResultModel(code,message);
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
