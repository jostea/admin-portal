package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CorrectCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectCodeRepository extends JpaRepository<CorrectCode, Long> {
}
