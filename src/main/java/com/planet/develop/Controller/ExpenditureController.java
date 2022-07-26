package com.planet.develop.Controller;

import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.DTO.ExpenditureResponseDto;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Repository.ExpenditureDetailRepository;
import com.planet.develop.Repository.ExpenditureRepository;
import com.planet.develop.Service.EcoService;
import com.planet.develop.Service.ExpenditureDetailService;
import com.planet.develop.Service.ExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class ExpenditureController {

    private final ExpenditureDetailRepository detailRepository;
    private final ExpenditureRepository expenditureRepository;
    private final ExpenditureDetailService detailService;
    private final ExpenditureService expenditureService;
    private final EcoService ecoService;

    /** 지출 데이터 저장 */
    @PostMapping("/expenditure/new")
    public ExpenditureResponseDto create_expenditure(@RequestHeader(JwtProperties.USER_ID) String userId,
                                                @RequestBody ExpenditureRequestDto reuqest) {
        reuqest.setUserId(userId);
        Long deno = detailService.save(reuqest); // 지출 상세 테이블 저장
        ExpenditureDetail detail = detailRepository.findById(deno).get(); // 지출 테이블과 매핑
        Long eno = expenditureService.save(reuqest, detail); // 지출 테이블 저장
        Expenditure expenditure = expenditureRepository.findById(eno).get(); // 에코 테이블과 매핑
        ecoService.save(reuqest, expenditure); // 에코 테이블 저장
        return new ExpenditureResponseDto(eno);
    }

    /** 지출 데이터 수정 후 */
    @PostMapping("/expenditure/{id}/update")
    public ExpenditureResponseDto update_expenditure(@PathVariable("id") Long id, // 여기서 id는 eno를 의미
                                                     @RequestBody ExpenditureRequestDto request) throws IllegalAccessException {
        Long eno = detailService.update(id, request);
        return new ExpenditureResponseDto(eno);
    }

    /** 지출 데이터 삭제 */
    @DeleteMapping("/calendar/{month}/expenditure/{id}")
    public void delete_income(@PathVariable("id") Long id){
        detailService.delete(id);
    }

}
