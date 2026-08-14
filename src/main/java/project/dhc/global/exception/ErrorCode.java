package project.dhc.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ERR_TEMPLATE(200, "ERR_TEMPLATE", "에러 템플릿입니다."),

    NOT_VALID_DTO_ERR(400, "NOT_VALID_DTO_ERR", "유효하지 않은 요청입니다."),

    INVALID_PASSWORD(401, "INVALID_PASSWORD", "비밀번호가 올바르지 않습니다."),

    ROOM_NOT_FOUND(404, "ROOM_NOT_FOUND", "존재하지 않는 호실입니다."),

    INTERNAL_SERVER_ERR(500, "INTERNAL_SERVER_ERR", "서버 측 오류가 발생했습니다.");
    private Integer statusCode;
    private String errorCode;
    private String errorMessage;
}
