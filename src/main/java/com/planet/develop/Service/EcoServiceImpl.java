package com.planet.develop.Service;

import com.planet.develop.DTO.EcoCostDto;
import com.planet.develop.DTO.EcoDetailDto;
import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.Entity.Eco;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Repository.EcoRepository;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class EcoServiceImpl implements EcoService {

    @Autowired
    private final EcoRepository ecoRepository;

    @Override
    public void save(ExpenditureRequestDto dto, Expenditure expenditure) {
        List<Eco> ecos = dtoToEntity(dto, expenditure);
        for (Eco eco : ecos) {
            ecoRepository.save(eco);
        }
    }

    @Override
    public void update(Long id, ExpenditureRequestDto dto, Expenditure expenditure) {
        this.delete(id, dto); // 친/반환경 데이터 삭제하기
        this.save(dto, expenditure); // 친/반환경 데이터 다시 저장하기
    }

    @Override
    public void delete(Long id, ExpenditureRequestDto dto) {
        List<Long> ecoEnoList = ecoRepository.getEcoByEno(id);
        for (Long eno : ecoEnoList) // 친/반환경 데이터 전부 삭제하기
            ecoRepository.deleteById(eno);
    }

    /** 중복 제거된 한 달 특정 날짜까지 친반환경별 지출 리스트 */
    @Override
    public List<EcoDetailDto> dupCheckedListByEco(String user, int year, int month, int days, EcoEnum eco) {
        List<EcoDetailDto> beforeDupCheckList = exListByEco(user, year, month, days, eco);
        for (EcoDetailDto dto : beforeDupCheckList) {
            log.info(dto.getEx_eno() + " " + dto.getEx_eno());
        }
        List<EcoDetailDto> afterDupCheckList = dupCheck(beforeDupCheckList);
        return afterDupCheckList;
    }

    /** 한 달 특정 날짜까지 친반환경별 지출 리스트 */
    @Override
    public List<EcoDetailDto> exListByEco(String user, int year, int month, int days, EcoEnum eco) {
        List<EcoDetailDto> ecoList = new ArrayList<>();
        IntStream.rangeClosed(1, days).forEach(day -> {
            LocalDate date = LocalDate.of(year, month, day);
            List<Object[]> listByEco = ecoRepository.getListByEco(user, date, eco);
            for (Object[] ob : listByEco) {
                EcoDetailDto detailDto = new EcoDetailDto(ob[0], ob[1], ob[2], ob[3], ob[4]);
                ecoList.add(detailDto);
            }
        });
        return ecoList;
    }

    /** 특정 날짜까지 친반환경별 지출 총액 */
    @Override
    public Long totalMonthByEco(String user, int year, int month, int days, EcoEnum eco) {
        Long total = 0L;
        List<EcoDetailDto> ecoList = dupCheckedListByEco(user, year, month, days, eco);
        for (EcoDetailDto dto : ecoList) {
            total += dto.getCost();
        }
        return total;
    }

    @Override
    public EcoCostDto getEcoCost(String user, int year, int month, int days) {
        Long totalEcoG = totalMonthByEco(user, year, month, days, EcoEnum.G);
        Long totalEcoR = totalMonthByEco(user, year, month, days, EcoEnum.R);
        return new EcoCostDto(totalEcoG, totalEcoR);
    }

}
