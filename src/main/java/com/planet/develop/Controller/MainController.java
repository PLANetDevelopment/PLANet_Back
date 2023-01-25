package com.planet.develop.Controller;

import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Service.ExpenditureDetailService;
import com.planet.develop.Service.IncomeService;
import com.planet.develop.Service.MainService;
import com.planet.develop.Service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class MainController {
    private final UserRepository userRepository;
    private final IncomeService incomeService;
    private final ExpenditureDetailService expenditureDetailService;
    private final MainService mainService;
    private final StatisticsService statisticsService;

    @GetMapping("/main/{year}/{month}")
    public mainResponseDto main(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month){
        User user = userRepository.findById(userId).get();
        String userName = user.getNickname();
        Long totalMonthIncome = incomeService.totalMonth(user,year,month);
        Long totalMonthExpenditure = expenditureDetailService.totalMonth(user,year, month);
        // 달에 구애를 받지 않는 전체 에코 퍼센티지 (총 지출)
        Long countEcoG = mainService.countTotalEcoNum(user, EcoEnum.G); // 총 지출 중 에코 개수
        Long countEcoR = mainService.countTotalEcoNum(user, EcoEnum.R); // 총 지출 중 반에코 개수
        double totalEcoPercentage = statisticsService.getPercentage(countEcoG, countEcoR);
        // 매달 에코 퍼센티지 (달마다)
        double ecoPercentage = mainService.getPercentage(user, year, month);
        return new mainResponseDto(userName,totalMonthIncome,totalMonthExpenditure,ecoPercentage,100-ecoPercentage,totalEcoPercentage,100-totalEcoPercentage);
    }

    @PostMapping("/main/update/{userName}")
    public void mainNameUpdate(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("userName") String userName) {
        userRepository.updateName(userId, userName);
    }

    @Data
    @AllArgsConstructor
    static class mainResponseDto<T>{
        private T userName;
        private T totalIncomeMonth;
        private T totalExpenditureMonth;
        private T ecoPercentage;
        private T noEcoPercentage;
        private T totalEcoPercentage;
        private T totalNEcoPercentage;
    }

}