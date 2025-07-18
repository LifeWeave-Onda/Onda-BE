package OndaProject.Onda.domain.sms.controller;


import OndaProject.Onda.domain.sms.dto.SmsRequest;
import OndaProject.Onda.domain.sms.service.MessageService;
import OndaProject.Onda.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send-verification")
    public CommonResponse<Boolean> sendVerificationSms(@RequestBody @Valid SmsRequest.PhoneNumber smsRequest){

        String phoneNumber = smsRequest.phoneNumber();

        if(phoneNumber.isEmpty()){
            throw new IllegalArgumentException("No Argument");
        }

        if (!phoneNumber.matches("^010\\d{8}$")) {
            throw new IllegalArgumentException("유효하지 않은 휴대폰 번호 형식입니다.");
        }

        messageService.sendVerifyCode(phoneNumber);

        return CommonResponse.ok(true);


    }

    @PostMapping("/verify-code")
    public CommonResponse<Boolean> verifyCode(@RequestBody SmsRequest.VerifyCode verifyCode){

        boolean result = messageService.verifyCode(verifyCode.phoneNumber(),verifyCode.code());
        return CommonResponse.ok(result);

    }
}
