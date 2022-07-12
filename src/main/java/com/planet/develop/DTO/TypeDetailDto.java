package com.planet.develop.DTO;

import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeDetailDto {
    boolean isIncome = true;
    money_Type type;
    money_Way way;
    Long id;
    Long cost;
    String memo;
    List<EcoDto> ecoList;

    public void saveIncomeType(money_Type type, money_Way way, Long cost, String memo, Long id) {
        this.type = type;
        this.way = way;
        this.cost = cost;
        this.memo = memo;
        this.id = id;
    }

    public void saveExpenditureType(money_Type type, money_Way way, Long id, Long cost,
                                    String memo, List<EcoDto> ecoList) {
        this.type = type;
        this.way = way;
        this.id = id;
        this.cost = cost;
        this.memo = memo;
        this.ecoList = ecoList;
    }
}
