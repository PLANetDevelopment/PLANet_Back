package com.planet.develop.Service;

import com.planet.develop.DTO.CheckDupDto;
import com.planet.develop.DTO.EcoCostDto;
import com.planet.develop.DTO.EcoDetailDto;
import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.Entity.Eco;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Enum.EcoDetail;
import com.planet.develop.Enum.EcoEnum;

import java.util.ArrayList;
import java.util.List;

public interface EcoService {

    /** 한 달 특정 날짜까지 친반환경별 지출 리스트 */
    List<EcoDetailDto> exListByEco(String user, int year, int month, int days, EcoEnum eco);

    /** 특정 날짜까지 친반환경별 지출 총액 */
    Long totalMonthByEco(String user, int year, int month, int days, EcoEnum eco);

    EcoCostDto getEcoCost(String user, int year, int month, int days);

    void save(ExpenditureRequestDto dto, Expenditure expenditure);

    void update(Long id, ExpenditureRequestDto dto, Expenditure expenditure);

    void delete(Long id, ExpenditureRequestDto dto);

    /** 중복 제거된 한 달 특정 날짜까지 친반환경별 지출 리스트 */
    List<EcoDetailDto> dupCheckedListByEco(String user, int year, int month, int days, EcoEnum eco);

    /** 한 달 특정 날짜까지 친반환경별 지출 리스트 중복 제거 */
    default List<EcoDetailDto> dupCheck(List<EcoDetailDto> ecoList) {
        List<EcoDetailDto> checkList = new ArrayList<>();
        List<CheckDupDto> checkDup = new ArrayList<>();
        for (EcoDetailDto dto : ecoList) {
            CheckDupDto check = new CheckDupDto(dto.getEx_eno(), dto.getEco());
            if (!checkDup.contains(check)) { // 새로운 데이터라면 (중복되지 않았다면)
                checkList.add(dto);
                checkDup.add(check);
            }
        }
        return checkList;
    }

    default List<Eco> dtoToEntity(ExpenditureRequestDto dto, Expenditure expenditure) {
        List<Eco> ecoList = new ArrayList<>();
        List<EcoDetail> ecoDetails = dto.getEcoDetail();
        int i = 0;
        for (EcoDetail ecoDetail : ecoDetails) {
            String userAdd = null; // etc가 아니라면 null을 유지
            // ecoDetail -> eco(G, N, R)로 변환
            EcoEnum ecoEnum = EcoEnum.N;
            if (EcoDetail.ecoProducts.equals(ecoDetail) || EcoDetail.vegan.equals(ecoDetail)
                    || EcoDetail.multiUse.equals(ecoDetail) || EcoDetail.personalBag.equals(ecoDetail)
                    || EcoDetail.sharing.equals(ecoDetail)) {
                ecoEnum = EcoEnum.G;
            } else if (EcoDetail.disposable.equals(ecoDetail) || EcoDetail.plasticBag.equals(ecoDetail)
                    || EcoDetail.wasteFood.equals(ecoDetail)) {
                ecoEnum = EcoEnum.R;
            } else if (EcoDetail.etc.equals(ecoDetail)) {
                ecoEnum = EcoEnum.N;
            } else { // 사용자가 친반환경 태그를 직접 추가한 경우
                userAdd = dto.getUserAdd().get(i);
                ecoEnum = dto.getEco().get(i);
                i++;
            }
            Eco entity = Eco.builder()
                    .eco(ecoEnum)
                    .ecoDetail(ecoDetail)
                    .userAdd(userAdd)
                    .expenditure(expenditure)
                    .build();
            ecoList.add(entity);
        }
        return ecoList;
    }
}
