package com.planet.develop.DTO;

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
