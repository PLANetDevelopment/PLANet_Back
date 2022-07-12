package com.planet.develop.DTO;

import com.planet.develop.Enum.EcoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckDupDto {
    Long ex_eno;
    EcoEnum eco;
}
