package com.planet.develop.Entity;

import com.planet.develop.DTO.QnaDto;
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
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Boolean isAnswer;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Qna(String title, String content, Boolean isAnswer, String answer) {
        this.title = title;
        this.content = content;
        this.isAnswer = isAnswer;
        this.answer = answer;
    }

    public QnaDto entityToDto() {
        return new QnaDto(
                this.qno, this.title, this.content, this.isAnswer, this.answer, this.user.getUserId());
    }

}
