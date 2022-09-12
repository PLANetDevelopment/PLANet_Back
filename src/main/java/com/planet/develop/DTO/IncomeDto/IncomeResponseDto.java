package com.planet.develop.DTO.IncomeDto;

import lombok.Data;

@Data
public class IncomeResponseDto {
    Long incomeId;

    public IncomeResponseDto(Long incomeId){
        this.incomeId=incomeId;
    }
}