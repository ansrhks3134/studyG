package com.studyolle.account;


import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        //토큰 만들고
        //newAccount가 Detected객체여 DB에 싱크가 되지않음. 토큰값저장안됨 그래서
        //@Transactional를 사용
        newAccount.generateEmailCheckToken();
        //메세지 만들기
        sendSignUpconfirmEmail(newAccount);
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .sStudyCreatedByWeb(true)
                .studyEnrollmentResultByEmail(true)
                .studyUpdatedByWeb(true)
                .build();

        //회원 저장
        //save안에서는 transtion처리
        return accountRepository.save(account);
    }

    private void sendSignUpconfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("スタディーG、会員登録認証");
        mailMessage.setText("/check-email-token?token="+newAccount.getEmailCheckToken()
                +"&email="+newAccount.getEmail());
        //메세지 보내기
        javaMailSender.send(mailMessage);
    }


}

