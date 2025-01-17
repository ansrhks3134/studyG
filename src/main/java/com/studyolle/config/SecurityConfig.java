package com.studyolle.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //인증 체크안하고 할 수 있도록 서큐리티 재설정
   @Override
    protected void configure(HttpSecurity http) throws Exception {
       //인증 가로채기
        http.authorizeRequests().mvcMatchers("/","/login","/sign-up","/check-email","/check-email-token",
                "/email-login","/check-email-login","/login-link").permitAll()
        .mvcMatchers(HttpMethod.GET,"/profile/*").permitAll()
        .anyRequest().authenticated();
    }


    //static한 resource들은 security 필터를 적용하지 말아라
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
