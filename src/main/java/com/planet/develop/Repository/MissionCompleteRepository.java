package com.planet.develop.Repository;

import com.planet.develop.Entity.MissionComplete;
import com.planet.develop.Login.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionCompleteRepository {
    private final EntityManager em;


    public void save(MissionComplete mission) {
        em.persist(mission);
    }

    public List<MissionComplete> findMissions(User user, int year, int month){
        LocalDate startDate = LocalDate.of(2022,month,1);
        LocalDate endDate = LocalDate.of(2022,month,startDate.lengthOfMonth());

        return em.createQuery("select u from MissionComplete u where u.user= :user and :startDate<=u.date and u.date <= :endDate", MissionComplete.class)
                .setParameter("user",user)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getResultList();
    }

    public MissionComplete findOne(Long id) {
        return em.find(MissionComplete.class, id);
    }
    @Transactional
    public void delete(MissionComplete mission){
        MissionComplete findMission = findOne(mission.getId());
        em.remove(findMission);
    }
}