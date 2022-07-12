package com.planet.develop.DTO;

import lombok.Data;

@Data
public class IncomeResponseDto {
    Long incomeId;

    public IncomeResponseDto(Long incomeId){
        this.incomeId=incomeId;
    }
}