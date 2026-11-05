package project.dhc.global.exception.exceptions;

import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

public class NotificationDataIncompleteException extends BusinessException {
    public static final NotificationDataIncompleteException EXCEPTION = new NotificationDataIncompleteException();

    public NotificationDataIncompleteException() {
        super(ErrorCode.NOTIFICATION_DATA_INCOMPLETE);
    }
}
