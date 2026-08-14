package project.dhc.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동으로 만들어주는 어노테이션
public class LoginResponse {

    private int status; // http 상태 코드
    private String message; // 로그인 결과 메세지
    private String accessToken; // JWT Access Token
}
