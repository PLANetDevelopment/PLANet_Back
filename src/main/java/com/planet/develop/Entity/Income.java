package com.planet.develop.Entity;

import com.planet.develop.Enum.money_Type;
import com.planet.develop.Enum.money_Way;
import com.planet.develop.Login.Model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Income extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="ino")
    private Long id;

    private Long in_cost;

    @Enumerated(EnumType.STRING)
    private money_Way in_way;

    @Enumerated(EnumType.STRING)
    private money_Type in_type;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_code")
    private User user;

    @Builder
    public Income(Long in_cost, money_Way in_way, money_Type in_type, String memo, LocalDate date, User user){
        this.in_cost=in_cost;
        this.in_way=in_way;
        this.in_type=in_type;
        this.memo=memo;
        this.user=user;
        changeDate(date);
    }

    public void update_income(Long in_cost, money_Way in_way, money_Type in_type, String memo, LocalDate date){
        this.in_cost=in_cost;
        this.in_way=in_way;
        this.in_type=in_type;
        this.memo=memo;
        changeDate(date);
    }

}