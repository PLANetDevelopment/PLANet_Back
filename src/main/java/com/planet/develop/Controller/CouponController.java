package com.planet.develop.Controller;

import com.planet.develop.DTO.CouponListDto;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "localhost:3000", allowedHeaders = {"POST", "GET", "PATCH"})
//@CrossOrigin(origins = "https://main.d2f9fwhj50mv28.amplifyapp.com")
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

    /** 쿠폰 리스트 조회 페이지 - 테스트용 */
//    @GetMapping("/{id}/coupon")
//    public CouponListDto findCoupon(@PathVariable("id") String id) throws IllegalAccessException {
//        User user = userRepository.findById(id).get();
//        couponService.remainingDaysUpdate(userId); // 쿠폰 조회 전 남은 날짜 먼저 업데이트
//        CouponListDto couponList = couponService.getCouponList(userId);
//        return couponList;
//    }

    /** 쿠폰 등록 페이지 */
    @PostMapping("/coupon/register")
    public String couponRegister(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestParam("cno") String cno) {
        couponService.couponRegister(userId, cno);
        return cno;
    }

    /** 쿠폰 등록 페이지 - 테스트용 */
//    @PostMapping("/{id}/coupon/register")
//    public String couponRegister(@PathVariable("id") String id, @RequestParam("cno") String cno) {
//        User user = userRepository.findById(id).get();
//        couponService.couponRegister(userId, cno);
//        return cno;
//    }

    /** 쿠폰 사용 */
    @PostMapping("/coupon/use")
    public String useCoupon(@RequestParam("cno") String cno) throws IllegalAccessException {
        couponService.useCoupon(cno);
        return "쿠폰 사용 완료";
    }

}
