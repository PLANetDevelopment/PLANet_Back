package com.planet.develop.DTO.CouponDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponListDto {
    int couponCount;
    List<CouponDto> couponDtos;
}
