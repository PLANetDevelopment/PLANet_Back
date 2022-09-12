package com.planet.develop.Service;

import com.planet.develop.DTO.ExpenditureDto.ExpenditureTypeDetailDto;
import com.planet.develop.DTO.ExpenditureDto.ExpenditureRequestDto;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;

import java.time.LocalDate;
import java.util.List;

public interface ExpenditureDetailService {

    Long save(ExpenditureRequestDto dto);

    Long totalDay(User user, LocalDate date);

    Long totalTypeDay(User user, money_Type type, LocalDate date);

    Long totalWayDay(User user, money_Way way, LocalDate date);

    List<ExpenditureTypeDetailDto> findDay(User user, LocalDate date);

    List<ExpenditureTypeDetailDto> findDayByType(User user, LocalDate date, money_Type type);

    Long totalMonth(User user, int year, int month);

    Long totalMonthType(User user, int year, int month, money_Type type);

    Long totalWayMonth(User user, int year, int month, money_Way way);

    Long totalMonthDay(User user, int year, int month, int day);

    List<Expenditure> getMonthList(User user, int year, int month);

    List<Expenditure> getMonthList(User user, int year, int month, int day);

    List<Expenditure> getMonthTypeList(User user, int year, int month, money_Type type);

    List<Expenditure> getMonthWayList(User user, int year, int month, money_Way way);

    Long update(Long id, ExpenditureRequestDto dto) throws IllegalAccessException;

    void delete(Long id);

    default ExpenditureDetail dtoToEntity(ExpenditureRequestDto dto) {
        ExpenditureDetail entity = ExpenditureDetail.builder()
                .exType(dto.getExType())
                .exWay(dto.getExWay())
                .memo(dto.getMemo())
                .build();
        return entity;
    }

}
