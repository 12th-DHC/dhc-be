package project.dhc.domain.user.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private Integer roomNumber;
    private String roomPassword;
}
