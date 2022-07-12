package com.planet.develop.Repository;

import com.planet.develop.Entity.CouponStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponStorageRepository extends JpaRepository<CouponStorage, String>  {

    /** 쿠폰 저장소에서 쿠폰 조회 */
    @Query("select cs from CouponStorage cs where cs.cno = :cno")
    Optional<CouponStorage> getCouponFromStorage(@Param("cno") String cno);

}
