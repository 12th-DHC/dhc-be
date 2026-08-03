package project.dhc.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동으로 만들어주는 어노테이션
public class LoginResponse {

    private int status;
    private String message;
}
