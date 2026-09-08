package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class ErrorTemplateErr extends BusinessException {
	public static final ErrorTemplateErr EXCEPTION = new ErrorTemplateErr();
    public ErrorTemplateErr() {
        super(ErrorCode.ERR_TEMPLATE);
    }
}
