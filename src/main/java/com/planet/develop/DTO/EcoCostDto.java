package com.planet.develop.DTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcoCostDto {
    Long totalEcoG;
    Long totalEcoR;
}