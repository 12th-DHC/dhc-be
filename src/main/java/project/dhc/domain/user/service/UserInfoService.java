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

    // jwt를 이용하여 현재 로그인한 사용자의 정보를 조회
    public UserInfoResponse getUserInfo(String roomNumberString) {

        Integer roomNumber = Integer.valueOf(roomNumberString); // Sting으로 가져온 방 번호 -> Integer로 변환

        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> RoomNotFoundException.EXCEPTION); // Room 조회
        
        // 최종 응답
        return UserInfoResponse.builder()
                .roomNumber(room.getRoomNumber())
                .aEmail(room.getAEmail())
                .bEmail(room.getBEmail())
                .aName(room.getAName())
                .bName(room.getBName())
                .build();
    }
}