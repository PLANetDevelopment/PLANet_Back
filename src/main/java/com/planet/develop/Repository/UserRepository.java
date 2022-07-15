package com.planet.develop.Repository;

import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    // 사용자 이름 업데이트
    @Query(" update User u SET u.nickname = :nickname where u.userId = :user_id")
    void updateName(@Param("user_id") String user_id, @Param("nickname") String nickname);
}
