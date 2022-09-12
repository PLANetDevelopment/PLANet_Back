package com.planet.develop.Service;

import com.planet.develop.DTO.CalendarDto.CalendarDto;
import com.planet.develop.DTO.StatisticsDto.*;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.TIE;
import com.planet.develop.Enum.money_Type;

import java.util.List;

public interface StatisticsDetailService {
    StatisticsDto calculDif(String id, int year, int month, int currDay, int lastDay);
    CalendarDto totalMonthDay(String id, int year, int month, int day);
    List<StatisticsDayDetailDto> findDayDetail(String id, int year, int month, TIE tie);
    StatisticsDto merge(String id, int year, int month, int currDay, int lastDay, TIE tie);
    StatisticsDto functionByMonth(String id, int year, int month, TIE tie);
    StatisticsEcoDto calculEcoDif(Long totalEx, String id, int year, int month, int currDay, int lastDay);
    StatisticsEcoDto mergeEco(Long totalEx, String id, int year, int month, int currDay, int lastDay);
    StatisticsEcoDto functionEcoByMonth(String id, int year, int month);
    StatisticsEcoCategoryDto findMonthExTypeDetail(String id, money_Type exType, EcoEnum eco, int year, int month);
    List<StatisticsCategoryCount> countEcoTypeExpenditure(String id, EcoEnum eco, int year, int month);
}
