package com.planet.develop.Repository;

import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Login.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final EntityManager em;

    public Long getMonthEcoCount(User user, EcoEnum eco, int year, int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = LocalDate.of(year,month,startDate.lengthOfMonth());

        return em.createQuery("select count(*) from Eco ec " +
                "left join ExpenditureDetail ed on ec.expenditure.eno = ed.eno " +
                "left join Expenditure e on e.eno = ed.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate", Long.class)
                .setParameter("user",user)
                .setParameter("eco",eco)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getSingleResult();
    }

    /** 현재 달의 (구체적인 시작점)친환경 태그 개수 구하기*/
    public Long getNowEcoCount(User user,LocalDate startDate,LocalDate endDate,EcoEnum eco) {
        return em.createQuery("select count(*) from Eco ec " +
                "left join ExpenditureDetail ed on ec.expenditure.eno = ed.eno " +
                "left join Expenditure e on e.eno = ed.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate", Long.class)
                .setParameter("user", user)
                .setParameter("eco", eco)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
    }

    /** 현재 달이 아닌 친환경 태그 개수 구하기*/
    public Long getLastEcoCount(User user, LocalDate last,LocalDate startDate,EcoEnum eco) {
        return em.createQuery("select count(*) from Eco ec " +
                "left join ExpenditureDetail ed on ec.expenditure.eno = ed.eno " +
                "left join Expenditure e on e.eno = ed.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate", Long.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", last)
                .setParameter("eco",eco)
                .getSingleResult();
    }

    /** 카테고리/친(반)환경 태그 상위 4개 개수 구하기*/
    public List<Object[]> getCategoryFiveTagCount(User user, LocalDate startDate, LocalDate endDate, EcoEnum eco){

        List<Object[]> resultList = em.createQuery("select ed.exType,count(*) from Eco ec " +
                "left join ExpenditureDetail ed on ec.expenditure.eno = ed.eno " +
                "left join Expenditure e on e.eno = ed.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate " +
                "group by ed.exType " +
                "order by count(*) DESC ", Object[].class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("eco", eco)
                .setMaxResults(4)
                .getResultList();
        return resultList;
    }

    /** 카테고리/친(반)환경 태그별 태그 개수 구하기*/
    public Long getCategoryTagCount(User user, LocalDate startDate, LocalDate endDate, money_Type type,EcoEnum eco){
        return em.createQuery("select count(*) from Expenditure e " +
                "left join ExpenditureDetail ed on e.eno = ed.eno " +
                "left join Eco ec on e.eno = ec.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate and ed.exType = :type", Long.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("eco",eco)
                .setParameter("type",type)
                .getSingleResult();
    }

    /** 카테고리/친(반)환경 태그별 태그 리스트 구하기*/
    public List<Object[]> getCategoryList(User user, LocalDate startDate, LocalDate endDate, EcoEnum eco){

        List<Object[]> resultList = em.createQuery("select ed.exType,count(*) from Eco ec " +
                "left join ExpenditureDetail ed on ec.expenditure.eno = ed.eno " +
                "left join Expenditure e on e.eno = ed.eno " +
                "where e.user = :user and ec.eco = :eco and :startDate<=e.date and e.date <= :endDate " +
                "group by ed.exType " +
                "order by count(*) DESC ", Object[].class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("eco", eco)
                .getResultList();
        return resultList;
    }
}