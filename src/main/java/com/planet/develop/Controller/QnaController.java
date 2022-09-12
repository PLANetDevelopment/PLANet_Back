package com.planet.develop.Controller;

import com.planet.develop.DTO.QnaDto.QnaDto;
import com.planet.develop.Entity.Qna;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Repository.QnaRepository;
import com.planet.develop.Service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class QnaController {

    private final QnaRepository qnaRepository;
    private final QnaService qnaService;

    /** 문의 리스트 */
    @GetMapping("/qna")
    public List<QnaDto> qnaList(@RequestHeader(JwtProperties.USER_ID) String userId){
        List<Qna> qnaList = qnaRepository.getQnaList();
        List<QnaDto> qnaDtoList = new ArrayList<>();
        for (Qna qna : qnaList) { // entity -> dto
            QnaDto qnaDto = qna.entityToDto();
            qnaDtoList.add(qnaDto);
        }
        return qnaDtoList;
    }

    /** 문의하기 */
    @PostMapping("/qna/new")
    public void qnaRegister(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestBody QnaDto dto){
        qnaService.save(dto);
    }

    /** 문의 답변하기 */
    @PostMapping("/qna/answer/{id}")
    public void qnaAnswer(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestBody QnaDto dto){
        qnaRepository.saveAnswer(dto.getAnswer(), dto.getQno());
    }

    /** 문의 수정하기 */


    /** 문의 삭제하기 */
    @DeleteMapping("/qna/delete/{id}")
    public void qnaDelete(@PathVariable("id") Long id){
        qnaService.delete(id);
    }

}
