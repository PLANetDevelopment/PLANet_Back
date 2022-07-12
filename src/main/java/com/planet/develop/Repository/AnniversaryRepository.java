package com.planet.develop.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnniversaryRepository {
    private final EntityManager em;

    public List<Object[]> getAnniversaryList(int month){
        LocalDate startDate = LocalDate.of(2022,month,1);
        LocalDate endDate = LocalDate.of(2022,month,startDate.lengthOfMonth());

        return em.createQuery("select a.date,a.content from Anniversary a where :startDate<=a.date and a.date <= :endDate", Object[].class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getResultList();
    }

    public String getAnniversary(int year,int month,int day){
        LocalDate date  = LocalDate.of(year,month,day);
        TypedQuery<String> date1 = em.createQuery("select a.content from Anniversary a where :date=a.date", String.class)
                .setParameter("date", date);

        try {
            return date1.getSingleResult();
        } catch (Exception e) {
            return "";
        }
    }

}