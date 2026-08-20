package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class CleaningSearchNotFoundException extends BusinessException {
    public static final CleaningSearchNotFoundException EXCEPTION = new CleaningSearchNotFoundException();
    public CleaningSearchNotFoundException() {
        super(ErrorCode.CLEANING_SEARCH_NOT_FOUND);
    }
}
