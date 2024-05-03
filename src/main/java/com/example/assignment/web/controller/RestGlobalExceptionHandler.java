package com.example.assignment.web.controller;

import com.example.assignment.core.exception.UserNotFoundException;
import com.example.assignment.core.exception.response.ErrorResponse;
import com.example.assignment.core.exception.response.FieldDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestGlobalExceptionHandler {
    private static final String VALIDATION_TITLE = "Validation failed";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldDetail> fieldDetailList = e.getFieldErrors()
                .stream()
                .map(fieldError -> new FieldDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                .sorted(Comparator.comparing(FieldDetail::getFieldName).thenComparing(FieldDetail::getMessage))
                .collect(Collectors.toList());
        ErrorResponse<List<FieldDetail>> errorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), VALIDATION_TITLE, fieldDetailList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintViolationException(ConstraintViolationException e) {
        List<FieldDetail> fieldDetailList = e.getConstraintViolations()
                .stream()
                .map(fieldError -> new FieldDetail(fieldError.getPropertyPath().toString(), fieldError.getMessage()))
                .sorted(Comparator.comparing(FieldDetail::getFieldName).thenComparing(FieldDetail::getMessage))
                .collect(Collectors.toList());
        ErrorResponse<List<FieldDetail>> errorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), VALIDATION_TITLE, fieldDetailList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> onBindException(BindException e) {
        List<FieldDetail> fieldDetailList = e.getFieldErrors()
                .stream()
                .map(fieldError -> new FieldDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                .sorted(Comparator.comparing(FieldDetail::getFieldName).thenComparing(FieldDetail::getMessage))
                .collect(Collectors.toList());
        ErrorResponse<List<FieldDetail>> errorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), VALIDATION_TITLE, fieldDetailList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> onUserNotFoundException(UserNotFoundException e) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> onRuntimeException(RuntimeException e) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
