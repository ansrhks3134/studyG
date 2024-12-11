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
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;


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

        //인증 이메일 보낸다는것을 controller가 알아야 할 필요는 없을 것 같아서 숨
        accountService.processNewAccount(signUpForm);
        return "redirect:/";

    }


    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if (account == null){
            model.addAttribute("error","wrong.email");
            return view;
        }
        if (!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error","wrong.email");
            return view;
        }

        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        model.addAttribute("numberOfUser",accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }



}
