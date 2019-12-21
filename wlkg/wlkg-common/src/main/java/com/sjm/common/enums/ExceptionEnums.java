package com.sjm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    Object_Null(401,"对象为空"),
    URL_CANNOT_BE_NULL(501,"地址不能为空"),
    FILE_TYPE_MISMATCH(502,"文件类型不匹配"),
    LIST_NULL(502,"列表为空"),
    FILE_ERROR(503,"文件上传错误") ;
    private Integer code;
    private String msg;
}
