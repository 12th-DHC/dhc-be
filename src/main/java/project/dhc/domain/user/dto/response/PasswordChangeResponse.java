package project.dhc.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordChangeResponse {

    private String message; // 비밀번호 변경 결과 메시지
}