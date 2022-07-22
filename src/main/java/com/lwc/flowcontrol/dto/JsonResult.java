package com.lwc.flowcontrol.dto;

/**
 * Tianye © Copyright 2020
 *
 * @Description: 返回统一格式
 * @ClassName: JsonResult
 * @Package: com.thtf.utm.controller
 * @CreateDate: Created in 2020-3-20 17:49
 * @Author: Tiany
 */

import java.io.Serializable;

public class JsonResult implements Serializable {

    private static final long serialVersionUID = 3785382972587997562L;

    private String status = null;
    private String description = null;
    private Object data = null;

    public JsonResult status(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
