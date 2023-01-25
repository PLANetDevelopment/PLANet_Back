package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.StarTalk.Scrap;
import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findAllByUser(User user);
    Scrap findByPostAndUser(Post post, User user);
}
