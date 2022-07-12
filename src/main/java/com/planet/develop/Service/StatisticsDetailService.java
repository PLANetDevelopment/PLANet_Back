package com.planet.develop.Service;

import com.planet.develop.DTO.*;
import com.planet.develop.Enum.TIE;

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
}
