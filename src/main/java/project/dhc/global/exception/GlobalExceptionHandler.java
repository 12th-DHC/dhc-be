package project.dhc.global.exception;
// Service에서 발생한 예외를 받아서 HTTP 응답으로 변환해주는 역할

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException exception
    ) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode.getStatus().value(), errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
