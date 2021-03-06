package com.sjm.common.dto;

import com.sjm.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {
    private Integer status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums) {
        this.status = exceptionEnums.getCode();
        this.message = exceptionEnums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
