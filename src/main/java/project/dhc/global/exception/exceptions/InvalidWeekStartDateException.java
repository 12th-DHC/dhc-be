package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class InvalidWeekStartDateException extends BusinessException {
    public static final InvalidWeekStartDateException EXCEPTION = new InvalidWeekStartDateException();
    public InvalidWeekStartDateException() {
        super(ErrorCode.INVALID_WEEK_START_DATE);
    }
}
