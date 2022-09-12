package com.planet.develop.DTO.StatisticsDto;

import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.money_Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsEcoCategoryDto {
    EcoEnum eco; // 친반환경
    money_Type exType; // 지출 유형
    Long totalExpenditure; // 친반환경별 유형별 한 달 총지출
    int countEx; // 지출 개수
    List<StatisticsDateEcoCategoryDto> typeDetailList; // 유형별 지출 리스트
}
