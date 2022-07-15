package com.planet.develop.Service;

import com.planet.develop.DTO.CouponDetailDto;
import com.planet.develop.DTO.CouponDto;
import com.planet.develop.DTO.CouponListDto;
import com.planet.develop.Entity.*;
import com.planet.develop.Login.Model.KakaoUser;
import com.planet.develop.Login.Repository.KakaoUserRepository;
import com.planet.develop.Repository.CouponDetailRepository;
import com.planet.develop.Repository.CouponRepository;
import com.planet.develop.Repository.CouponStorageRepository;
import com.planet.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponDetailRepository detailRepository;
    private final CouponStorageRepository storageRepository;
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;

    /** 사용자 쿠폰 리스트 조회 */
    @Override
    public CouponListDto getCouponList(String id) {
        List<Object[]> couponList = couponRepository.getCouponList(id);
        List<CouponDto> couponDtoList = new ArrayList<>();
        for (Object[] obj : couponList) {
            CouponDto couponDto = new CouponDto(
                    obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], obj[6], obj[7], obj[8], obj[9], obj[10]
                    ); // entity -> dto
            couponDtoList.add(couponDto);
        }
        int couponCount = couponRepository.getCouponCount(id); // 쿠폰 개수
        CouponListDto couponListDto = new CouponListDto(couponCount, couponDtoList);
        return couponListDto;
    }

    /** 사용자 쿠폰 상세 조회 */
    @Override
    public CouponDetailDto getCouponDetail(String cno) {
        Object[] obj = couponRepository.getCouponDetail(cno).get(0);
        CouponDetailDto detailDto = new CouponDetailDto(
                obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], obj[6], obj[7]);
        return detailDto;
    }

    /** 쿠폰 등록 */
    @Override
    public void couponRegister(String id, String cno) {
        // 쿠폰 저장소에서 쿠폰 정보 가져오기
        CouponStorage couponStorage = storageRepository.getCouponFromStorage(cno).get();
        // 쿠폰 상세 테이블에 쿠폰 정보 저장하기
        CouponDetail couponDetail = new CouponDetail().couponStorageToCouponDetail(couponStorage);
        detailRepository.save(couponDetail);
        // 쿠폰 테이블에 쿠폰 정보 저장하기
        Coupon coupon = new Coupon().couponStorageToCoupon(couponStorage); // CouponStorage -> Coupon 변환
        User user = userRepository.findById(id).get();
        CouponDetail detail = detailRepository.findById(cno).get();
        coupon.setUser(user); coupon.setCouponDetail(detail);
        couponRepository.save(coupon);
    }

    /** 쿠폰 사용 */
    @Transactional
    public void useCoupon(String cno) throws IllegalAccessException {
        Coupon coupon = couponRepository.findById(cno).orElseThrow(() ->
                new IllegalArgumentException("해당 쿠폰이 없습니다. cno = " + cno));
        coupon.updateAvailability(0, false); // 쿠폰 사용 불가능 처리
    }

    /** 쿠폰 만료: 쿠폰 종료 날짜 지남 */
    @Transactional
    public void couponExpires(String cno, boolean b) throws IllegalAccessException {
        Coupon coupon = couponRepository.findById(cno).orElseThrow(() ->
                new IllegalArgumentException("해당 쿠폰이 없습니다. cno = " + cno));
        coupon.updateExpiration(0, true); // 쿠폰 만료 처리
    }

    /** 쿠폰 남은 날짜 업데이트 */
    @Transactional
    public void couponUpdate(String cno, int remainingDays) throws IllegalAccessException {
        Coupon coupon = couponRepository.findById(cno).orElseThrow(() ->
                new IllegalArgumentException("해당 쿠폰이 없습니다. cno = " + cno));
        coupon.update(remainingDays); // 쿠폰 테이블 수정
    }

    /** 쿠폰 남은 날짜 계산 */
    @Transactional
    public void remainingDaysUpdate(String id) throws IllegalAccessException {
        List<Object[]> couponList = couponRepository.getCouponDate(id); // 사용 가능한 쿠폰들만 가져옴
        for (Object[] coupon : couponList) {
            LocalDate now = LocalDate.now(); // 현재 날짜
            LocalDate endDate = (LocalDate)coupon[2]; // 종료 날짜
            Calendar cur = new GregorianCalendar(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            Calendar end = new GregorianCalendar(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());

            long diffSec = end.getTimeInMillis() - cur.getTimeInMillis(); // 밀리초로 차이 계산
            if (diffSec <= 0) { // 종료 날짜가 지났다면 쿠폰 사용 만료 처리
                couponExpires((String) coupon[0], true);
            } else {
                long diffDays = diffSec / (24 * 60 * 60 * 1000); // 밀리초 -> 일로 변환
                // db의 남은 날짜와 비교해서 다르면 db 업데이트
                long exRemainingDays = (int) coupon[1];
                if (exRemainingDays != diffDays) {
                    couponUpdate((String) coupon[0], (int) diffDays);
                }
            }
        }
    }

}
