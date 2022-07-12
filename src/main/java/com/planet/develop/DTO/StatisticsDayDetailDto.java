package com.planet.develop.DTO;

import com.planet.develop.Entity.BaseEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDayDetailDto extends BaseEntity {
    List<TypeDetailDto> detailDtoList; // 통계 부분 지출 상세
}
