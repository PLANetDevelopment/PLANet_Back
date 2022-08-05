package com.planet.develop.Service;

import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final StatisticsService statisticsService;
    private final StatisticsRepository statisticsRepository;
    public double getPercentage(User user, int year, int month){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year,month,startDate.lengthOfMonth());
        Long ecoCount= statisticsRepository.getNowEcoCount(user, startDate, endDate, EcoEnum.G);
        Long noneEcoCount= statisticsRepository.getNowEcoCount(user, startDate, endDate, EcoEnum.R);

        System.out.println("친환경 태그 개수: " + ecoCount);
        System.out.println("반환경 태그 개수: " + noneEcoCount);

        double percentage = statisticsService.getPercentage(ecoCount, noneEcoCount);

        System.out.println("퍼센트: " + percentage);

        return percentage;
    }
}