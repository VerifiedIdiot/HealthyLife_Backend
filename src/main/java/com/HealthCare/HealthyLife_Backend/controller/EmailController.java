package com.HealthCare.HealthyLife_Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@RestController
@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private JavaMailSender mailSender; // spring Framework에서 이메일을 보내기 위한 인터페이스

    @GetMapping("/mail")
    public ResponseEntity<String> sendMail(@RequestParam String id) {

        // 임의의 인증 번호 생성
        Random random = new Random();
        int min = 111111;
        int max = 999999;
        String temPw = String.valueOf(random.nextInt(max - min) + min);
        System.out.println("202401 : " + temPw);

        // 이메일에 들어갈 내용
        String htmlContent = "<div style=\"margin: auto; padding: 50px; text-align: center; width: 700px; height: 200px; background-color: #0c134f; border-radius: 20px;\">"
                + "<p style=\"font-size: 30px; color: violet; font-weight: 600;\">Wellv 오신 것을 환영합니다!</p>"
                + "<p style=\"font-size: 16px; color: #d4adfc;\">요청하신 인증번호를 보내드립니다.</p>"
                + "<div style=\"font-size: 20px; color: #ccc;\">" + temPw + "</div>"
                + "</div>";

        ;

        // 이메일로 전송
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            helper.setFrom("wellv2024@naver.com"); // 발신자 주소
            helper.setTo(id); // 수신자 주소
            helper.setSubject("wellv에 오신 것을 환영 합니다!"); // 이메일 제목
            helper.setText(htmlContent, true); // 이메일 내용, true는 HTML 형식을 사용한다는 의미
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage); // 이메일 발송

        return new ResponseEntity<>(temPw, HttpStatus.OK); // 발송된 인증번호와 함께 응답
    }
}