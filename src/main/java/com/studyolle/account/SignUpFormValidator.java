package com.studyolle.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;




    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    //object Form에서 오는 객체를 실제로 담는것 errors는 객체를 바인팅할때 에러를 담는
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) object;
        if (accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email", new Object[]{signUpForm.getEmail()},"使用中のメールアドレスです。");
        }

        if (accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname","invalid.nickname", new Object[]{signUpForm.getNickname()},"使用中のニックネームです。");

        }
    }
}
