package com.planet.develop.DTO.StatisticsDto;

import com.planet.develop.Enum.money_Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsCategoryCount implements Comparable<StatisticsCategoryCount> {
    private money_Type exType; // 카테고리
    private int count; // 지출 개수
    private int percent; // 퍼센트

    @Override
    public int compareTo(StatisticsCategoryCount o) {
        return o.count - this.getCount();
    }
}
