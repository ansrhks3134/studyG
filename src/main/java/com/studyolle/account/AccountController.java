package com.studyolle.account;


import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

    //signUpForm가 아래의 signUpSubmit의 signUpForm과 mapping
    @InitBinder("signUpForm")
    public void iniBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        //생략하면 객체의 이름이됨
        //model.addAttribute("signUpForm",new SignUpForm());
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }


    //@Valid를 통해 검사 후 Errors 에러가 생기면 if문으로
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }

        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) //TODO encoding 해야함
                .sStudyCreatedByWeb(true)
                .studyEnrollmentResultByEmail(true)
                .studyUpdatedByWeb(true)
                .build();

        //회원 저장
        Account newAccount = accountRepository.save(account);

        //토큰 만들고
        newAccount.generateEmailCheckToken();
        //메세지 만들기
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("スタディーG、会員登録認証");
        mailMessage.setText("/check-email-token?token="+newAccount.getEmailCheckToken()
                +"&email="+newAccount.getEmail());
        //메세지 보내기
        javaMailSender.send(mailMessage);

        //TODO 회원 가입 처리
        return "redirect:/";

    }

}
