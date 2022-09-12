package com.planet.develop.Service;

import com.planet.develop.DTO.IncomeDto.IncomeRequestDto;
import com.planet.develop.Entity.Income;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    /** 수입 입력 **/
    public Long save(IncomeRequestDto dto);

    /** 수입 변경 **/
    public void update(Long income_id, Long in_cost, money_Way in_way, money_Type in_type, String memo, LocalDate date);

    /** 수입 삭제 **/
    public void delete(Long income_id);

    /** 일별 조회 **/
    public List<Income> findDay(String user_id, LocalDate date);

    /** 특정 일별 총합  **/
    public Long totalDay(String user_id,LocalDate date);
    /** type 일별 총합 **/
    public Long typeDay(String user_id, LocalDate date, money_Type type);

    /** way 일별 총합 **/
    public Long wayDay(String user_id, LocalDate date, money_Way way);
    /** 월별 조회 **/
    public List<Income> findMonth(String user_id,int month);

    /** 월별 총합2 **/
    Long totalMonth(User user, int year, int month);

    /** type 월별 총합 **/
    public Long typeMonth(String user_id, int Month, money_Type type);

    /** way 월별 총합 **/
    public Long wayMonth(String user_id, int Month, money_Way way);

    /** 한 달 특정 일까지의 총 수입 **/
    public Long totalMonthDay(String user_id, int month, int day);

    default Income dtoToEntity(IncomeRequestDto dto) {
        User user = User.builder()
                .userId(dto.getUserId())
                .build();
        Income income = Income.builder()
                .in_cost(dto.getIn_cost())
                .date(dto.getDate())
                .in_type(dto.getIn_type())
                .in_way(dto.getIn_way())
                .memo(dto.getMemo())
                .user(user)
                .build();
        return income;
    }
}