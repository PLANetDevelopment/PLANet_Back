package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {
    String cno; // 쿠폰 번호
    String coupon; // 쿠폰 이름
    int remainingDays; // 남은 날짜
    int discount; // 할인율
    boolean availability; // 사용 가능 여부
    boolean expiration; // 만료 여부

    LocalDate startDate; // 시작 날짜
    LocalDate endDate; // 종료 날짜
    String usageInfo; // 사용 정보
    String couponInfo; // 쿠폰 설명
    String detailInfo; // 상세 정보

    public CouponDto(Object cno, Object coupon, Object remainingDays, Object discount,
                     Object availability, Object expiration) {
        this.cno = (String) cno;
        this.coupon = (String) coupon;
        this.remainingDays = (int) remainingDays;
        this.discount = (int) discount;
        this.availability = (boolean) availability;
        this.expiration = (boolean) expiration;
    }

    public CouponDto(Object cno, Object coupon, Object remainingDays, Object discount,
                     Object availability, Object expiration, Object starDate, Object endDate,
                     Object usageInfo, Object couponInfo, Object detailInfo) {
        this.cno = (String) cno;
        this.coupon = (String) coupon;
        this.remainingDays = (int) remainingDays;
        this.discount = (int) discount;
        this.availability = (boolean) availability;
        this.expiration = (boolean) expiration;
        this.startDate = (LocalDate) starDate;
        this.endDate = (LocalDate) endDate;
        this.usageInfo = (String) usageInfo;
        this.couponInfo = (String) couponInfo;
        this.detailInfo = (String) detailInfo;
    }
}
