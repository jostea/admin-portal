package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CandidateCustomTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateCustomTaskRepository extends JpaRepository<CandidateCustomTask, Long> {
}
