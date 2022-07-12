package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDetailDto {
    String cno;
    String coupon; // 쿠폰 이름
    int discount; // 할인율
    LocalDate startDate; // 시작 날짜
    LocalDate endDate; // 종료 날짜
    String usageInfo; // 사용 정보
    String couponInfo; // 쿠폰 설명
    String detailInfo; // 상세 정보

    public CouponDetailDto(Object cno, Object coupon, Object discount, Object startDate,
                           Object endDate, Object usageInfo, Object couponInfo, Object detailInfo) {
        this.cno = (String) cno;
        this.coupon = (String) coupon;
        this.discount = (int) discount;
        this.startDate = (LocalDate) startDate;
        this.endDate = (LocalDate) endDate;
        this.usageInfo = (String) usageInfo;
        this.couponInfo = (String) couponInfo;
        this.detailInfo = (String) detailInfo;
    }
}
