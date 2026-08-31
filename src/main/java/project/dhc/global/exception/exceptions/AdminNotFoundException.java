package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class AdminNotFoundException extends BusinessException {
	public static final AdminNotFoundException EXCEPTION = new AdminNotFoundException();
    public AdminNotFoundException() {
        super(ErrorCode.ADMIN_NOT_FOUND);
    }
}
