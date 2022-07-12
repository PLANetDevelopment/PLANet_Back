package com.planet.develop.Service;

import com.planet.develop.DTO.*;
import com.planet.develop.Enum.TIE;

import java.util.List;

public interface CalendarService {
    /** 월별 지출/수입 총액과 일별 지출/소비 총액 구하기 */
    CalendarDto findCalendar(String id, int year, int month);
    /** 유형별 하루 지출/수입 상세 */
    Result findDayExTypeDetail(String id, int year, int month, int day);
    /** 중복 선택한 친/반환경 데이터를 TypeDetailDto로 변환해서 List<TypeDetailDto> 타입으로 리턴 */
    List<TypeDetailDto> getExpenditureTypeDtos(List<ExpenditureTypeDetailDto> ex_days);
    /** 친/반환경 중복 체크 시 */
    List<EcoDto> dupEcoList(List<ExpenditureTypeDetailDto> ex_days, Long exEno);
    /** 일별 전체/수입/지출 상세 내역 */
    List<TypeDetailDto> inExTypeDetailDto(String id, int month, int day, TIE tie);
}
