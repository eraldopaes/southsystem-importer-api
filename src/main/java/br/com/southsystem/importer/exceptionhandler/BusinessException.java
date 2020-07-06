package br.com.southsystem.importer.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public BusinessException(String errorCode) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
    }

    public BusinessException(HttpStatus status, String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }
}
