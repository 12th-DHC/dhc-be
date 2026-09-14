package project.dhc.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dhc.domain.user.dto.response.UserInfoResponse;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.global.util.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final RoomRepository roomRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // jwt를 이용하여 현재 로그인한 사용자의 정보를 조회
    public UserInfoResponse getUserInfo(String token) {

        String jwt = token.substring(7); // Authorization 헤더의 "Bearer " 부분 제거
        String subject = jwtTokenProvider.getSubject(jwt); // JWT의 subject에서 방 번호 가져오기
        Integer roomNumber = Integer.valueOf(subject); // Sting으로 가져온 방 번호 -> Integer로 변환

        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> RoomNotFoundException.EXCEPTION); // Room 조회

        UserInfoResponse.Data data = new UserInfoResponse.Data(
                room.getRoomNumber(),
                room.getAEmail(),
                room.getBEmail()
        );
        
        // 최종 응답
        return new UserInfoResponse(
                200,
                data,
                "정보 조회 완료"
        );
    }
}