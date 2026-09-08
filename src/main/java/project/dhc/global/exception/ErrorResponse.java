package project.dhc.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status; // http 상태 메세지
    private String message; // 에러 메세지
}
