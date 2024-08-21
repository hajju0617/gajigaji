package com.green.gajigaji.user.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.gajigaji.common.exception.CommonErrorCode;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.user.UserMapper;
import com.green.gajigaji.user.email.model.TimeCheckInstance;
import com.green.gajigaji.user.model.FindPasswordReq;
import com.green.gajigaji.user.usercommon.UserErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

import static com.green.gajigaji.common.GlobalConst.SUCCESS_MESSAGE;
import static com.green.gajigaji.user.usercommon.CommonUser.tempPassword;
import static com.green.gajigaji.user.usercommon.UserErrorMessage.*;


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
            throw new CustomException(AUTH_CODE_INCORRECT);
        }
        LocalTime now = LocalTime.now();
        TimeCheckInstance timeCheckInstance = null;
        try {
            timeCheckInstance = om.readValue(str, TimeCheckInstance.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(TRY_AGAIN_MESSAGE);
        }
        Duration diff = Duration.between(timeCheckInstance.getNowTest(), now);
        if(diff.toSeconds() > 60) {
            log.info("diff.toSeconds() : {}", diff.toSeconds());
            log.info("timeCheckInstance.getNowTest()) : {}",timeCheckInstance.getNowTest());
            throw new CustomException(AUTH_CODE_EXPIRED);
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

    public String joinEmail(String userEmail) {
        if (mapper.emailExists(userEmail) == 0) {
            throw new CustomException(UNREGISTERED_EMAIL_MESSAGE);
        }
        if (mapper.checkEmail(userEmail) == 2) {
            throw new CustomException(EMAIL_ALREADY_VERIFIED_MESSAGE);
        }
        makeRandomNumber();
        String setFrom = "hajju0617@naver.com"; // email-config에 설정한 자신의 이메일 주소
        String toMail = userEmail;
        String title = "이메일 인증.";
        String content =
                "안녕하세요." + 	//html 형식으로 작성
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
        int result = mapper.emailExists(p.getUserEmail());
        if(result == 0) {
            throw new CustomException(NOT_FOUND_MESSAGE);
        }

        String temp = tempPassword(10);

        String hashPw = passwordEncoder.encode(temp);
        p.setUserSetPw(hashPw);

        String setFrom = "hajju0617@naver.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = p.getUserEmail();
        String title = "임시 비밀번호 발급."; // 이메일 제목
        String content =
                "안녕하세요." +
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
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        LocalTime temp = LocalTime.now();
        TimeCheckInstance now = new TimeCheckInstance(toMail, temp);
        try {
            String json = om.writeValueAsString(now);
            redisUtil.setData(Integer.toString(authNumber), json);
        } catch (JsonProcessingException e) {
            throw new CustomException(UserErrorMessage.TRY_AGAIN_MESSAGE);
        }
    }

    public void handlePartyRequest(String email, String text) {
        String setFrom = "hajju0617@naver.com";
        String toMail = email;
        String title = "안녕하세요 가지가지입니다";
        String content = "<img src='cid:partyImage'>" +
                "<br><br>" +
                "안녕하세요 가까운 지역, 가까운 지인 " +
                "<br>" +
                "가지가지 웹사이트를 운영하고 있는 관리자입니다." +
                "<br>" +
                "모임 생성에 대한 심사 결과를 알려드립니다." +
                "<br><br>" +
                text;
        partyMailSend(setFrom, toMail, title, content);
    }

    //이메일을 전송합니다.
    public void partyMailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);

            ClassPathResource imageResource = new ClassPathResource("static/static/media/gajigaji.png");
            helper.addInline("partyImage", imageResource);

            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
    }
}
