package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class InvalidRefreshTokenException extends BusinessException {
    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
