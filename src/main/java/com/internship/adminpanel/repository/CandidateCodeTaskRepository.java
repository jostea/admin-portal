package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CandidateCodeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateCodeTaskRepository extends JpaRepository<CandidateCodeTask, Long> {
}
