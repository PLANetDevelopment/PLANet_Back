package com.planet.develop.DTO;

import com.planet.develop.Enum.EcoDetail;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.money_Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayExTypeDetailDto {
    money_Type exType;
    Long cost;
    String memo;
    EcoEnum eco;
    EcoDetail eco_detail;
}
