package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.*;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion_QuestionId(Long questionId);
    List<Answer> findByQuestion_QuestionIdIn(List<Long> questionIds);
}