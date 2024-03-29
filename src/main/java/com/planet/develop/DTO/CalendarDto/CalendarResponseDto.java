package com.planet.develop.DTO.CalendarDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponseDto {
    Optional<List<Object[]>> anniversaryList;
    CalendarDto calendarDto;
    String content;
}