package com.studyolle.account;


import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) //TODO encoding 해야함
                .sStudyCreatedByWeb(true)
                .studyEnrollmentResultByEmail(true)
                .studyUpdatedByWeb(true)
                .build();

        //회원 저장
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

    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        //토큰 만들고
        newAccount.generateEmailCheckToken();
        //메세지 만들기
        sendSignUpconfirmEmail(newAccount);

    }
}

