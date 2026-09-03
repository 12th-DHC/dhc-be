package project.dhc.global.exception;

import java.util.List;

public record ErrorResponse<T>(
	boolean success,
    ErrorDTO<T> error
) {
	public static ErrorResponse<Void> from(ErrorCode errorCode) {
        ErrorDTO<Void> errorDTO = new ErrorDTO<>(errorCode.getErrorCode(), errorCode.getErrorMessage(), null);
        return new ErrorResponse<>(
                false,
                errorDTO
        );
    }
	
	public static ErrorResponse<List<FieldErrorDto>> from(List<FieldErrorDto> detail) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_DTO_ERR;
        ErrorDTO<List<FieldErrorDto>> errorDTO = new ErrorDTO<>(errorCode.getErrorCode(), errorCode.getErrorMessage(), detail);
        return new ErrorResponse<>(
                false,
                errorDTO
        );
    }
}
