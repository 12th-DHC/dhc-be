package project.dhc.domain.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordChangeRequest {

    private String currentPassword;
    private String newPassword;
}
