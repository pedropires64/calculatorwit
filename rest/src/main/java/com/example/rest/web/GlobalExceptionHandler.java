package com.example.rest.web;

import com.example.rest.model.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Restringe o advice aos controllers da API (CalcController),
 * não interfere com endpoints do Springdoc/Swagger.
 */
@RestControllerAdvice(assignableTypes = { CalcController.class })
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField() + " " + (fe.getDefaultMessage() == null ? "is invalid" : fe.getDefaultMessage()))
        .collect(Collectors.joining("; "));
    return ResponseEntity.badRequest().body(new ApiError(msg));
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<ApiError> handleNumberFormat(NumberFormatException ex) {
    return ResponseEntity.badRequest().body(new ApiError("Invalid number format"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    // Erros genéricos no nosso controller: 504
    return new ResponseEntity<>(new ApiError("Upstream timeout"), new HttpHeaders(), HttpStatus.GATEWAY_TIMEOUT);
  }
}
