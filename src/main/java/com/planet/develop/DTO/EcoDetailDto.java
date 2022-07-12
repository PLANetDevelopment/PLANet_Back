package com.planet.develop.DTO;

import com.planet.develop.Enum.EcoEnum;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcoDetailDto {
    Long eno;
    LocalDate date;
    Long cost;
    Long ex_eno;
    EcoEnum eco;

    public EcoDetailDto(Object eno, Object date, Object cost, Object ex_eno, Object eco) {
        this.eno = (Long) eno;
        this.date = (LocalDate) date;
        this.cost = (Long) cost;
        this.ex_eno = (Long) ex_eno;
        this.eco = (EcoEnum) eco;
    }
}
