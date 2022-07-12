package com.planet.develop.Entity;

import com.planet.develop.Enum.EcoDetail;
import com.planet.develop.Enum.EcoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Eco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;

    @Enumerated(EnumType.STRING)
    private EcoEnum eco;

    @Enumerated(EnumType.STRING)
    @Column(name = "eco_detail")
    private EcoDetail ecoDetail;

    private String userAdd;

    @ManyToOne(fetch = FetchType.LAZY)
    private Expenditure expenditure;

}
