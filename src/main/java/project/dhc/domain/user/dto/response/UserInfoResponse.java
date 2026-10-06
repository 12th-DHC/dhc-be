package project.dhc.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private Integer roomNumber;
    private String aEmail;
    private String bEmail;
    private String aName;
    private String bName;
}