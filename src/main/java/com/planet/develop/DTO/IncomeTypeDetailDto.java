package com.planet.develop.DTO;

import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomeTypeDetailDto {
    private Long in_cost;
    private money_Way in_way;
    private money_Type in_type;
    private String memo;
}