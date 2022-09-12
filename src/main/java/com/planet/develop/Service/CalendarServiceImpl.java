package com.planet.develop.Service;

import com.planet.develop.DTO.*;
import com.planet.develop.DTO.CalendarDto.CalendarDayDto;
import com.planet.develop.DTO.CalendarDto.CalendarDto;
import com.planet.develop.DTO.EcoDto.EcoDto;
import com.planet.develop.DTO.ExpenditureDto.ExpenditureTypeDetailDto;
import com.planet.develop.DTO.ExpenditureDto.TypeDetailDto;
import com.planet.develop.Entity.Income;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.TIE;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Repository.AnniversaryRepository;
import com.planet.develop.Repository.ExpenditureRepository;
import com.planet.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class CalendarServiceImpl implements CalendarService {

    private final IncomeService incomeService;
    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;
    private final AnniversaryRepository anniversaryRepository;
    private final ExpenditureDetailService expenditureDetailService;

    /** 1일-31일 동안 하루 지출/수입/eco_count */
    @Override
    public CalendarDto findCalendar(String id, int year, int month) {
        User user = userRepository.findById(id).get();
        Long totalMonthIncome = incomeService.totalMonth(user,year,month); // 한 달 총 수입
        Long totalMonthExpenditure = expenditureDetailService.totalMonth(user, year, month); // 한 달 총 지출
        List<CalendarDayDto> calendarDayDtos = new ArrayList<>();

        int days = LocalDate.of(year,month,1).lengthOfMonth();
        int sumOfEcoCount=0;
        int sumOfNoneEcoCount=0;
        for(int n=1;n<=days;n++) {
            Long incomeDay = incomeService.totalDay(id, LocalDate.of(year, month,n));
            Long expenditureDay = expenditureDetailService.totalDay(user, LocalDate.of(year, month, n));
            int ecoCount = expenditureRepository.getDayEcoList(user, EcoEnum.G, LocalDate.of(year, month, n)).size();
            int noneEcoCount = expenditureRepository.getDayEcoList(user, EcoEnum.R, LocalDate.of(year, month, n)).size();
            sumOfEcoCount+=ecoCount;
            sumOfNoneEcoCount+=noneEcoCount;
            if (incomeDay!=0 || expenditureDay!=0) { // 수입/지출이 0이면 리스트에 담지 않음.
                CalendarDayDto calendarDayDto = new CalendarDayDto(LocalDate.of(2022, month, n), incomeDay, expenditureDay, ecoCount, noneEcoCount);
                calendarDayDtos.add(calendarDayDto);
            }
        }
        return new CalendarDto(sumOfEcoCount, sumOfNoneEcoCount, totalMonthIncome,totalMonthExpenditure, calendarDayDtos);
    }

    /** 유형별 하루 지출/수입 상세 */
    public Result findDayExTypeDetail(String id, int year, int month, int day) {
        User user = userRepository.findById(id).get();
        List<Income> in_days = incomeService.findDay(id, LocalDate.of(year, month, day)); // 수입
        List<ExpenditureTypeDetailDto> ex_days = expenditureDetailService.findDay(user, LocalDate.of(year, month, day)); // 지출
        String content = anniversaryRepository.getAnniversary(year, month, day); // 명언
        List<TypeDetailDto> in_detailDtos = getIncomeTypeDtos(in_days);
        List<TypeDetailDto> ex_detailDtos = getExpenditureTypeDtos(ex_days);
        in_detailDtos.addAll(ex_detailDtos);

        Map<money_Type, List<TypeDetailDto>> total = makeListToMap(in_detailDtos); // 유형별 수입/지출 map
        Map<money_Type, Long> moneyTotal = new HashMap<>();

        for (List<TypeDetailDto> value : total.values()) {
            for (TypeDetailDto typeDetailDto : value) {
                boolean income = typeDetailDto.isIncome();
                Long now=0L;
                if (income) // 수입이면 +
                    now+=typeDetailDto.getCost();
                else // 지출이면 -
                    now-=typeDetailDto.getCost();
                if (moneyTotal.containsKey(typeDetailDto.getType())) { // key가 이미 map에 존재한다면
                    Long sum = moneyTotal.get(typeDetailDto.getType())+now;
                    moneyTotal.replace(typeDetailDto.getType(), sum);
                } else { // key가 map에 존재하지 않는다면
                    moneyTotal.put(typeDetailDto.getType(), now);
                }
            }
        }
        return new Result(moneyTotal,total,content);
    }

    private List<TypeDetailDto> getIncomeTypeDtos(List<Income> in_days) {
        List<TypeDetailDto> in_detailDtos = new ArrayList<>();
        for (Income dto : in_days) { // 타입 변환
            TypeDetailDto typeDto = new TypeDetailDto();
            typeDto.saveIncomeType(dto.getIn_type(), dto.getIn_way(), dto.getIn_cost(), dto.getMemo(),dto.getId());
            in_detailDtos.add(typeDto);
        }
        return in_detailDtos;
    }

    @Override
    public List<TypeDetailDto> inExTypeDetailDto(String id, int month, int day, TIE tie) {
        User user = userRepository.findById(id).get();
        List<Income> in_days = new ArrayList<>();
        List<ExpenditureTypeDetailDto> ex_days = new ArrayList<>();
        if (tie == TIE.I) {
            in_days = incomeService.findDay(id, LocalDate.of(2022, month, day)); // 수입 상세 내역만
        } else if (tie == TIE.E) {
            ex_days = expenditureDetailService.findDay(user, LocalDate.of(2022, month, day)); // 지출 상세 내역만
        } else {
            in_days = incomeService.findDay(id, LocalDate.of(2022, month, day)); // 수입 상세 내역과
            ex_days = expenditureDetailService.findDay(user, LocalDate.of(2022, month, day)); // 지출 상세 내역 모두
        }
        List<TypeDetailDto> in_detailDtos = getIncomeTypeDtos(in_days);
        List<TypeDetailDto> ex_detailDtos = getExpenditureTypeDtos(ex_days);
        in_detailDtos.addAll(ex_detailDtos);
        return in_detailDtos;
    }

    /** 중복 선택한 친/반환경 데이터를 TypeDetailDto로 변환해서 List<TypeDetailDto> 타입으로 리턴 */
    public List<TypeDetailDto> getExpenditureTypeDtos(List<ExpenditureTypeDetailDto> ex_days) {
        List<TypeDetailDto> ex_detailDtos = new ArrayList<>();
        List<Long> exEnoList = new ArrayList<>();
        Long dupCheck = null;
        for (ExpenditureTypeDetailDto dto : ex_days) { // 중복 선택한 친반환경 지출
            // expenditure_eno에 따라 행이 중복 출력되는 오류 해결
            System.out.println();
            System.out.println();
            dupCheck = dto.getEno();
            if (exEnoList.contains(dupCheck)) // 친반환경 중복 제거 후
                continue;
            else // 친반환경 중복 제거 전
                exEnoList.add(dupCheck);
            // 중복 선택한 친/반환경 데이터를 List<EcoDto> 타입으로 변환해서 리턴
            List<EcoDto> dupEcoList = dupEcoList(ex_days, dupCheck);
            TypeDetailDto typeDto = new TypeDetailDto();
            typeDto.saveExpenditureType(dto.getExType(), dto.getExWay(), dto.getEno(), dto.getCost(), dto.getMemo(), dupEcoList);
            typeDto.setIncome(false);
            ex_detailDtos.add(typeDto);
        }
        return ex_detailDtos;
    }

    /** 중복 선택한 친/반환경 데이터를 List<EcoDto> 타입으로 변환해서 리턴 */
    @Override
    public List<EcoDto> dupEcoList(List<ExpenditureTypeDetailDto> ex_days, Long exEno) {
        List<EcoDto> ecoDtoList = new ArrayList<>();
        for (ExpenditureTypeDetailDto dto : ex_days) { // 하루 지출 (친반환경 중복 제거 전)
            System.out.println(exEno.compareTo(dto.getEno()));
            if (exEno.compareTo(dto.getEno()) == 0) { // 하루 지출 여러 개 중 하나 선택
                EcoDto ecoDto = new EcoDto(dto.getEco(), dto.getEcoDetail(), dto.getUserAdd());
                ecoDtoList.add(ecoDto);
            }
        }
        return ecoDtoList;
    }

    public Map<money_Type, List<TypeDetailDto>> makeListToMap(List<TypeDetailDto> in_detailDtos) {
        Map<money_Type, List<TypeDetailDto>> map = new HashMap<>();
        for (TypeDetailDto dto : in_detailDtos) {
            money_Type key = dto.getType();
            if (map.containsKey(key)) {
                List<TypeDetailDto> list = map.get(key);
                list.add(dto);
            } else {
                List<TypeDetailDto> list = new ArrayList<>();
                list.add(dto);
                map.put(key, list);
            }
        }
        return map;
    }

}