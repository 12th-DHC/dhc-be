package project.dhc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dhc.dto.request.UserLoginRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.entity.Room;
import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;
import project.dhc.global.exception.exceptions.InvalidPasswordException;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.repository.RoomRepository;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.dto.request.EmailRegisterRequest;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoomRepository roomRepository;

    public LoginResponse login(UserLoginRequest request) {
        // 방번호 조회
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);

        // 비밀번호 확인
        if (!room.getRoomPassword().equals(request.getRoomPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        // 로그인 성공
        return new LoginResponse(200, "로그인 완료");
    }
    @Transactional
    public void registerEmail(Integer roomNumber, EmailRegisterRequest request) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);
        if ("A".equals(request.area())) {
            room.setAEmail(request.email());
        } else if ("B".equals(request.area())) {
            room.setBEmail(request.email());
        }
    }
}
