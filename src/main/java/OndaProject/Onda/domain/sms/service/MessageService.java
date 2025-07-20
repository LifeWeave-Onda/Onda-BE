package OndaProject.Onda.domain.sms.service;


import OndaProject.Onda.global.exception.CustomException;
import OndaProject.Onda.global.exception.ExceptionCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MessageService {

    /*
    Pass로 변경시(개발자모드테스트종료) log에 핸드폰번호는 삭제
     */


    private DefaultMessageService messageService;
    private final StringRedisTemplate stringRedisTemplate;
    private final String REDIS_KEY_PREFIX = "verify:";

    @Value("${onda-sms.api.key}")
    private String apiKey;

    @Value("${onda-sms.api.secret}")
    private String apiSecret;

    @Value("${onda-sms.api.url}")
    private String apiUrl;

    @Value("${onda-sms.sender-number}")
    private String smsSenderNumber;

    public MessageService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }

    public String generateVerificationCode() {

        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendVerifyCode(String phoneNumber) {
        String authCode = generateVerificationCode();
        String message = "[Onda] 인증번호: " + authCode;

        try {
            log.info("SMS 인증번호 발송 시도: phoneNumber={}", phoneNumber);
            sendSms(smsSenderNumber, phoneNumber, message);

            String redisKey = REDIS_KEY_PREFIX + phoneNumber;
            stringRedisTemplate.opsForValue().set(redisKey, authCode, 3, TimeUnit.MINUTES);

            log.info("SMS 인증번호 발송 성공: phoneNumber={}", phoneNumber);

        } catch (Exception e) {
            log.error("SMS 인증번호 발송 실패: phoneNumber={}, error={}", phoneNumber, e.getMessage(), e);
            throw new CustomException(ExceptionCode.SMS_SEND_FAILED);
        }
    }



    // 단일 메시지 발송
    public SingleMessageSentResponse sendSms(String from, String to, String text) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);

        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }


    public void verifyCode(String phoneNumber, String inputCode) {
        log.info("인증번호 검증 시도: phoneNumber={}, inputCode={}", phoneNumber, inputCode);

        String redisKey = REDIS_KEY_PREFIX + phoneNumber;
        String storedCode = stringRedisTemplate.opsForValue().get(redisKey);

        if (storedCode == null) {
            log.warn("Redis 에 저장된 인증번호 없음: phoneNumber={}", phoneNumber);
            throw new CustomException(ExceptionCode.VERIFICATION_CODE_NOT_FOUND);
        }

        if (!inputCode.equals(storedCode)) {
            log.warn("인증번호 검증 실패: phoneNumber={}", phoneNumber);
            throw new CustomException(ExceptionCode.VERIFICATION_CODE_MISMATCH);
        }

        stringRedisTemplate.delete(redisKey);
        log.info("인증번호 검증 성공: phoneNumber={}", phoneNumber);
    }

}
