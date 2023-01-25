package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Reply;
import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByUser(User user);
}
