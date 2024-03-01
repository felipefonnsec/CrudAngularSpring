package com.felipe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.felipe.exeption.RecordNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class AplicationControllerAdvice {
    //tratamento de exception
    
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RecordNotFoundException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handeMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        return exception.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .reduce(" ", (acc, error) -> acc + error + "\n");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handeConstraintViolationException(ConstraintViolationException exception){
        return exception.getConstraintViolations().stream()
        .map(error -> error.getPropertyPath() + " " + error.getMessage())
        .reduce(" ", (acc, error) -> acc + error + "\n");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handeMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
        if (exception != null && exception.getRequiredType() != null) {
            @SuppressWarnings("null")
            String type = exception.getRequiredType().getName();
            String[] typeParts = type.split("\\.");
            String typeName = typeParts[typeParts.length - 1];
            return exception.getName() + " should be of type " + typeName;     
        }
        return "Agumento não valido";
    }

}
