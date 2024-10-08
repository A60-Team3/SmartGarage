package org.example.smartgarage.handlers;

import io.jsonwebtoken.security.SignatureException;
import org.example.smartgarage.dtos.response.ApiErrorResponseDTO;
import org.example.smartgarage.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(annotations = RestController.class)
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,@NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(objectError -> errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage()));

        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
        return new ResponseEntity<>(apiErrorResponseDTO, headers, status);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<?> handleCustomAuthenticationException(CustomAuthenticationException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), List.of(ex.getMessage()));

        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<?> handleEntityDuplicateException(EntityDuplicateException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.CONFLICT, ex.getLocalizedMessage(), List.of(ex.getMessage()));

        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<?> handleAccountStatusException(AccountStatusException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidFilterArgumentException.class)
    public ResponseEntity<?> handleDateTimeException(InvalidFilterArgumentException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionRequestException.class)
    public ResponseEntity<?> handleDateTimeException(ConversionRequestException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleDateTimeException(IOException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleDateTimeException(UsernameNotFoundException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserMismatchException.class)
    public ResponseEntity<?> handleDateTimeException(UserMismatchException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VisitMismatchException.class)
    public ResponseEntity<?> handleVisitMismatchException(VisitMismatchException ex) {
        ApiErrorResponseDTO apiErrorResponseDTO =
                new ApiErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}
