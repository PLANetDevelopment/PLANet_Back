package com.planet.develop.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Mission {
    @Id @GeneratedValue
    @Column(name="mno")
    private Long id;
    private String emoji;
    private String name;
}