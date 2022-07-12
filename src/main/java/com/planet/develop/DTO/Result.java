package com.planet.develop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private T totalMoney;
    private T totalDetails;
    private String content;
}