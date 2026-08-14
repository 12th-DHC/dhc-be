package project.dhc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordChangeResponse {

    private int status; // HTTP 상태 코드
    private String message; // 비밀번호 변경 결과 메시지
}