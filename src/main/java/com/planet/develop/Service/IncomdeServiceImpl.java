package com.planet.develop.Service;

import com.planet.develop.DTO.IncomeRequestDto;
import com.planet.develop.Entity.Income;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import com.planet.develop.Login.Model.KakaoUser;
import com.planet.develop.Login.Repository.KakaoUserRepository;
import com.planet.develop.Repository.IncomeRepository;
import com.planet.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class IncomeServiceImpl implements IncomeService{

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;

    /** 수입 입력 **/
    @Override
    public Long save(IncomeRequestDto dto) {
        Income entity = dtoToEntity(dto);
        incomeRepository.save(entity);
        return entity.getId();
    }

    /** 수입 변경 **/
    @Override
    public void update(Long income_id, Long in_cost, money_Way in_way, money_Type in_type, String memo, LocalDate date){
        Income income = incomeRepository.findOne(income_id);
        income.update_income(in_cost, in_way, in_type, memo, date);
    }

    /** 수입 삭제 **/
    @Override
    public void delete(Long income_id){
        Income income = incomeRepository.findOne(income_id);
        incomeRepository.delete(income);
    }

    /** 일별 조회 **/
    @Override
    public List<Income> findDay(String user_id, LocalDate date){
        User user = userRepository.findById(user_id).get();
        List<Income> days = incomeRepository.findDay(user, date);
        return days;
    }

    /** 특정 일별 총합  **/
    @Override
    public Long totalDay(String user_id,LocalDate date) {
        User user = userRepository.findById(user_id).get();
        List<Income> days=incomeRepository.findDay(user, date);
        Long total=0L;
        for (Income day : days) {
            total+=day.getIn_cost();
        }
        return total;
    }
    /** type 일별 총합 **/
    @Override
    public Long typeDay(String user_id, LocalDate date, money_Type type) {
        User user = userRepository.findById(user_id).get();
        List<Income> days=incomeRepository.findDay(user,date);
        Long total=0L;
        for (Income day : days) {
            if(day.getIn_type()==type)
                total+=day.getIn_cost();
        }
        return total;
    }

    /** way 일별 총합 **/
    @Override
    public Long wayDay(String user_id, LocalDate date, money_Way way) {
        User user = userRepository.findById(user_id).get();
        List<Income> days=incomeRepository.findDay(user, date);
        Long total=0L;
        for (Income day : days) {
            if(day.getIn_way()==way)
                total+=day.getIn_cost();
        }
        return total;
    }

    /** 월별 조회 **/
    @Override
    public List<Income> findMonth(String user_id,int month){
        User user = userRepository.findById(user_id).get();
        List<Income> days = incomeRepository.findMonth(user, month);
        return days;
    }

    /** 월별 총합2 **/
    @Override
    public Long totalMonth(User user, int year, int month) {
        Long total = incomeRepository.calMonth(user,year,month);
        return total;
    }

    /** type 월별 총합 **/
    @Override
    public Long typeMonth(String user_id, int Month, money_Type type) {
        User user = userRepository.findById(user_id).get();
        List<Income> days=incomeRepository.findMonth(user,Month);
        Long total=0L;
        for (Income day : days) {
            if(day.getIn_type()==type)
                total+=day.getIn_cost();
        }
        return total;
    }

    /** way 월별 총합 **/
    @Override
    public Long wayMonth(String user_id, int Month, money_Way way) {
        User user = userRepository.findById(user_id).get();
        List<Income> days=incomeRepository.findMonth(user, Month);
        Long total=0L;
        for (Income day : days) {
            if(day.getIn_way()==way)
                total+=day.getIn_cost();
        }
        return total;
    }

    /** 한 달 특정 일까지의 총 수입 **/
    @Override
    public Long totalMonthDay(String user_id, int month, int day) {
        User user = userRepository.findById(user_id).get();
        List<Income> incomeList = incomeRepository.findMonthDay(user, month, day);
        Long total = 0L;
        for (Income income : incomeList) {
            total += income.getIn_cost();
        }
        return total;
    }

}