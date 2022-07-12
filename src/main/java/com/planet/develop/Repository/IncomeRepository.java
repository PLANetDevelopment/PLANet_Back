package com.planet.develop.Repository;

import com.planet.develop.Entity.Income;
import com.planet.develop.Login.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IncomeRepository {
    private final EntityManager em;

    public void save(Income income) {
        em.persist(income);
    }

    public void delete(Income income){
        em.remove(income);
    }

    public Income findOne(Long id) {
        return em.find(Income.class, id);
    }

    public List<Income> findDay(User user, LocalDate date){
        return em.createQuery("select u from Income u where u.user= :user and u.date= :date", Income.class)
                .setParameter("user",user)
                .setParameter("date",date)
                .getResultList();
    }


    public List<Income> findMonth(User user, int month) {

        LocalDate startDate = LocalDate.of(2022,month,1);
        LocalDate endDate = LocalDate.of(2022,month,startDate.lengthOfMonth());

        return em.createQuery("select u from Income u where u.user= :user and :startDate<=u.date and u.date <= :endDate", Income.class)
                .setParameter("user",user)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getResultList();
    }

    /** 한 달 특정 날짜까지 수입 조회 */
    public List<Income> findMonthDay(User user, int month, int day) {

        LocalDate startDate = LocalDate.of(2022,month,1);
        LocalDate endDate = LocalDate.of(2022,month,day);

        return em.createQuery("select u from Income u where u.user= :user and :startDate<=u.date and u.date <= :endDate", Income.class)
                .setParameter("user",user)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getResultList();
    }

    public Long calMonth(User user,int year,int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = LocalDate.of(year,month,startDate.lengthOfMonth());

        Long sum = em.createQuery("select sum(u.in_cost) from Income u where u.user= :user and :startDate<=u.date and u.date <= :endDate", Long.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        if (sum!=null)
            return sum;
        else
            return 0L;
    }

}