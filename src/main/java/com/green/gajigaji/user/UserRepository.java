package com.green.gajigaji.user;

import com.green.gajigaji.security.SignInProviderType;
import com.green.gajigaji.user.jpa.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsUserEntityByUserNickname(String userNickname);

    boolean existsUserEntityByUserEmail(String userEmail);


    UserEntity findUserEntityByUserEmailAndProviderType(String userEmail, SignInProviderType providerType);
    UserEntity findUserEntityByUserSeq(Long userSeq);

    @Query("SELECT u.userEmail " +
           "FROM userMaster u " +
           "WHERE u.userName = :userName AND u.userPhone = :userPhone AND u.userBirth = :userBirth")
    String findUserId(@Param("userName") String userName
                    , @Param("userPhone") String userPhone
                    , @Param("userBirth") Date userBirth);      // DB 테이블 컬럼과 타입 맞추기

    @Query("SELECT u.userPw FROM userMaster u WHERE u.userSeq = :userSeq")
    String findUserPwByUserSeq(Long userSeq);

    boolean existsUserEntityByUserSeq(Long userSeq);

    @Transactional
    @Modifying
    @Query("UPDATE userMaster u SET u.userEmail = CONCAT(u.userEmail, '_', :userSeq), u.userState = 2 " +
            "WHERE u.userSeq = :userSeq AND u.userState = 1")
    int deactivateUser(Long userSeq);








}
