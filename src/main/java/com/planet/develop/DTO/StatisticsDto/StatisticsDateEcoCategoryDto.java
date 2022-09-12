package com.planet.develop.DTO.StatisticsDto;

import com.planet.develop.DTO.ExpenditureDto.ExpenditureTypeDetailDto;
import com.planet.develop.DTO.ExpenditureDto.TypeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDateEcoCategoryDto {
    LocalDate date; // 날짜
    List<TypeDetailDto> detailDtoList; // 친환경별 유형별 날짜별 지출 리스트
}
