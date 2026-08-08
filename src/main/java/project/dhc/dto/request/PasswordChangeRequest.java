package project.dhc.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {

    private Integer roomNumber;
    private String currentPassword;
    private String newPassword;
}