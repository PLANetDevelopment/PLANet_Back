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
public class CouponDetail {
    @Id
    private String cno;
    private LocalDate startDate; // 시작 날짜
    private String usageInfo; // 사용 정보
    private String couponInfo; // 쿠폰 정보
    private String detailInfo; // 상세 정보

    /** 생성자 */
    public CouponDetail(String cno, String usageInfo, String couponInfo, String detailInfo) {
        this.cno = cno;
        this.usageInfo = usageInfo;
        this.couponInfo = couponInfo;
        this.detailInfo = detailInfo;
    }

    /** CouponStorage -> CouponDetail */
    public CouponDetail couponStorageToCouponDetail(CouponStorage storage) {
        return new CouponDetail(storage.getCno(), storage.getUsageInfo(), storage.getCouponInfo(), storage.getDetailInfo());
    }
}
