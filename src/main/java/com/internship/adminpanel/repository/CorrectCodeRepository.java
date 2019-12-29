package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.CorrectCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrectCodeRepository extends JpaRepository<CorrectCode, Long> {
    List<CorrectCode> findAllByCodeTask(CodeTask codeTask);
}
