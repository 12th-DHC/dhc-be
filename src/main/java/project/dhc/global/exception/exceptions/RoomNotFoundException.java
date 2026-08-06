package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class RoomNotFoundException extends BusinessException {
    public static final RoomNotFoundException EXCEPTION = new RoomNotFoundException();
    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }
}
