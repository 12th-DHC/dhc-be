package project.dhc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.dto.request.UserLoginRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.entity.Room;
import project.dhc.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(UserLoginRequest request) {
        // 방번호 조회
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 호실입니다."));

        // 입력한 비밀번호와 DB의 BCrypt 해시 비교
        if (!passwordEncoder.matches(
                request.getRoomPassword(),
                room.getRoomPassword()
        )) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        // 로그인 성공
        return new LoginResponse(200, "로그인 완료");
    }
}
