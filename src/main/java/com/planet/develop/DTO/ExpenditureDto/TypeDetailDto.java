package com.planet.develop.DTO.ExpenditureDto;

import com.planet.develop.DTO.EcoDto.EcoDto;
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
    boolean isIncome = true; // 수입 or 지출
    money_Type type; // 유형
    money_Way way; // 방법
    Long id; // 아이디
    Long cost; // 비용
    String memo; // 메모
    List<EcoDto> ecoList; // 에코 리스트

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
