package com.planet.develop.Service;

import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Login.Model.User;

public interface ExpenditureService {

    Long save(ExpenditureRequestDto dto, ExpenditureDetail detail);

    default Expenditure dtoToEntity(ExpenditureRequestDto dto,
                                    ExpenditureDetail detail) {
        User user = User.builder()
                .kakaoEmail(dto.getUserId())
                .build();
        Expenditure exEntity = Expenditure.builder()
                .eno(detail.getEno())
                .cost(dto.getEx_cost())
                .user(user)
                .detail(detail)
                .build();
        exEntity.changeDate(dto.getDate());
        return exEntity;
    }

    default ExpenditureRequestDto entityToDto(Expenditure entity) {
        ExpenditureRequestDto dto = ExpenditureRequestDto.builder()
                .userId(entity.getUser().getKakaoEmail())
                .ex_cost(entity.getCost())
                .date(entity.getDate())
                .build();
        return dto;
    }

}
