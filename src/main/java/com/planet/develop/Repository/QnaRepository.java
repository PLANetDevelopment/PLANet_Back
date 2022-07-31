package com.planet.develop.Repository;

import com.planet.develop.Entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    /** 문의 조회 */
    @Query("select q from Qna q")
    List<Qna> getQnaList();

    /** 문의 완료 */
    @Transactional
    @Modifying
    @Query(" update Qna q set q.isAnswer = true, q.answer = :answer  where q.qno = :qno")
    void saveAnswer(@Param("answer") String answer, @Param("qno") Long qno);

}
