package project.dhc.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.user.dto.request.PasswordChangeRequest;
import project.dhc.domain.user.dto.response.PasswordChangeResponse;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PasswordChangeResponse changePassword(PasswordChangeRequest request) {

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
        return new PasswordChangeResponse(
                200,
                "비밀번호가 성공적으로 변경되었습니다."
        );
    }
}