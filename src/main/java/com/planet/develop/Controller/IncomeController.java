package com.planet.develop.Controller;

import com.planet.develop.DTO.IncomeRequestDto;
import com.planet.develop.DTO.IncomeResponseDto;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://main.d2f9fwhj50mv28.amplifyapp.com")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class IncomeController {

    private final IncomeService incomeService;

    /** 수입 데이터 저장*/
    @PostMapping("/income/new")
    public IncomeResponseDto create_income(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestBody IncomeRequestDto dto) {
        dto.setUserId(userId);
        Long incomeId = incomeService.save(dto);
        return new IncomeResponseDto(incomeId);
    }

    /**수입 데이터 수정*/
    @PostMapping("/income/{id}/update")
    public IncomeResponseDto update_income(@PathVariable("id") Long id, @RequestBody IncomeRequestDto request){
        incomeService.update(id,request.getIn_cost(),request.getIn_way(),
                request.getIn_type(),request.getMemo(),request.getDate());

        return new IncomeResponseDto(id);
    }

    /** 수입 데이터 삭제*/
    @DeleteMapping("/calendar/{month}/income/{id}")
    public void delete_income(@PathVariable("id") Long id){
        incomeService.delete(id);
    }

}