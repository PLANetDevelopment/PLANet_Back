package com.planet.develop.Service;

import com.planet.develop.DTO.QnaDto.QnaDto;
import com.planet.develop.Entity.Qna;

public interface QnaService {

    void save(QnaDto qnaDto);

    void delete(Long id);

    default Qna dtoToEntity(QnaDto dto) {
        Qna entity = Qna.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isAnswer(dto.getIsAnswer())
                .answer(dto.getAnswer())
                .build();
        return entity;
    }

}
