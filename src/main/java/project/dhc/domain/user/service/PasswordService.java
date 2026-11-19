package project.dhc.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.user.dto.request.PasswordChangeRequest;
import project.dhc.domain.user.dto.response.PasswordChangeResponse;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.InvalidPasswordException;
import project.dhc.global.exception.exceptions.RoomNotFoundException;

@Service // 비밀번호 변경 관련 비즈니스 로직
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class PasswordService {

    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;


    // 비밀번호 변경
    @Transactional // 비밀번호 변경 내용을 DB에 반영
    public PasswordChangeResponse changePassword(
            String authenticatedRoomNumber,
            PasswordChangeRequest request
    ) {

        // 인증된 사용자 방 번호로 조회
        Room room = roomRepository.findByRoomNumber(Integer.valueOf(authenticatedRoomNumber))
                .orElseThrow(() ->
                        RoomNotFoundException.EXCEPTION
                );

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                room.getRoomPassword()
        )) {
            throw InvalidPasswordException.EXCEPTION;
        }

        // 새 비밀번호 암호화
        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        // 새 비밀번호 저장
        room.setRoomPassword(encodedPassword);

        // 비밀번호 변경 성공 응답
        return new PasswordChangeResponse(
                "비밀번호가 성공적으로 변경되었습니다."
        );
    }
}