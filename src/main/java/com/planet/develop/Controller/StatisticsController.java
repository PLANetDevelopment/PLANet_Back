package com.planet.develop.Controller;

import com.planet.develop.DTO.EcoDto.GuessEcoCountDto;
import com.planet.develop.DTO.StatisticsDto.StatisticsCategoryCount;
import com.planet.develop.DTO.StatisticsDto.StatisticsDto;
import com.planet.develop.DTO.StatisticsDto.StatisticsEcoCategoryDto;
import com.planet.develop.DTO.StatisticsDto.StatisticsEcoDto;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.TIE;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class StatisticsController {

    private final StatisticsDetailService statisticsDetailService;
    private final UserRepository userRepository;
    private final IncomeService incomeService;
    private final StatisticsService statisticsService;
    private final ExpenditureDetailService expenditureDetailService;
    private final EcoService ecoService;

    /** 친/반환경 태그 통계 */
    @GetMapping("/statistics/{year}/{month}/{day}")
    public Result statistics(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month,@PathVariable("day") int day){
        User user = userRepository.findById(userId).get();
        
        Long incomeTotal = incomeService.totalMonth(user,year, month); // 수입 총합
        Long expenditureTotal = expenditureDetailService.totalMonth(user,year,month); // 지출 총합

        Map<String,Object> ecoBoard = statisticsService.getEcoCountComparedToLast(user,year,month, day);

        GuessEcoCountDto guessEcoCountDto = statisticsService.getGuessMonthEcoCount(user, year, month, day);
        Map<Integer, Long> ecoCount = statisticsService.getGuessMonthEcoCount(user, year, month, day).getMonthEcoMap();
        ecoCount.put(month, guessEcoCountDto.getCurrEcoCount()); // 친환경 개수 예측

        int count_G_category = statisticsService.countCategory(user, year, month, EcoEnum.G); // 친환경 카테고리 개수 - 4
        int count_R_category = statisticsService.countCategory(user, year, month, EcoEnum.R); // 반환경 카테고리 개수 - 4

        List<List<Object[]>> fiveTagCounts = statisticsService.getFiveTagCounts(user, year, month);
        Object ecoDifference = ecoBoard.get("ecoDifference");
        Object noEcoDifference = ecoBoard.get("noEcoDifference");
        Object percentage = ecoBoard.get("percentage");
        Object nowEcoCount=ecoBoard.get("nowEcoCount");
        Object nowNoneEcoCount=ecoBoard.get("noneEcoCount");
        List<Object[]> ecoTagCounts=fiveTagCounts.get(0);
        List<Object[]> noEcoTagCounts=fiveTagCounts.get(1);
        Result result = new Result(user.getNickname(), incomeTotal, expenditureTotal, ecoDifference, noEcoDifference, ecoCount, nowEcoCount, nowNoneEcoCount, percentage, ecoTagCounts, noEcoTagCounts, count_G_category, count_R_category);
        System.out.println("친환경 태그: " + result.more_G_category);
        System.out.println("반환경 태그: " + result.more_R_category);
        return result;
    }

    /** 친환경 태그 통계 */
    @GetMapping("/statistics/ecoCountsDetail/{year}/{month}")
    public statisticsEcoRequestDto statisticsEcoDetail(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month) {
        User user = userRepository.findById(userId).get();
        List<Object[]> tagCategoryList = statisticsService.getTagCategoryList(user, year, month, EcoEnum.G);
        return new statisticsEcoRequestDto(tagCategoryList);
    }

    /** 반환경 태그 통계 */
    @GetMapping("/statistics/noEcoCountsDetail/{year}/{month}")
    public statisticsEcoRequestDto statisticsNoEcoDetail(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year,@PathVariable("month") int month) {
        User user = userRepository.findById(userId).get();
        List<Object[]> tagCategoryList = statisticsService.getTagCategoryList(user, year, month, EcoEnum.R);
        return new statisticsEcoRequestDto(tagCategoryList);
    }

    /** 지난 달 대비 수입/지출 차액 + 한 달 일별 상세 내역 페이지 */
    @GetMapping("/statistics/total/{year}/{month}")
    public StatisticsDto findTotalStatistics(@RequestHeader(JwtProperties.USER_ID) String userId,  @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(userId, year, month, TIE.T);
    }

    /** 비고) 수입 내역 필터링 */
    @GetMapping("/statistics/total/income/{year}/{month}")
    public StatisticsDto filteringIncome(@RequestHeader(JwtProperties.USER_ID) String userId,  @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(userId, year, month, TIE.I);
    }

    /** 비고) 지출 내역 페이지 */
    @GetMapping("/statistics/total/expenditure/{year}/{month}")
    public StatisticsDto filteringExpenditure(@RequestHeader(JwtProperties.USER_ID) String userId,  @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(userId, year, month, TIE.E);
    }

    /** 수입 페이지 */
    @GetMapping("/statistics/income/{year}/{month}")
    public StatisticsDto findIncomeStatistics(@RequestHeader(JwtProperties.USER_ID) String userId,  @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(userId, year, month, TIE.I);
    }

    /** 지출 페이지 */
    @GetMapping("/statistics/expenditure/{year}/{month}")
    public StatisticsEcoDto findExpenditureStatistics(@RequestHeader(JwtProperties.USER_ID) String userId,  @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionEcoByMonth(userId, year, month);
    }

    /**
     * 테스트용 코드 ==========================================================================================
     */

//    /** 지출 친환경 카테고리에서 '더보기' 또는 '>' 클릭 시 */
//    @GetMapping("/statistics/expenditure/{year}/{month}/category/ecoG")
//    public List<StatisticsCategoryCount> findCategoryEcoG(@PathVariable("year") int year, @PathVariable("month") int month) {
//        String userId = "topjoy22@naver.com";
//        return statisticsDetailService.countEcoTypeExpenditure(userId, EcoEnum.G, year, month);
//    }
//
//    /** 지출 반환경 카테고리에서 '더보기' 또는 '>' 클릭 시 */
//    @GetMapping("/statistics/expenditure/{year}/{month}/category/ecoR")
//    public List<StatisticsCategoryCount> findCategoryEcoR(@PathVariable("year") int year, @PathVariable("month") int month) {
//        String userId = "topjoy22@naver.com";
//        return statisticsDetailService.countEcoTypeExpenditure(userId, EcoEnum.R, year, month);
//    }
//
//    /** 친환경 유형별 지출 상세 */
//    @GetMapping("/statistics/expenditure/{year}/{month}/{category}/ecoG")
//    public StatisticsEcoCategoryDto findCategoryEcoGDetail(@PathVariable("category") String type, @PathVariable("year") int year, @PathVariable("month") int month) {
//        String userId = "topjoy22@naver.com";
//        money_Type exType = getMoney_type(type);
//        return statisticsDetailService.findMonthExTypeDetail(userId, exType, EcoEnum.G, year, month);
//    }
//
//    /** 반환경 유형별 지출 상세 */
//    @GetMapping("/statistics/expenditure/{year}/{month}/{category}/ecoR")
//    public StatisticsEcoCategoryDto findCategoryEcoRDetail(@PathVariable("category") String type, @PathVariable("year") int year, @PathVariable("month") int month) {
//        String userId = "topjoy22@naver.com";
//        money_Type exType = getMoney_type(type);
//        return statisticsDetailService.findMonthExTypeDetail(userId, exType, EcoEnum.R, year, month);
//    }

    /**
     * 배포용 코드 ======================================================================================
     * */

    /** 지출 친환경 카테고리에서 '더보기' 또는 '>' 클릭 시 */
    @GetMapping("/statistics/expenditure/{year}/{month}/category/ecoG")
    public List<StatisticsCategoryCount> findCategoryEcoG(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month) {
        return statisticsDetailService.countEcoTypeExpenditure(userId, EcoEnum.G, year, month);
    }

    /** 지출 반환경 카테고리에서 '더보기' 또는 '>' 클릭 시 */
    @GetMapping("/statistics/expenditure/{year}/{month}/category/ecoR")
    public List<StatisticsCategoryCount> findCategoryEcoN(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month) {
        return statisticsDetailService.countEcoTypeExpenditure(userId, EcoEnum.R, year, month);
    }

    /** 친환경 유형별 지출 상세 */
    @GetMapping("/statistics/expenditure/{year}/{month}/{category}/ecoG")
    public StatisticsEcoCategoryDto findCategoryEcoGDetail(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("category") String type, @PathVariable("year") int year, @PathVariable("month") int month) {
        money_Type exType = getMoney_type(type);
        return statisticsDetailService.findMonthExTypeDetail(userId, exType, EcoEnum.G, year, month);
    }

    /** 반환경 유형별 지출 상세 */
    @GetMapping("/statistics/expenditure/{year}/{month}/{category}/ecoR")
    public StatisticsEcoCategoryDto findCategoryEcoRDetail(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("category") String type, @PathVariable("year") int year, @PathVariable("month") int month) {
        money_Type exType = getMoney_type(type);
        return statisticsDetailService.findMonthExTypeDetail(userId, exType, EcoEnum.R, year, month);
    }

    /** String -> money_Type 예) 식비 -> food */
    private money_Type getMoney_type(String type) {
        money_Type exType = null;
        switch(type) {
            case "급여":
                exType = money_Type.salary;
                break;
            case "용돈":
                exType = money_Type.allowance;
                break;
            case "식비":
                exType = money_Type.food;
                break;
            case "기타":
                exType = money_Type.etc;
                break;
            case "교통":
                exType = money_Type.traffic;
                break;
            case "문환생활":
                exType = money_Type.culture;
                break;
            case "생필품":
                exType = money_Type.daily_necessity;
                break;
            case "마트":
                exType = money_Type.market;
                break;
            case "교육":
                exType = money_Type.study;
                break;
            case "통신":
                exType = money_Type.communication;
                break;
            case "의료/건강":
                exType = money_Type.medical_treatment;
                break;
            case "경조사/회비":
                exType = money_Type.congratulations;
                break;
            case "가전":
                exType = money_Type.home_appliances;
                break;
            case "공과금":
                exType = money_Type.dues;
                break;
            default:
                type = "기타";
                break;
        } return exType;
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T userName;
        private T incomeTotal;
        private T expenditureTotal;
        private T ecoDifference;
        private T noEcoDifference;
        private T ecoCount;
        private T nowEcoCount;
        private T nowNoneEcoCount;
        private T percentage;
        private T ecoTagCounts;
        private T noEcoTagCounts;
        private T more_G_category;
        private T more_R_category;
    }

    @Data
    @AllArgsConstructor
    static class statisticsEcoRequestDto<T>{
        private T tagList;
    }

}