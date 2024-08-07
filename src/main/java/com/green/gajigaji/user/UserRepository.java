package com.green.gajigaji.user;

import com.green.gajigaji.user.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


}
