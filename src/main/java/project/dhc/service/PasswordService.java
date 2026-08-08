package project.dhc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.dto.request.PasswordChangeRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.entity.Room;
import project.dhc.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse changePassword(PasswordChangeRequest request) {

        // 방 번호로 방 찾기
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 호실입니다.")
                );

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                room.getRoomPassword()
        )) {
            throw new RuntimeException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호를 BCrypt로 해싱
        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        // DB에 해싱된 비밀번호 저장
        room.setRoomPassword(encodedPassword);
        roomRepository.save(room);

        // 성공 응답
        return new LoginResponse(
                200,
                "비밀번호가 성공적으로 변경되었습니다."
        );
    }
}