package com.planet.develop.Service;

import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Repository.EcoRepository;
import com.planet.develop.Repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExpenditureServiceImpl implements ExpenditureService {

    private final ExpenditureRepository repository;

    @Override
    public Long save(ExpenditureRequestDto dto, ExpenditureDetail detail) {
        Expenditure entity = dtoToEntity(dto, detail);
        repository.save(entity);
        return entity.getEno();
    }

}
