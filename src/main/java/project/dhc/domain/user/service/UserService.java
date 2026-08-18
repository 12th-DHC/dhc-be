package project.dhc.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.domain.user.dto.request.UserLoginRequest;
import project.dhc.domain.admin.dto.response.LoginResponse;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.CustomException;
import project.dhc.global.exception.ErrorCode;
import project.dhc.global.util.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(UserLoginRequest request) {
        // 방번호 조회
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        // 입력한 비밀번호와 DB의 BCrypt 해시 비교
        if (!passwordEncoder.matches(
                request.getRoomPassword(),
                room.getRoomPassword()
        )) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // USER라는 subject와 USER 권한을 JWT에 저장
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        String.valueOf(room.getRoomNumber()),
                        "USER"
                );

        // 로그인 성공
        return new LoginResponse(
                200,
                "로그인 완료",
        accessToken
                );
    }
}