package OndaProject.Onda.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public int getHttpStatus() {
        return this.exceptionCode.getStatus();
    }
}
