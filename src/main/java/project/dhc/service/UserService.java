package project.dhc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dhc.dto.request.UserLoginRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.entity.Room;
import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;
import project.dhc.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoomRepository roomRepository;

    public LoginResponse login(UserLoginRequest request) {
        // 방번호 조회
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 비밀번호 확인
        if (!room.getRoomPassword().equals(request.getRoomPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인 성공
        return new LoginResponse(200, "로그인 완료");
    }
}
