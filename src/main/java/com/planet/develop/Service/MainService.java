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
        Long ecoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate, EcoEnum.G);
        Long noneEcoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,EcoEnum.R);
        double percentage = statisticsService.getPercentage(ecoCount, noneEcoCount);
        System.out.println("ecoCount = " + ecoCount);
        System.out.println("noneEcoCount = " + noneEcoCount);
        return percentage;
    }
}