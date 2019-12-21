package com.sjm.common.exception;

import com.sjm.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WlkgException extends RuntimeException {

    private ExceptionEnums exceptionEnums;

}
