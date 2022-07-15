package com.planet.develop.Service;

import com.planet.develop.DTO.*;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import com.planet.develop.Repository.EcoRepository;
import com.planet.develop.Repository.ExpenditureDetailRepository;
import com.planet.develop.Repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExpenditureDetailServiceImpl implements ExpenditureDetailService {

    private final ExpenditureRepository expenditureRepository;
    private final ExpenditureDetailRepository detailRepository;
    private final EcoRepository ecoRepository;
    private final EcoService ecoService;
    private final ExpenditureService expenditureService;

    private final EntityManager em;

    /** 지출 등록 */
    @Override
    public Long save(ExpenditureRequestDto dto) {
        ExpenditureDetail entity = dtoToEntity(dto);
        detailRepository.save(entity);
        return entity.getEno();
    }

    /** 하루 지출 총액 */
    @Override
    public Long totalDay(User user, LocalDate date) {
        Long total = 0L;
        List<Object[]> exTypeList = expenditureRepository.getDayExpenditure(user, date);
        for (Object[] arr : exTypeList) {
            total += (Long) arr[0];
        }
        return total;
    }

    /** 하루 지출 유형별 총액 */
    @Override
    public Long totalTypeDay(User user, money_Type type, LocalDate date) {
        Long total = 0L;
        List<Object[]> exTypeList = expenditureRepository.getDayExTypeList(user, type, date);
        for (Object[] arr : exTypeList) {
            total += (Long) arr[1];
        }
        return total;
    }

    /** 하루 지출 방법별 총액 */
    @Override
    public Long totalWayDay(User user, money_Way way, LocalDate date) {
        Long total = 0L;
        List<Object[]> exWayList = expenditureRepository.getDayExWayList(user, way, date);
        for (Object[] arr : exWayList) {
            total += (Long) arr[1];
        }
        return total;
    }

    @Override
    public List<ExpenditureTypeDetailDto> findDay(User user, LocalDate date) {
        List<Object[]> dayList = expenditureRepository.getDayList(user, date);
        List<ExpenditureTypeDetailDto> dtoList = new ArrayList<>();
        for (Object[] arr : dayList) {
            ExpenditureTypeDetailDto dto = new ExpenditureTypeDetailDto(
                    arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8]);
            dtoList.add(dto);
        }
        return dtoList;
    }

    /** 한 달 지출 총액 */
    @Override
    public Long totalMonth(User user, int year , int month) {
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = LocalDate.of(year,month,startDate.lengthOfMonth());
        Long total = expenditureRepository.calMonth(user,startDate,endDate);
        if (total==null)
            total=0L;
        return total;
    }

    /** 한 달 지출 유형별 총액 */
    @Override
    public Long totalMonthType(User user, int year, int month, money_Type type) {
        Long total = 0L;
        List<Expenditure> exTypeList = getMonthTypeList(user, year, month, type);
        for (Expenditure e : exTypeList) {
            ExpenditureRequestDto dto = expenditureService.entityToDto(e);
            total += dto.getEx_cost();
        }
        return total;
    }

    /** 한 달 지출 방법별 총액 */
    @Override
    public Long totalWayMonth(User user, int year, int month, money_Way way) {
        Long total = 0L;
        List<Expenditure> exWayList = getMonthWayList(user, year, month, way);
        for (Expenditure e : exWayList) {
            ExpenditureRequestDto dto = expenditureService.entityToDto(e);
            total += dto.getEx_cost();
        }
        return total;
    }

    /** 한 달 특정 날짜까지 총 지출 */
    @Override
    public Long totalMonthDay(User user, int year, int month, int day) {
        Long total = 0L;
        List<Expenditure> exList = getMonthList(user, year, month, day);
        for (Expenditure e : exList) {
            ExpenditureRequestDto dto = expenditureService.entityToDto(e);
            total += dto.getEx_cost();
        }
        return total;
    }

    /** 한 달 지출 리스트 */
    @Override
    public List<Expenditure> getMonthList(User user, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        int lengthOfMonth = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(year, month, lengthOfMonth);
        return em.createQuery("select e from Expenditure e left join ExpenditureDetail ed on e.eno = ed.eno " +
                "where :startDate <= e.date and e.date <= :endDate " +
                "and e.user = :user", Expenditure.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .setParameter("user", user)
                .getResultList();
    }

    /** 한 달 특정 날짜까지 지출 리스트 */
    @Override
    public List<Expenditure> getMonthList(User user, int year, int month, int day) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, day);
        return em.createQuery("select e from Expenditure e left join ExpenditureDetail ed on e.eno = ed.eno " +
                "where :startDate <= e.date and e.date <= :endDate " +
                "and e.user = :user", Expenditure.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Expenditure> getMonthTypeList(User user, int year, int month, money_Type type) {
        LocalDate startDate = LocalDate.of(year,month,1);
        int lengthOfMonth = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(year,month,lengthOfMonth);
        return em.createQuery("select e from Expenditure e left join ExpenditureDetail ed on e.eno = ed.eno " +
                "where :startDate <= e.date and e.date <= :endDate " +
                "and e.user = :user and e.detail.exType = :type", Expenditure.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .setParameter("user", user)
                .setParameter("type", type)
                .getResultList();
    }

    /** 한 달 지출 방법별 리스트 */
    @Override
    public List<Expenditure> getMonthWayList(User user, int year, int month, money_Way way) {
        LocalDate startDate = LocalDate.of(year,month,1);
        int lengthOfMonth = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(year,month,lengthOfMonth);
        return em.createQuery("select e from Expenditure e left join ExpenditureDetail ed on e.eno = ed.eno " +
                "where :startDate <= e.date and e.date <= :endDate " +
                "and e.user = :user and e.detail.exWay = :way", Expenditure.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .setParameter("user", user)
                .setParameter("way", way)
                .getResultList();
    }

    /** 지출 수정 */
    @Transactional
    public Long update(Long id, ExpenditureRequestDto dto) throws IllegalAccessException {
        Expenditure expenditure = expenditureRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        ExpenditureDetail expenditureDetail = detailRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        expenditure.update(dto.getEx_cost(), dto.getDate()); // 지출 테이블 수정
        expenditureDetail.update(dto.getExType(), dto.getExWay(), dto.getMemo()); // 지출 상세 테이블 수정
        ecoService.update(id, dto, expenditure); // 에코 테이블 수정
        return id;
    }

    @Override
    public void delete(Long id) {
        List<Long> ecoByEno = ecoRepository.getEcoByEno(id);
        for (Long eno : ecoByEno)
            ecoRepository.deleteById(eno);
        expenditureRepository.deleteById(id);
    }

}
