package com.studyolle.account;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        //생략하면 객체의 이름이됨
        //model.addAttribute("signUpForm",new SignUpForm());
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

}
