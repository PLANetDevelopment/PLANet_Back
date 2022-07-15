package com.planet.develop.Entity;

import com.planet.develop.DTO.CouponDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Coupon {
    @Id
    private String cno;
    private String coupon; // 쿠폰명
    @Column(nullable = false)
    private int remainingDays; // 남은 날짜
    private int discount; // 할인율
    @Column(nullable = false)
    private LocalDate endDate; // 종료 날짜
    private boolean availability; // 사용 가능 여부
    private boolean expiration; // 만료 여부

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "cno")
    private CouponDetail couponDetail; // 쿠폰 상세 테이블과 일대일 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 테이블과 일대다 매핑

    public CouponDto entityToDto() {
        CouponDto couponDto = new CouponDto(
                cno, coupon, remainingDays, discount, availability, expiration);
        return couponDto;
    }

    public void updateAvailability(int remainingDays, boolean availability) {
        this.remainingDays = remainingDays;
        this.availability = availability;
    }

    public void updateExpiration(int remainingDays, boolean expiration) {
        this.remainingDays = remainingDays;
        this.expiration = expiration;
    }

    public void update(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    /** 생성자 */
    public Coupon(String cno, String coupon, int discount, LocalDate endDate, boolean availability, boolean expiration) {
        this.cno = cno;
        this.coupon = coupon;
        this.discount = discount;
        this.endDate = endDate;
        this.availability = availability;
        this.expiration = expiration;
    }

    /** CouponStorage -> Coupon */
    public Coupon couponStorageToCoupon(CouponStorage storage) {
        return new Coupon(storage.getCno(), storage.getCoupon(), storage.getDiscount(),
                storage.getEndDate(), true, false);
    }
}
