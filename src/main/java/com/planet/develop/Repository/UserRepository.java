package com.planet.develop.Repository;

import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {
    // 사용자 이름 업데이트
    @Transactional
    @Modifying
    @Query(" update User u SET u.nickname = :nickname where u.userId = :user_id")
    void updateName(@Param("user_id") String user_id, @Param("nickname") String nickname);
    User findByUserId(String userId);
}
