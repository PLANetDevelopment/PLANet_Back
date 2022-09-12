package com.planet.develop.Controller;

import com.planet.develop.DTO.*;
import com.planet.develop.DTO.CalendarDto.CalendarDto;
import com.planet.develop.DTO.CalendarDto.CalendarResponseDto;
import com.planet.develop.Entity.Quote;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Repository.AnniversaryRepository;
import com.planet.develop.Repository.QuoteRepository;
import com.planet.develop.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class CalendarController {

    private final CalendarService calendarService;
    private final QuoteRepository quoteRepository;
    private final AnniversaryRepository anniversaryRepository;

    Random random = new Random();

    /** 월별 수입/지출 조회 함수 */
    @GetMapping("/calendar/{year}/{month}")
    public CalendarResponseDto findCalendar(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int  year, @PathVariable("month") int month){
        CalendarDto calendar = calendarService.findCalendar(userId,year,month);
        int qno = random.nextInt(40) + 1;
        Quote quote = quoteRepository.findById(qno).get();
        Optional<List<Object[]>> anniversaryList = Optional.ofNullable(anniversaryRepository.getAnniversaryList(month));
        return new CalendarResponseDto(anniversaryList, calendar, quote.getContent());
    }

    /** 일별 조회 (세부 조회) */
    @GetMapping("/calendar/{year}/{month}/{day}")
    public Result findIncomeDetail(@RequestHeader(JwtProperties.USER_ID) String userId,@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
        return  calendarService.findDayExTypeDetail(userId,year,month,day);
    }

}