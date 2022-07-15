package com.planet.develop.Repository;

import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    /**
     * Expenditure 테이블과 ExpenditureDetail 테이블 조인
     * */
    @Query("select e, ed from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno where e.eno = :id")
    List<Object[]> tableJoin(@Param("id") Long  id);

    /**
     * 특정 사용자의 하루 지출 리스트 가져오기
     */
    @Query("select e.eno, e.cost, ed.exType, ed.exWay, ed.memo, ec.eco, ec.ecoDetail, ec.userAdd, ec.expenditure.eno " +
            "from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno " +
            "left join Eco ec on e.eno = ec.expenditure.eno " +
            "where e.user = :user and e.date = :date")
    List<Object[]> getDayList(@Param("user") User user, @Param("date") LocalDate date);

    /**
     * 특정 사용자의 하루 지출 금액 가져오기
     */
    @Query("select e.cost from Expenditure e where e.user = :user and e.date = :date")
    List<Object[]> getDayExpenditure(@Param("user") User user, @Param("date") LocalDate date);

    /**
     * 특정 사용자의 하루 친/반환경별 지출 리스트 가져오기
     */
    @Query("select e.user.userId, e.cost, ec.eco from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno " +
            "left join Eco ec on e.eno = ec.expenditure.eno " +
            "where e.user = :user and ec.eco = :eco and e.date = :date")
    List<Object[]> getDayEcoList(@Param("user") User user, @Param("eco") EcoEnum eco, @Param("date") LocalDate date);

    /**
     * 특정 사용자의 하루 지출 유형별 지출 리스트 가져오기
     */
    @Query("select e.user.userId, e.cost, ed.exType, e.date from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno " +
            "where e.user = :user and ed.exType = :exType and e.date = :date")
    List<Object[]> getDayExTypeList(@Param("user") User user, @Param("exType") money_Type exType, @Param("date") LocalDate date);

    /**
     * 특정 사용자의 하루 지출 방법별 지출 리스트 가져오기
     */
    @Query("select e.user.userId, e.cost, ed.exWay, e.date from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno " +
            "where e.user = :user and ed.exWay = :exWay and e.date = :date")
    List<Object[]> getDayExWayList(@Param("user") User user, @Param("exWay") money_Way exWay, @Param("date") LocalDate date);

    /**
     * 특정 사용자의 하루 지출 총합
     */
    @Query("select sum(e.cost) from Expenditure e " +
            "where e.user = :user and :startDate<=e.date and e.date <= :endDate")
    Long calMonth(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
