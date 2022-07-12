package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDto {
    int sumOfEcoCount;
    int sumOfNoneEcoCount;
    Long totalMonthIncome;
    Long totalMonthExpenditure;
    List<CalendarDayDto> calendarDayDtos;

    public CalendarDto(Long totalMonthIncome, Long totalMonthExpenditure) {
        this.totalMonthIncome = totalMonthIncome;
        this.totalMonthExpenditure = totalMonthExpenditure;
    }
}
