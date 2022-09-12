package com.planet.develop.DTO.QnaDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaDto {
    Long qno;
    String title;
    String content;
    Boolean isAnswer;
    String answer;
    String userId;
}
