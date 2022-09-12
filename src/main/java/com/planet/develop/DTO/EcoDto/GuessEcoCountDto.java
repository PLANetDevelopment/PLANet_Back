package com.planet.develop.DTO.EcoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuessEcoCountDto {
    private Map<Integer, Long> monthEcoMap; // 5개월치 친환경 개수
    private Long currEcoCount; // 현재 달 친환경 개수
}
