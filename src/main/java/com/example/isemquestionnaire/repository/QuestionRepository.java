package com.example.isemquestionnaire.repository;

import com.example.isemquestionnaire.model.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Questionnaire, Long> {
}
