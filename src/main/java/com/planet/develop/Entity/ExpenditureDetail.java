package com.planet.develop.Entity;

import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
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
public class ExpenditureDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;

    @Enumerated(EnumType.STRING)
    @Column(name = "ex_way")
    private money_Way exWay;

    @Enumerated(EnumType.STRING)
    @Column(name = "ex_type")
    private money_Type exType;

    private String memo;

    public void update(money_Type type, money_Way way, String memo) {
        this.exType = type;
        this.exWay = way;
        this.memo = memo;
    }

}
