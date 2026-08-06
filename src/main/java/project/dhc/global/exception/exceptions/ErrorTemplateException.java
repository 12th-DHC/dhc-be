package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class ErrorTemplateException extends BusinessException {
    public static final ErrorTemplateException EXCEPTION = new ErrorTemplateException();
    public ErrorTemplateException() {
        super(ErrorCode.ERR_TEMPLATE);
    }
}
