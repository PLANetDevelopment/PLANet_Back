package com.planet.develop.Repository;

import com.planet.develop.Entity.Eco;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EcoRepository extends JpaRepository<Eco, Long> {

    /** 특정 지출 내역에 해당하는 친/반환경 데이터의 기본키 가져오기 */
    @Query("select e.eno from Eco e " +
            "left join ExpenditureDetail ed on e.expenditure.eno = ed.eno " +
            "where e.expenditure.eno = :eno")
    List<Long> getEcoByEno(@Param("eno") Long eno);

    /** 하루 지출 유형별 지출 리스트 가져오기 */
    @Query("select e.eno, e.date, e.cost, eco.expenditure.eno, eco.eco from Expenditure e " +
            "left join ExpenditureDetail ed on e.eno = ed.eno " +
            "left join Eco eco on eco.expenditure.eno = ed.eno " +
            "where e.user.userId = :user and e.date = :date and eco.eco = :eco")
    List<Object[]> getListByEco(@Param("user") String user, @Param("date") LocalDate date, @Param("eco") EcoEnum eco);

    /** 특정 기간 친환경 태그를 가진 지출 리스트 가져오기 (중복 제거) */
    @Query("select distinct eco.expenditure from Eco eco " +
            "where eco.expenditure.user.userId = :user and eco.eco = :eco " +
            "and :startDate <= eco.expenditure.date and eco.expenditure.date <= :endDate")
    List<Eco> getExpenditureByEco(@Param("user") String user, @Param("eco") EcoEnum eco, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
