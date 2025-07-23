package OndaProject.Onda.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {



    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(CustomException exception) {
        log.warn("[Custom Exception] message : {}", exception.getMessage());

        return ResponseEntity.status(exception.getHttpStatus()).body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("[Unknown Exception]", ex);
        ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
