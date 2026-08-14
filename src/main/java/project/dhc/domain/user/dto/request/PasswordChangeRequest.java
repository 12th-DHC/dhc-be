package project.dhc.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordChangeRequest {

    private Integer roomNumber;
    private String currentPassword;
    private String newPassword;
}