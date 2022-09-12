package com.planet.develop.DTO.QnaDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaRequestDto {
    String title;
    String content;
    Boolean isAnswer;
    String answer;
}
