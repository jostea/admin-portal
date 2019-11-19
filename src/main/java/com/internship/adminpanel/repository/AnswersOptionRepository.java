package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.AnswersOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersOptionRepository extends JpaRepository<AnswersOption, Long> {
}
