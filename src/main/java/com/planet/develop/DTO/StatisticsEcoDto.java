package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsEcoDto {
    Long totalMonthExpenditure; // 한 달 지출 총액
    Long ecoCost; // 친환경 지출 총액
    Long notEcoCost; // 반환경 지출 총액
    boolean ecoMore = false; // 친환경 지출을 '덜' 함
    boolean notEcoMore = false; // 반환경 지출을 '덜' 함
    List<StatisticsDayDetailDto> detailDtoList; // 지출 내역

    public StatisticsEcoDto(Long totalMonthExpenditure, Long ecoCost, Long notEcoCost) {
        this.totalMonthExpenditure = totalMonthExpenditure;
        this.ecoCost = ecoCost;
        this.notEcoCost = notEcoCost;
    }
}
