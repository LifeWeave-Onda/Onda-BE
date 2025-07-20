package OndaProject.Onda.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    public static ErrorResponse from(CustomException exception) {
        return new ErrorResponse(
                exception.getExceptionCode().getCode(),
                exception.getMessage(),
                exception.getHttpStatus()
        );
    }


}
