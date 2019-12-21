package com.sjm.common.advice;

import com.sjm.common.dto.ExceptionResult;
import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(WlkgException.class)
    public  ResponseEntity<ExceptionResult> handleException(WlkgException e){
        ExceptionEnums em = e.getExceptionEnums();
        return ResponseEntity.status(HttpStatus.OK).body(new ExceptionResult(em));
    }
}
