package com.planet.develop.DTO.IncomeDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomeDto {
    private Long totalIncomeMonth;
    private List<Long> incomeDays;
}