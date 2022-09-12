package com.planet.develop.DTO.ExpenditureDto;

import com.planet.develop.Enum.EcoDetail;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureTypeDetailDto {
    private Long eno; // 아이디
    private Long cost; // 비용
    private money_Type exType; // 유형
    private money_Way exWay; // 방법
    private String memo; // 메모
    private EcoEnum eco;
    private EcoDetail ecoDetail;
    private String userAdd; // 사용자 추가 태그
    private Long exEno; // 지출 아이디

    public ExpenditureTypeDetailDto(Object eno, Object cost, Object exType, Object exWay,
                                    Object memo, Object eco, Object ecoDetail, Object userAdd, Object exEno) {
        this.eno = (Long) eno;
        this.cost = (Long) cost;
        this.exType = (money_Type) exType;
        this.exWay = (money_Way) exWay;
        this.memo = (String) memo;
        this.eco = (EcoEnum) eco;
        this.ecoDetail = (EcoDetail) ecoDetail;
        this.userAdd = (String) userAdd;
        this.exEno = (Long) exEno;
    }
}
