package com.planet.develop.Service;

import com.planet.develop.DTO.CouponDetailDto;
import com.planet.develop.DTO.CouponListDto;

public interface CouponService {
    /** 사용자 쿠폰 리스트 조회 */
    CouponListDto getCouponList(String id);

    /** 사용자 쿠폰 상세 조회 */
    CouponDetailDto getCouponDetail(String cno);

    /** 쿠폰 등록 */
    void couponRegister(String id, String cno);

    /** 쿠폰 사용 */
    void useCoupon(String cno) throws IllegalAccessException;

    /** 쿠폰 남은 날짜 계산 */
    void remainingDaysUpdate(String id) throws IllegalAccessException;
}
