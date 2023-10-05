package com.example.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException handleMethodArgumentNotValidException(NotFoundException ex) {
        return new NotFoundException("Not found");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestException handleBadRequestException(BadRequestException ex) {
        return new BadRequestException("Bad_Request");
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflictException handleConflictException(ConflictException ex) {
        return new ConflictException("Conflict");
    }


}
