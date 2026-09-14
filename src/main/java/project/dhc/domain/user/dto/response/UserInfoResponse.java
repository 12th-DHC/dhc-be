package project.dhc.domain.user.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponse {

    private int status; // http 상태 코드
    private Data data; // 사용자 정보
    private String message; // 응답 메세지

    public UserInfoResponse(int status, Data data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    // 사용자 정보를 담는 객체
    @Getter
    @NoArgsConstructor
    public static class Data{

        private Integer roomNumber; // 방 번호
        private String aEmail; // A학생 이메일
        private String bEmail; // B학생 이메일

        public Data(Integer roomNumber, String aEmail, String bEmail) {
            this.roomNumber = roomNumber;
            this.aEmail = aEmail;
            this.bEmail = bEmail;
        }
    }
}
