package com.green.gajigaji.user.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.gajigaji.user.UserMapper;
import com.green.gajigaji.user.model.FindPasswordReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

import static com.green.gajigaji.user.datacheck.CommonUser.tempPassword;
import static com.green.gajigaji.user.userexception.ConstMessage.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailSendService {
    private final ObjectMapper om;
    private final UserMapper mapper;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private int authNumber;


    public String CheckAuthNum(String email,String authNum) {
        // 현재시간을 가져와서 문자열로 저장하고 1분뒤 시간과 비교해서
        // 시간이 초과 됐다면 인증번호 만료
        String str = redisUtil.getData(authNum);
        if(str == null) {
            return AUTH_CODE_INCORRECT;
        }
        LocalTime now = LocalTime.now();
        TimeCheckInstance timeCheckInstance = null;
        try {
            timeCheckInstance = om.readValue(str, TimeCheckInstance.class);
        } catch (JsonProcessingException e) {
            return TRY_AGAIN_MESSAGE;
        }
        Duration diff = Duration.between(timeCheckInstance.getNowTest(), now);
        if(diff.toSeconds() > 60) {
            log.info("diff.toSeconds() : {}", diff.toSeconds());
            log.info("timeCheckInstance.getNowTest()) : {}",timeCheckInstance.getNowTest());
            return AUTH_CODE_EXPIRED;
        }
        mapper.checkAuthNum(email);
        return SUCCESS_MESSAGE;
    }

    //임의의 6자리 양수
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    public String joinEmail(String email) {
        if (mapper.emailExists(email) == 0) {
            return UNREGISTERED_EMAIL_MESSAGE;
        }
        if (mapper.checkEmail(email) == 2) {
            return EMAIL_ALREADY_VERIFIED_MESSAGE;
        }
        makeRandomNumber();
        String setFrom = "hajju0617@naver.com"; // email-config에 설정한 자신의 이메일 주소
        String toMail = email;
        String title = "이메일 인증.";
        String content =
                "안녕하세요." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "해당 인증번호를 입력해주세요";
        mailSend(setFrom, toMail, title, content);

        return Integer.toString(authNumber);
    }


    public String setPassword(FindPasswordReq p) {
//        SimpleInfo user = mapper.getSimpleUserInfo(p.getUserEmail());
//        if(user == null) {
//            return 0;
//        }
        String temp = tempPassword(10);

        String hashPw = passwordEncoder.encode(temp);
        p.setUserSetPw(hashPw);

        String setFrom = "hajju0617@naver.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = p.getUserEmail();
        String title = "임시 비밀번호 발급."; // 이메일 제목
        String content =
                "안녕하세요." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "임시 비밀번호는 " + temp + "입니다." +
                        "<br>" +
                        "로그인 후 비밀번호 변경을 꼭 해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        mapper.setPassword(p);
        return temp;
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");

            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        LocalTime temp = LocalTime.now();
        TimeCheckInstance now = new TimeCheckInstance(toMail, temp);
        try {
            String json = om.writeValueAsString(now);
            redisUtil.setData(Integer.toString(authNumber), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
//        redisUtil.setDataExpire(Integer.toString(authNumber),toMail/*받는 사람 이메일*/,60);
//
//        LocalTime oneMinuteLater = now.plusMinutes(1);
//        String timeStr = now.toString();
//        try {
//            Thread.sleep(60 * 1000); // 1분 = 60초 = 60 * 1000 밀리초
//            LocalTime current = LocalTime.now();
//            if (current.isAfter(oneMinuteLater) || current.equals(oneMinuteLater)) {
//                log.info("current : {}", current);
//                log.info("oneMinuteLater : {}", oneMinuteLater);
//                System.out.println("인증 번호 유효시간 초과");
//                timeCheck = true;
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
