package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public static final RefreshTokenNotFoundException EXCEPTION = new RefreshTokenNotFoundException();
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_ROUND);
    }
}
