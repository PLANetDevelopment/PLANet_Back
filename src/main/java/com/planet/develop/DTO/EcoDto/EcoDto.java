package com.planet.develop.DTO.EcoDto;

import com.planet.develop.Enum.EcoDetail;
import com.planet.develop.Enum.EcoEnum;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcoDto {
    private EcoEnum eco;
    private EcoDetail ecoDetail;
    private String userAdd;

    public EcoDto(EcoEnum eco, EcoDetail ecoDetial) {
        this.eco = eco;
        this.ecoDetail = ecoDetial;
    }
}
