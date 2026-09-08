package project.dhc.global.exception;
// 에러 상태 코드와 메세지를 모아두는 곳


import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    ADMIN_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "관리자 계정이 존재하지 않습니다."
    ),

    ROOM_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "존재하지 않는 호실입니다."
    ),
    INVALID_PASSWORD(
            HttpStatus.UNAUTHORIZED,
            "비밀번호가 올바르지 않습니다."
    );

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
