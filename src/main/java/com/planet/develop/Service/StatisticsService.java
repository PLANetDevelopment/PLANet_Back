package com.planet.develop.Service;

import com.planet.develop.DTO.GuessEcoCountDto;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Repository.ExpenditureRepository;
import com.planet.develop.Repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ExpenditureRepository expenditureRepository;

    public Long getMonthEcoCount(User user, EcoEnum eco, int year, int month){
        Long monthEcoCount = statisticsRepository.getMonthEcoCount(user, eco,year, month);
        return monthEcoCount;
    }

    /** 이번 달 친환경 개수 예측 */
    public GuessEcoCountDto getGuessMonthEcoCount(User user, int year, int month, int day){
        LocalDate firstExDate = expenditureRepository.getFirstDate(user); // 첫 소비 시작 날짜

        if (firstExDate == null)
            firstExDate = LocalDate.now();

        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        Period period = firstExDate.until(currentDate);
        int check5MonthOfData = period.getYears() * 12 + period.getMonths(); // 현재 달 - 첫 소비 달

        int checkIsLastYear = 0;
        if (check5MonthOfData < 5) { // 데이터가 5개월치가 안 된다면
            checkIsLastYear = month - check5MonthOfData;
        } else { // 데이터가 5개월치가 된다면
            checkIsLastYear = month - 5;
            }

        GuessEcoCountDto guessEcoCountDto = getEcoAvgConsiderLastYear(user, checkIsLastYear, check5MonthOfData, year, month);// 작년 데이터까지 필요한지 확인하고 평균 계산

        Long currMonthEcoCount = getCurrMonthEcoCount(user, year, month, day); // 현재 달 친환경 개수

        // 예상치
        // 만약 현재 달 태그 개수가 평균을 넘겼다면
        if (currMonthEcoCount >= guessEcoCountDto.getCurrEcoCount()) {
            if (currentDate.getMonthValue() <= 10) { // 현재 날짜가 10일 전이라면
                guessEcoCountDto.setCurrEcoCount(guessEcoCountDto.getCurrEcoCount() * 3); // 현재 태그 개수의 3배
            } else if (currentDate.getMonthValue() <= 20) { // 현재 날짜가 10일 이후, 20일 전이라면
                guessEcoCountDto.setCurrEcoCount(guessEcoCountDto.getCurrEcoCount() * 2); // 현재 태그 개수의 2배
            } else {
                guessEcoCountDto.setCurrEcoCount(guessEcoCountDto.getCurrEcoCount() + 3); // 현재 태그 개수 + 3개
            }
        } // 평균을 넘기지 않았다면 평균이 현재 달의 예상치

//        guessEcoCountDto.setCurrEcoCount(guessEcoCountDto.getCurrEcoCount() + currMonthEcoCount); // 예상치 = 평균치 + 현재 달 친환경 개수

        return guessEcoCountDto;
    }

    GuessEcoCountDto getEcoAvgConsiderLastYear(User user, int checkIsLastYear, int check5MonthOfData, int year, int month) {
        Long sum = 0L; // 친환경 개수 합
        Map<Integer, Long> monthEcoMap = new HashMap<>(); // 달 : 친환경 개수

        int last6monthAgo = month - check5MonthOfData;

        if (check5MonthOfData == 0) { // 이번 달이 첫 소비라면 지난 달을 계산할 필요없음
            monthEcoMap.put(month - 1, 0L);
        } else if (checkIsLastYear <= 0) { // 작년 데이터까지 필요하다면
            getEcoCount(user, year, month);
            for (int i = (12 + last6monthAgo); i <= 12; i++) { // 작년 친환경 개수 합산
                Long ecoCount = getEcoCount(user, year - 1, i);
                monthEcoMap.put(i, ecoCount);
                sum += ecoCount;
            }
            for (int i = 1; i <= (month - 1); i++) { // 올해 친환경 개수 합산
                Long ecoCount = getEcoCount(user, year, i);
                monthEcoMap.put(i, ecoCount);
                sum += ecoCount;
            }
        } else if (checkIsLastYear > 0){ // 올해 데이터로 충분하고 이번달이 첫 소비가 아니라면
            for (int i = last6monthAgo; i < month; i++) {
                Long ecoCount = getEcoCount(user, year, i);
                monthEcoMap.put(i, ecoCount);
                sum += ecoCount;
            }
        }

        Long avg = 0L;
        if (check5MonthOfData != 0) { // 첫 소비 달 == 현재 달인 경우 평균 = 0
            avg = sum / check5MonthOfData; // 평균 계산
        }
        return new GuessEcoCountDto(monthEcoMap, avg);
    }

    /**
     * 특정 달의 친환경 개수 구하기 (현재 달 제외: 현재 일까지만 계산해야 하므로)
     */
    Long getEcoCount(User user, int year, int month) {
        Long ecoCount = statisticsRepository.getMonthEcoCount(user, EcoEnum.G, year, month);
        return ecoCount;
    }

    /**
     * 특정 달의 반환경 개수 구하기 (현재 달 제외: 현재 일까지만 계산해야 하므로)
     */
    Long getNoneEcoCount(User user, int year, int month) {
        Long ecoCount = statisticsRepository.getMonthEcoCount(user, EcoEnum.R, year, month);
        return ecoCount;
    }

    /**
     * 현재 달의 친환경 개수 구하기
     */
    Long getCurrMonthEcoCount(User user, int year, int month, int day) {
        LocalDate startDate = LocalDate.of(year, month, 1); // 시작 날짜
        LocalDate currentDate = LocalDate.of(year, month, day); // 현재 날짜
        Long nowEcoCount = statisticsRepository.getNowEcoCount(user, startDate, currentDate, EcoEnum.G);
        return nowEcoCount;
    }

    /**
     * 현재 달의 반환경 개수 구하기
     */
    Long getCurrMonthNoneEcoCount(User user, int year, int month, int day) {
        LocalDate startDate = LocalDate.of(year, month, 1); // 시작 날짜
        LocalDate currentDate = LocalDate.of(year, month, day); // 현재 날짜
        Long nowEcoCount = statisticsRepository.getNowEcoCount(user, startDate, currentDate, EcoEnum.R);
        return nowEcoCount;
    }

    public Map<String,Object> getEcoCountComparedToLast(User user, int year, int month, int day) {
        Map<String, Object> eco = new HashMap<>();

        LocalDate endDate=LocalDate.now();
        Long ecoCount = 0l;
        Long noneEcoCount = 0l;
        Long lastEcoCount = 0l;
        Long lastNoneEcoCount = 0l;

        if(endDate.getMonthValue()!=month) {// 현재 달이 요청한 달이 아니라면
            ecoCount = getEcoCount(user, year, month); // 친환경 개수
            lastEcoCount = getEcoCount(user, year, month - 1); // 저번 달 친환경 개수
            noneEcoCount = getNoneEcoCount(user, year, month); // 반환경 개수
            lastNoneEcoCount = getNoneEcoCount(user, year, month - 1); // 저번 달 반환경 개수

        } else { // 현재 달이 요청한 달이라면
            ecoCount = getCurrMonthEcoCount(user, year, month, day); // 친환경 개수
            lastEcoCount = getEcoCount(user, year, month - 1); // 저번 달 친환경 개수
            noneEcoCount = getCurrMonthNoneEcoCount(user, year, month, day); // 반환경 개수
            lastNoneEcoCount = getEcoCount(user, year, month - 1); // 저번 달 반환경 개수
        }

        LocalDate last=endDate.minusMonths(1);

        System.out.println("현재 달 친환경 태그 개수: " + ecoCount);
        System.out.println("현재 달 반환경 태그 개수: " + noneEcoCount);
        System.out.println("현재 달이 아닌 친환경 태그 개수: " + lastEcoCount);
        System.out.println("현재 달이 아닌 반환경 태그 개수: " + lastNoneEcoCount);

        Long ecoDifference = ecoCount-lastEcoCount;
        Long noEcoDifference = noneEcoCount - lastNoneEcoCount;

        double percentage = getPercentage(ecoCount, noneEcoCount);

        eco.put("ecoDifference",ecoDifference);
        eco.put("noEcoDifference",noEcoDifference);
        eco.put("percentage",percentage);
        eco.put("nowEcoCount",ecoCount);
        eco.put("noneEcoCount",noneEcoCount);

        return eco;
    }

    /** 친/반환경 퍼센테이지 구하는 함수 */
    public double getPercentage(Long ecoCount, Long noneEcoCount) {
        double percentage=0L;
        if (ecoCount !=0 & noneEcoCount !=0){
            percentage = Math.round((double) ecoCount /(double)(noneEcoCount + ecoCount)*100);
        }
        if (ecoCount !=0 & noneEcoCount ==0){
            percentage=100L;
        }
        return percentage;
    }

    /** 상위 4개 + 더보기 태그 보여주기 */
    public  List<List<Object[]>> getFiveTagCounts(User user, int year, int month){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        List<Object[]> categoryFiveTagCount = statisticsRepository.getCategoryFiveTagCount(user, startDate, endDate, EcoEnum.G);
        List<Object[]> categoryFiveNoTagCount = statisticsRepository.getCategoryFiveTagCount(user, startDate, endDate, EcoEnum.R);
        Long ecoCount = statisticsRepository.getNowEcoCount(user, startDate, endDate,EcoEnum.G);
        Long noneEcoCount = statisticsRepository.getNowEcoCount(user, startDate, endDate,EcoEnum.R);

        List<List<Object[]>> result = new ArrayList<>();
        List<Object[]> eco = new ArrayList<>();
        List<Object[]> noEco = new ArrayList<>();
        Long ecoCnt=0L;
        Long noEcoCnt=0L;
        for (Object[] tag : categoryFiveTagCount) {
            eco.add(tag);
            ecoCnt+=(Long)tag[1];
        }
        for (Object[] tag : categoryFiveNoTagCount) {
            noEco.add(tag);
            noEcoCnt+=(Long)tag[1];
        }
        Object eco_remain[]=new Object[2];
        eco_remain[0]="더보기";

        System.out.println("전체 태그 개수: " + ecoCount);
        System.out.println("상위 4개 태그 개수: " + ecoCnt);

        eco_remain[1] = ecoCount - ecoCnt; // 전체 태그 개수 - 상위 4개 태그 개수
        eco.add(eco_remain);
        eco_remain[1] = noneEcoCount-noEcoCnt;
        noEco.add(eco_remain);

        System.out.println("전체 태그 개수: " + noneEcoCount);
        System.out.println("상위 4개 태그 개수: " + noEcoCnt);

        result.add(eco);
        result.add(noEco);
        return result;
    }

    public List<Object[]> getTagCategoryList(User user, int year, int month, EcoEnum ecoEnum){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate=LocalDate.of(year, month, startDate.lengthOfMonth());
        List<Object[]> categoryList= statisticsRepository.getCategoryList(user, startDate, endDate,ecoEnum);
        Long totalCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,ecoEnum);
        List<Object[]> result = new ArrayList<>();
        for (Object[] objects : categoryList) {
            Object[] category = new Object[3];
            category[0]=objects[0];
            Long tmp= (Long)objects[1];
            double cnt = (double) tmp;
            // 퍼센테이지
            category[1] = Math.round(cnt/(double)totalCount*100);
            //개수
            category[2]=objects[1];
            result.add(category);
        }
        return result;
    }

}