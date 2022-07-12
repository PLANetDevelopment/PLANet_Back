package com.planet.develop.Controller;

import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Login.Model.User;
import com.planet.develop.Login.Repository.UserRepository;
import com.planet.develop.Service.ExpenditureDetailService;
import com.planet.develop.Service.IncomeService;
import com.planet.develop.Service.MainService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://main.d2f9fwhj50mv28.amplifyapp.com")
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class MainController {
    private final UserRepository userRepository;
    private final IncomeService incomeService;
    private final ExpenditureDetailService expenditureDetailService;
    private final MainService mainService;

    @GetMapping("/main/{year}/{month}")
    public mainResponseDto main(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month){
        User user = userRepository.findByKakaoEmail(userId);
        String userName = user.getNickname();
        Long totalMonthIncome = incomeService.totalMonth(user,year,month);
        Long totalMonthExpenditure = expenditureDetailService.totalMonth(user,year, month);
        double ecoPercentage = mainService.getPercentage(user, year, month);
        return new mainResponseDto(userName,totalMonthIncome,totalMonthExpenditure,ecoPercentage,100-ecoPercentage);
    }

    @PostMapping("/main/update")
    public void mainNameUpdate(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestParam String userName){
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
    }

}