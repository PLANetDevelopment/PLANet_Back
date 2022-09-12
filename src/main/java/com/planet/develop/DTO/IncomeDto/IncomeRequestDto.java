package com.planet.develop.DTO.IncomeDto;

import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeRequestDto {
    private Long in_cost;
    private money_Way in_way;
    private money_Type in_type;
    private String memo;
    private LocalDate date;
    private String userId;
}