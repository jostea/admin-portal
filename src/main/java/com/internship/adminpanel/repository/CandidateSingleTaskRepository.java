package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CandidateSingleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSingleTaskRepository extends JpaRepository<CandidateSingleTask, Long> {
}
