package OndaProject.Onda.domain.sms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SmsRequest{

        public record PhoneNumber(
                @NotBlank
                @Pattern(regexp = "^010\\d{8}$")
                String phoneNumber
        ){}

        public record VerifyCode(
                @NotBlank
                String phoneNumber,
                @NotBlank
                String code
        ){}

}
