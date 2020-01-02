package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CandidateSqlTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSqlTaskRepository extends JpaRepository<CandidateSqlTask, Long> {
}
