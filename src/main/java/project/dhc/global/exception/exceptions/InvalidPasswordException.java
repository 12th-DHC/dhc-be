package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class InvalidPasswordException extends BusinessException {
    public static final InvalidPasswordException EXCEPTION = new InvalidPasswordException();
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
