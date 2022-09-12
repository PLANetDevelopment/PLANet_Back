package com.planet.develop.DTO.ExpenditureDto;

import com.planet.develop.DTO.ExpenditureDto.ExpenditureTypeDetailDto;
import com.planet.develop.DTO.IncomeDto.IncomeTypeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayDetailDto {
    List<IncomeTypeDetailDto> in_days;
    List<ExpenditureTypeDetailDto> ex_days;
}
