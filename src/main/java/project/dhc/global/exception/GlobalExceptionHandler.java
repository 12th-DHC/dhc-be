package project.dhc.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<FieldErrorDto>>> handleValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_DTO_ERR;
        List<FieldErrorDto> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDto(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();
        
        ErrorResponse<List<FieldErrorDto>> response = ErrorResponse.from(details);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse<Void>> handleDateTimeParseException(DateTimeParseException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_DTO_ERR;

        ErrorResponse<Void> response = ErrorResponse.from(errorCode);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_DTO_ERR;

        ErrorResponse<Void> response = ErrorResponse.from(errorCode);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_DTO_ERR;

        ErrorResponse<Void> response = ErrorResponse.from(errorCode);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse<Void> response = ErrorResponse.from(errorCode);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException e
    ) {

        ErrorCode errorCode = ErrorCode.NOT_FOUND;

        ErrorResponse<Void> response =
                ErrorResponse.from(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception: ", e);
        ErrorResponse<Void> response = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERR);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERR.getStatusCode())
                .body(response);
    }
}
