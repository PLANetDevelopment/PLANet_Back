package com.planet.develop.Service;

import com.planet.develop.DTO.*;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.TIE;
import com.planet.develop.Repository.UserRepository;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StatisticsDetailServiceImpl implements StatisticsDetailService {

    private final CalendarService calendarService;
    private final IncomeService incomeService;
    private final ExpenditureDetailService expenditureDetailService;
    private final EcoService ecoService;
    private final UserRepository userRepository;

    /** 이번 달 지출/수입 총액 계산 + 지날 달 대비 현재 달의 수입/지출 차액 계산 */
    @Override
    public StatisticsDto calculDif(String id, int year, int month, int currDay, int lastDay) {
        CalendarDto thisMonth = totalMonthDay(id, year, month, currDay); // 이번 달
        CalendarDto lastMonth = totalMonthDay(id, year, month-1, lastDay); // 지난 달
        
        Long inDif = thisMonth.getTotalMonthIncome() - lastMonth.getTotalMonthIncome(); // 수입 차액
        Long exDif = thisMonth.getTotalMonthExpenditure() - lastMonth.getTotalMonthExpenditure(); // 지출 차액
        
        StatisticsDto statisticsDto = new StatisticsDto(
                thisMonth.getTotalMonthIncome(), thisMonth.getTotalMonthExpenditure(), inDif, exDif);
        
        if (inDif >= 0) statisticsDto.setInMore(true); // 이번 달 수입이 더 많다.
        else statisticsDto.setInDif(inDif*(-1)); // 음수가 나올 경우 절대값으로 저장
        if (exDif >= 0) statisticsDto.setExMore(true); // 이번 달 지출이 더 많다.
        else statisticsDto.setExDif(exDif*(-1));

        return statisticsDto;
    }

    // TODO: 수정
    /** 한 달 중 특정 날짜까지의 총 지출/수입 */
    @Override
    public CalendarDto totalMonthDay(String id, int year, int month, int day) {
        Long totalMonthIncome = incomeService.totalMonthDay(id, month, day); // 총 수입
        User user = userRepository.findById(id).get();
        Long totalMonthExpenditure = expenditureDetailService.totalMonthDay(user, year, month, day); // 총 지출
        return new CalendarDto(totalMonthIncome,totalMonthExpenditure);
    }

    /** 한 달 일별 수입/지출 상세 조회 */
    @Override
    public List<StatisticsDayDetailDto> findDayDetail(String id, int year, int month, TIE tie) {
        int days = LocalDate.of(year,month,month).lengthOfMonth(); // 날짜 세기
        List<StatisticsDayDetailDto> detailDtoList = new ArrayList<>(); // 반환할 리스트
        for(int day=1; day<=days; day++) { // 한 달 상세 내역 조회
            StatisticsDayDetailDto dto = new StatisticsDayDetailDto();
            LocalDate date = LocalDate.of(year, month, day); // 날짜
            List<TypeDetailDto> list = calendarService.inExTypeDetailDto(id, month, day, tie); // 일별 수입/지출 상세 내역 조회
            if (!list.isEmpty()) { // 수입/지출 상세 내역이 존재해야 배열에 담음.
                dto.changeDate(date); dto.setDetailDtoList(list); // 날짜와 상세 내역을 하나의 dto로 담는다.
                detailDtoList.add(dto); // 일별 상세 내역을 리스트에 담는다.
            }
        }
        return detailDtoList;
    }
    
    /** calculDif() 함수에서 얻은 데이터와 findDayDetail() 함수에서 얻은 데이터를 하나의 StatisticsDto로 합친다 */
    @Override
    public StatisticsDto merge(String id, int year, int month, int currDay, int lastDay, TIE tie) {
        StatisticsDto statisticsDto = calculDif(id, year, month, currDay, lastDay);
        List<StatisticsDayDetailDto> list = findDayDetail(id, year, month, tie);
        statisticsDto.setDetailDtoList(list);
        return statisticsDto;
    }

    /** 현재 월이라면 '지난 월 이맘때보다~' & 지난 월이라면 '지난 월보다~' */
    @Override
    public StatisticsDto functionByMonth(String id, int year, int month, TIE tie) {
        int lastDayOfMonth = LocalDate.of(year, month, month).lengthOfMonth(); // 조회 달 마지막 날짜
        int lastDayOfLastMonth = LocalDate.of(year, month-1, month-1).lengthOfMonth(); // 지난 달 마지막 날짜
        int today = LocalDate.now().getDayOfMonth(); // 오늘 날짜

        if (month == (int) LocalDate.now().getMonthValue()) { // 조회하는 월이 현재 월이라면
            if (today > lastDayOfLastMonth) { // 12월 31일에 조회한다면 -> 11월 30일까지 조회해서 비교
                return merge(id, year, month, today, lastDayOfMonth, tie);
            } else return merge(id, year, month, today, today, tie); // 12월 15일에 조회한다면 -> 11월 15일까지 조회해서 비교
        } else return merge(id, year, month, lastDayOfMonth, lastDayOfLastMonth, tie); // 조회하는 월이 현재 월이 아니라면 지난 달 총 수입/지출과 비교해서 계산
    }

    /** 이번 달 지출/수입 총액 계산 + 지날 달 대비 현재 달의 수입/지출 차액 계산 */
    @Override
    public StatisticsEcoDto calculEcoDif(Long totalEx, String id, int year, int month, int currDay, int lastDay) { // currDay: 이번 달 날짜, lastDay: 지난 달 날짜
        EcoCostDto thisMonth = ecoService.getEcoCost(id, year, month, currDay); // 이번 달
        EcoCostDto lastMonth = ecoService.getEcoCost(id, year, month-1, lastDay); // 이번 달

        Long ecoDif = thisMonth.getTotalEcoG() - lastMonth.getTotalEcoG(); // 친환경 차액
        Long nEcoDif = thisMonth.getTotalEcoR() - lastMonth.getTotalEcoR(); // 반환경 차액

        StatisticsEcoDto statisticsEcoDto = new StatisticsEcoDto(totalEx, ecoDif, nEcoDif);
        
        if (ecoDif >= 0) statisticsEcoDto.setEcoMore(true); // 친환경 소비 증가
        else statisticsEcoDto.setEcoCost(ecoDif*(-1)); // 음수가 나올 경우 절대값으로 저장
        if (nEcoDif >= 0) statisticsEcoDto.setNotEcoMore(true); // 반환경 소비 증가
        else statisticsEcoDto.setNotEcoCost(nEcoDif*(-1));

        return statisticsEcoDto;
    }

    /** 지출 페이지) 친반환경 차액과 지출 상세 리스트 합치기 */
    @Override
    public StatisticsEcoDto mergeEco(Long totalEx, String id, int year, int month, int currDay, int lastDay) {
        StatisticsEcoDto statisticsEcoDto = calculEcoDif(totalEx, id, year, month, currDay, lastDay);
        List<StatisticsDayDetailDto> list = findDayDetail(id, year, month, TIE.E);
        statisticsEcoDto.setDetailDtoList(list);
        return statisticsEcoDto;
    }
    
    /** 지출 페이지 */
    @Override
    public StatisticsEcoDto functionEcoByMonth(String id, int year, int month) {
        User user = userRepository.findById(id).get();
        Long totalExpenditure = expenditureDetailService.totalMonth(user, year, month); // 판 달 지출 총액

        int lastDayOfMonth = LocalDate.of(year, month, month).lengthOfMonth(); // 조회 달 마지막 날짜
        int lastDayOfLastMonth = LocalDate.of(year, month-1, month-1).lengthOfMonth(); // 지난 달 마지막 날짜
        int today = LocalDate.now().getDayOfMonth(); // 오늘 날짜

        if (month == (int) LocalDate.now().getMonthValue()) { // 조회하는 월이 현재 월이라면
            if (today > lastDayOfLastMonth) { // 12월 31일에 조회한다면 -> 11월 30일까지 조회해서 비교
                return mergeEco(totalExpenditure, id, year, month, today, lastDayOfLastMonth);
            } else return mergeEco(totalExpenditure, id, year, month, today, today); // 12월 15일에 조회한다면 -> 11월 15일까지 조회해서 비교
        } else return mergeEco(totalExpenditure, id, year, month, lastDayOfMonth, lastDayOfLastMonth); // 조회하는 월이 현재 월이 아니라면 지난 달 총 수입/지출과 비교해서 계산
    }

}
