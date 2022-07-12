package com.planet.develop.Entity;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
@Getter
public class BaseEntity {
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate date;
    public void changeDate(LocalDate date) {
        this.date=date;
    }
}