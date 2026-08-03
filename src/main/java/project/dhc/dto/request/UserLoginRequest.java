package project.dhc.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {

    private Integer roomNumber;
    private String roomPassword;
}
