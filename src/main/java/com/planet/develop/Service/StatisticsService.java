package com.planet.develop.Service;

import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Login.Model.KakaoUser;
import com.planet.develop.Repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public Map<Integer,Long> getYearEcoCount(User user, EcoEnum eco, int year){
        Map<Integer,Long> result=new HashMap<>();
        for(int n=1;n<=12;n++){
            Long monthEcoCount=getMonthEcoCount(user,eco,year,n);
            result.put(n,monthEcoCount);
        }
        return result;
    }


    public Long getMonthEcoCount(User user, EcoEnum eco, int year, int month){
        Long monthEcoCount = statisticsRepository.getMonthEcoCount(user, eco,year, month);
        return monthEcoCount;
    }

    public Long getGuessMonthEcoCount(User user, int year, int month, int day){
        LocalDate startDate = LocalDate.of(year,month,day);
        LocalDate endDate = LocalDate.of(year,month,startDate.lengthOfMonth());
        Long nowEcoCount = statisticsRepository.getNowEcoCount(user,LocalDate.of(year, month, 1),startDate,EcoEnum.G);
        Long sum=0L;
        startDate=startDate.plusDays(1);
        for(int i=1;i<=12;i++) {
            startDate= startDate.minusMonths(i);
            endDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.lengthOfMonth());
            sum+=statisticsRepository.getNowEcoCount(user, startDate,endDate, EcoEnum.G);
        }
        Long avg= 0L;
        if (sum<12)
            avg= sum;
        else
            avg=sum/12;

        return avg+nowEcoCount;
    }

    public Map<String,Object> getEcoCountComparedToLast(User user, int year, int month){
        Map<String, Object> eco = new HashMap<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate=LocalDate.now();
        if(endDate.getMonthValue()!=month) // 현재 달이 요청한 달이 아니라면
            endDate = LocalDate.of(year,month,startDate.lengthOfMonth());

        LocalDate last=endDate.minusMonths(1);

        Long ecoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,EcoEnum.G);
        System.out.println("현재 달 친환경 태그 개수: " + ecoCount);
        Long noneEcoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,EcoEnum.R);
        System.out.println("현재 달 반환경 태그 개수: " + noneEcoCount);
        Long lastEcoCount=statisticsRepository.getLastEcoCount(user,last,startDate.minusMonths(1),EcoEnum.G);
        System.out.println("현재 달이 아닌 친환경 태그 개수: " + ecoCount);
        Long lastNoEcoCount=statisticsRepository.getLastEcoCount(user,last,startDate.minusMonths(1),EcoEnum.R);
        System.out.println("현재 달이 아닌 반환경 태그 개수: " + ecoCount);

        Long ecoDifference = ecoCount-lastEcoCount;
        Long noEcoDifference = noneEcoCount - lastNoEcoCount;
        double percentage = getPercentage(ecoCount, noneEcoCount);
        System.out.println("ecoCount = " + ecoCount);
        System.out.println("noneEcoCount = " + noneEcoCount);
        eco.put("ecoDifference",ecoDifference);
        eco.put("noEcoDifference",noEcoDifference);
        eco.put("percentage",percentage);
        eco.put("nowEcoCount",ecoCount);
        eco.put("noneEcoCount",noneEcoCount);
        return eco;
    }

    /** 친/반환경 퍼센테이지 구하는 함수**/
    public double getPercentage(Long ecoCount, Long noneEcoCount) {
        double percentage=0L;
        if (ecoCount !=0 & noneEcoCount !=0){
            percentage = Math.round((double) ecoCount /(double)(noneEcoCount + ecoCount)*100);


        }
        if (ecoCount !=0 & noneEcoCount ==0){
            percentage=100L;
        }
        return percentage;
    }


    /** 상위 4개 + 더보기 태그 보여주기 */
    public  List<List<Object[]>> getFiveTagCounts(User user, int year, int month){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate=LocalDate.of(year, month, startDate.lengthOfMonth());
        List<Object[]> categoryFiveTagCount = statisticsRepository.getCategoryFiveTagCount(user, startDate, endDate, EcoEnum.G);
        List<Object[]> categoryFiveNoTagCount = statisticsRepository.getCategoryFiveTagCount(user, startDate, endDate, EcoEnum.R);
        Long ecoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,EcoEnum.G);
        Long noneEcoCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,EcoEnum.R);

        List<List<Object[]>> result=new ArrayList<>();
        List<Object[]> eco = new ArrayList<>();
        List<Object[]> noEco = new ArrayList<>();
        Long ecoCnt=0L;
        Long noEcoCnt=0L;
        for (Object[] tag : categoryFiveTagCount) {
            eco.add(tag);
            ecoCnt+=(Long)tag[1];
        }
        for (Object[] tag : categoryFiveNoTagCount) {
            noEco.add(tag);
            noEcoCnt+=(Long)tag[1];
        }
        Object eco_remain[]=new Object[2];
        eco_remain[0]="더보기";
        eco_remain[1] = ecoCount - ecoCnt;
        eco.add(eco_remain);
        eco_remain[1] = noneEcoCount-noEcoCnt;
        noEco.add(eco_remain);

        result.add(eco);
        result.add(noEco);
        return result;
    }

    public List<Object[]> getTagCategoryList(User user, int year, int month, EcoEnum ecoEnum){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate=LocalDate.of(year, month, startDate.lengthOfMonth());
        List<Object[]> categoryList= statisticsRepository.getCategoryList(user, startDate, endDate,ecoEnum);
        Long totalCount=statisticsRepository.getNowEcoCount(user, endDate, startDate,ecoEnum);
        List<Object[]> result = new ArrayList<>();
        for (Object[] objects : categoryList) {
            Object[] category = new Object[3];
            category[0]=objects[0];
            Long tmp= (Long)objects[1];
            double cnt = (double) tmp;
            // 퍼센테이지
            category[1] = Math.round(cnt/(double)totalCount*100);
            //개수
            category[2]=objects[1];
            result.add(category);
        }
        return result;
    }

}