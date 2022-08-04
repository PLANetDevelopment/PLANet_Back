package com.planet.develop.Controller;

import com.planet.develop.DTO.CouponListDto;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class CouponController {

    private final CouponService couponService;

    /** 쿠폰 리스트 조회 페이지 */
    @GetMapping("/coupon")
    public CouponListDto findCoupon(@RequestHeader(JwtProperties.USER_ID) String userId) throws IllegalAccessException {
        couponService.remainingDaysUpdate(userId); // 쿠폰 조회 전 남은 날짜 먼저 업데이트
        CouponListDto couponList = couponService.getCouponList(userId);
        return couponList;
    }

    /** 쿠폰 등록 페이지 */
    @PostMapping("/coupon/register/{cno}")
    public String couponRegister(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("cno") String cno) {
        couponService.couponRegister(userId, cno);
        return cno;
    }

    /** 쿠폰 사용 */
    @PostMapping("/coupon/use/{cno}")
    public String useCoupon(@PathVariable("cno") String cno) throws IllegalAccessException {
        couponService.useCoupon(cno);
        return "쿠폰 사용 완료";
    }

}
