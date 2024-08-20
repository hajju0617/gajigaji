package com.green.gajigaji.user;

import com.green.gajigaji.user.jpa.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsUserEntityByUserNickname(String userNickname);

    boolean existsUserEntityByUserEmail(String userEmail);

    UserEntity getUserEntityByUserEmail(String userEmail);

    @Query("SELECT u.userEmail " +
           "FROM userMaster u " +
           "WHERE u.userName = :userName AND u.userPhone = :userPhone AND u.userBirth = :userBirth")
    String findUserId(@Param("userName") String userName
                    , @Param("userPhone") String userPhone
                    , @Param("userBirth") Date userBirth);      // DB 테이블 컬럼과 타입 맞추기


}
