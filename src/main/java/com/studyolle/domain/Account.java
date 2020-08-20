package com.studyolle.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    //이메일 인증
    private boolean emailVerified;

    //이메일 검증 토큰값
    private String emailCheckToken;

    private LocalDateTime joinedAt;

    //프로필 관련

    //자기소개
    private String bio;

    private String url;

    //직업
    private String occupation;

    //사는곳
    private String location;

    //더 커질수도있어서 Lob를
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    //스터디가 만들어진것을 이메일로 받을 것인가
    private boolean studyCreatedByEmail;

    private boolean sStudyCreatedByWeb;

    //스터디모임에 가입신청을 이메일로 받을 것인가
    private boolean studyEnrollmentResultByEmail;

    private boolean sStudyEnrollmentResultByWeb;

    //갱신된 정보를 이메일로 받을 것인가
    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
