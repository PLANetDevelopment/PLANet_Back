package com.planet.develop.Service;

import com.planet.develop.DTO.EcoDto.GuessEcoCountDto;
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
        if (guessEcoCountDto.getCurrEcoCount() == 0) { // 현재 달이 소비의 첫 달이라면
            guessEcoCountDto.setCurrEcoCount(getEcoCount(user, year, month));
        } else {
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
        }

//        guessEcoCountDto.setCurrEcoCount(guessEcoCountDto.getCurrEcoCount() + currMonthEcoCount); // 예상치 = 평균치 + 현재 달 친환경 개수

        return guessEcoCountDto;
    }

    GuessEcoCountDto getEcoAvgConsiderLastYear(User user, int checkIsLastYear, int check5MonthOfData, int year, int month) {
        Long sum = 0L; // 친환경 개수 합
        Map<Integer, Long> monthEcoMap = new HashMap<>(); // 달 : 친환경 개수

        int last6monthAgo = month - check5MonthOfData;

        if (check5MonthOfData == 0) { // 이번 달이 첫 소비라면 지난 달을 계산할 필요없음
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
        } else if (checkIsLastYear > 0) { // 올해 데이터로 충분하고 이번달이 첫 소비가 아니라면
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

        make6monthData(monthEcoMap);
        return new GuessEcoCountDto(monthEcoMap, avg);
    }

    /** 6개월치 데이터 보내기 */
    Map<Integer, Long> make6monthData(Map<Integer, Long> ecoMap) {

        if (ecoMap.size() == 5)
            return ecoMap;

        int month = LocalDate.now().getMonthValue();
        int tempMonth = month;
        List<Integer> monthList = new ArrayList<>();
        for (int i = 1; i < 6; i++) { // 5개월치 달 계산: 만약 현재 3월이라면 -> 2 1 12 11 10
            if ((month - i) <= 0) {
                tempMonth = 12 - (i - month);
            } else
                tempMonth = month - i;
            monthList.add(tempMonth);
        }

        for (int i : monthList) {
            if (!ecoMap.containsKey(i)) { // 해당 월의 데이터가 없다면
                ecoMap.put(i, 0L); // 0을 넣는다.
            }
        }

        System.out.println("6개월치 데이터 테스트");
        System.out.println(ecoMap);
        return ecoMap;
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

        int lastDayOfMonth = LocalDate.of(year, month, month).lengthOfMonth(); // 조회 달 마지막 날짜
        int lastDayOfLastMonth = LocalDate.of(year, month-1, month-1).lengthOfMonth(); // 지난 달 마지막 날짜
        int today = LocalDate.now().getDayOfMonth(); // 오늘 날짜

        if(endDate.getMonthValue()!=month) {// 현재 달이 요청한 달이 아니라면
            ecoCount = getEcoCount(user, year, month); // 친환경 개수
            lastEcoCount = getEcoCount(user, year, month - 1); // 저번 달 친환경 개수
            noneEcoCount = getNoneEcoCount(user, year, month); // 반환경 개수
            lastNoneEcoCount = getNoneEcoCount(user, year, month - 1); // 저번 달 반환경 개수

        } else { // 현재 달이 요청한 달이라면
            ecoCount = getCurrMonthEcoCount(user, year, month, day); // 현재 달 친환경 개수
            noneEcoCount = getCurrMonthNoneEcoCount(user, year, month, day); // 현재 달 반환경 개수
            if (today > lastDayOfLastMonth) { // 12월 31일에 조회한다면 -> 11월 30일까지 조회해서 비교
                lastEcoCount = getCurrMonthEcoCount(user, year, month - 1, lastDayOfLastMonth); // 저번 달 친환경 개수
                lastNoneEcoCount = getCurrMonthNoneEcoCount(user, year, month - 1, lastDayOfLastMonth); // 저번 달 친환경 개수
            } else  {
                lastEcoCount = getCurrMonthEcoCount(user, year, month - 1, today);// 12월 15일에 조회한다면 -> 11월 15일까지 조회해서 비교
                lastNoneEcoCount = getCurrMonthNoneEcoCount(user, year, month - 1, today); // 저번 달 친환경 개수
            }
        }

        LocalDate last=endDate.minusMonths(1);

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

        eco_remain[1] = ecoCount - ecoCnt; // 전체 태그 개수 - 상위 4개 태그 개수
        eco.add(eco_remain);

        eco_remain[1] = noneEcoCount-noEcoCnt;
        noEco.add(eco_remain);

        result.add(eco);
        result.add(noEco);
        return result;
    }

    /** 카테고리 개수 - 4 */
    public int countCategory(User user, int year, int month, EcoEnum eco) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate=LocalDate.of(year, month, startDate.lengthOfMonth());
        int countExType = expenditureRepository.countExType(user, startDate, endDate, eco);
        if (countExType <= 4) countExType = 0;
        else countExType -= 4;
        return countExType;
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