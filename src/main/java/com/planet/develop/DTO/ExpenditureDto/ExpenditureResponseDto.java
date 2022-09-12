package com.planet.develop.DTO.ExpenditureDto;

import lombok.Data;

@Data
public class ExpenditureResponseDto {
    Long expenditureId;
    public ExpenditureResponseDto(Long expenditureId){
        this.expenditureId=expenditureId;
    }
}
