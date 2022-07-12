package com.planet.develop.DTO;

import lombok.Data;

@Data
public class ExpenditureResponseDto {
    Long expenditureId;
    public ExpenditureResponseDto(Long expenditureId){
        this.expenditureId=expenditureId;
    }
}
