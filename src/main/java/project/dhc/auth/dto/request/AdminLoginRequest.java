package project.dhc.auth.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequest {

    private String adminPassword;
}
