package com.planet.develop.Repository;

import com.planet.develop.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    /** 쿠폰 개수 조회 */
    @Query("select count(c) from Coupon c where c.user.userId = :id and c.availability = true")
    int getCouponCount(@Param("id") String id);

    /** 쿠폰 상세 조회 */
    @Query("select c.cno, c.coupon, c.discount, cd.startDate, c.endDate, cd.usageInfo, cd.couponInfo, cd.detailInfo " +
            "from Coupon c " +
            "left join CouponDetail cd on c.cno = cd.cno " +
            "where c.cno = :cno")
    List<Object[]> getCouponDetail(@Param("cno") String cno);

    /** 쿠폰 조회 */
    @Query("select c.cno, c.coupon, c.remainingDays, c.discount, c.availability, c.expiration, cd.startDate, c.endDate, cd.usageInfo, cd.couponInfo, cd.detailInfo from Coupon c left join CouponDetail cd on c.cno = cd.cno where c.user.userId = :id")
    List<Object[]> getCouponList(@Param("id") String id);

    /** 쿠폰 종료 날짜와 남은 날짜 조회 */
    @Query("select c.cno, c.remainingDays, c.endDate from Coupon c " +
            "where c.user.userId = :id and c.availability = true and c.expiration = false")
    List<Object[]> getCouponDate(@Param("id") String id);

}
