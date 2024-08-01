package com.green.gajigaji.user.jpa;

import com.green.gajigaji.plan.jpa.UpdatedAt;
import com.green.gajigaji.security.SignInProviderType;
import com.green.gajigaji.user.model.SignUpReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name = "userMaster")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignInProviderType providerType;

    @Column(length = 50, nullable = false)
    private String userEmail;

    @Column(length = 100, nullable = false)
    private String userPw;

    @Column(length = 20, nullable = false)
    private String userName;

    @Column(length = 100, nullable = false)
    private String userAddr;

    @Column(length = 20, nullable = false)
    private String userNickname;

    @Column(length = 50)
    private String userFav;

    @Temporal(TemporalType.DATE)
    private Date userBirth;

    @Column(length = 6, nullable = false)
    private int userGender;

    @Column(length = 20, nullable = false)
    private String userPhone;

    @Column(length = 1000)
    private String userIntro;

    @Column(length = 6, nullable = false)
    private int userGb;

    @Column(length = 100, nullable = false)
    private String userPic;

    @Column(length = 6, nullable = false)
    private int userState;

    @Column(length = 15, nullable = false)
    private String userRole;

    public UserEntity(SignUpReq p) {
        this.setUserEmail(p.getUserEmail());
        this.setUserPw(p.getUserPw());
        this.setUserName(p.getUserName());
        this.setUserAddr(p.getUserAddr());
        this.setUserNickname(p.getUserNickname());
        this.setUserGender(p.getUserGender());
        this.setUserPhone(p.getUserPhone());
        this.setUserIntro(p.getUserIntro());
        this.setInputDt(LocalDateTime.now());
    }

}

