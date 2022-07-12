package com.planet.develop.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponStorage {
    @Id
    private String cno;
    private String coupon; // 쿠폰명
    private int discount; // 할인율
    private LocalDate endDate; // 종료 날짜
    private String usageInfo; // 사용 정보
    private String couponInfo; // 쿠폰 정보
    private String detailInfo; // 상세 정보
}
