package project.dhc.global.exception;

public record FieldErrorDto(
        String field,
        String message
) {
}

