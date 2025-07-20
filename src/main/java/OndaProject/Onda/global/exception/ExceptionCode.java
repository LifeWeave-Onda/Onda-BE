package OndaProject.Onda.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ExceptionCode {

    // member
    MEMBER_NOT_FOUND("ACCOUNT-001","사용자를 찾을 수 없습니다.", NOT_FOUND),



    //Sms
    SMS_SEND_FAILED("SMS-001", "SMS 발송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    VERIFICATION_CODE_NOT_FOUND("SMS-002", "인증번호가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    VERIFICATION_CODE_MISMATCH("SMS-003", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ExceptionCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status.value();
    }
}
