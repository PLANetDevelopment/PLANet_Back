package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    Long totalMonthIncome;
    Long totalMonthExpenditure;
    boolean inMore = false; // '더': true, '덜': false
    boolean exMore = false;
    Long inDif; // 수입 차액
    Long exDif; // 지출 차액
    List<StatisticsDayDetailDto> detailDtoList; // 지출&수입 내역

    public StatisticsDto(Long totalMonthIncome, Long totalMonthExpenditure, Long inDif, Long exDif) {
        this.totalMonthIncome = totalMonthIncome;
        this.totalMonthExpenditure = totalMonthExpenditure;
        this.inDif = inDif;
        this.exDif = exDif;
    }
}
