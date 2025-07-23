package OndaProject.Onda.domain.sms.api;

import OndaProject.Onda.domain.sms.dto.SmsRequest;
import OndaProject.Onda.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface MessageApi {

    @Operation(summary = "인증번호 SMS 발송", description = "휴대폰 번호로 인증번호 SMS를 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 발송 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "성공", value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "인증번호 발송 성공",
                                        "data": true
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "SMS 발송 실패", value = """
                                    {
                                        "code": "SMS-001",
                                        "message": "SMS 발송에 실패했습니다."
                                    }
                                    """)
                    }))
    })
    CommonResponse<Boolean> sendVerificationSms(SmsRequest.PhoneNumber smsRequest);

    @Operation(summary = "인증번호 검증", description = "입력한 인증번호를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "성공", value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "요청이 성공적으로 처리되었습니다.",
                                        "data": true
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "404", description = "인증번호가 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "인증번호 없음", value = """
                                    {
                                        "code": "SMS-002",
                                        "message": "인증번호가 존재하지 않습니다."
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "인증번호 불일치",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "인증 실패", value = """
                                    {
                                        "code": "SMS-003",
                                        "message": "인증번호가 일치하지 않습니다."
                                    }
                                    """)
                    }))
    })
    CommonResponse<Boolean> verifyCode(SmsRequest.VerifyCode verifyCode);
}

