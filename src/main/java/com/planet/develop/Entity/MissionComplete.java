package com.planet.develop.Entity;

import com.planet.develop.Login.Model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MissionComplete extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="mcno")
    private Long id;

    private String emoji;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_code")
    private User user;

    @Builder
    public MissionComplete(String emoji,String name,User user,LocalDate date){
        this.emoji=emoji;
        this.name=name;
        this.user = user;
        changeDate(date);
    }
}