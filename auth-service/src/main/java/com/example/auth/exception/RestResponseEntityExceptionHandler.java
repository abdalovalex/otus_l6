package com.example.auth.exception;

import com.example.auth.dto.ErrorResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ResponseStatusException.class)
    protected ResponseEntity<ErrorResponseDTO> handleConflict(ResponseStatusException exp) {
        ErrorResponseDTO errorResponseDto = new ErrorResponseDTO();
        errorResponseDto.setCode(0);
        errorResponseDto.setMessage(exp.getReason());
        return new ResponseEntity<>(errorResponseDto, exp.getStatusCode());
    }
}
