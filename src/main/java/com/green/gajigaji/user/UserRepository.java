package com.green.gajigaji.user;

import com.green.gajigaji.user.jpa.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsUserEntityByUserNickname(String userNickname);


}
