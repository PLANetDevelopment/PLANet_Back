package com.planet.develop.Service;

import com.planet.develop.DTO.QnaDto.QnaDto;
import com.planet.develop.Entity.Qna;
import com.planet.develop.Repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaRepository qnaRepository;

    @Override
    public void save(QnaDto qnaDto) {
        Qna qna = dtoToEntity(qnaDto);
        qnaRepository.save(qna);
    }

    @Override
    public void delete(Long id) {
        qnaRepository.deleteById(id);
    }

}
