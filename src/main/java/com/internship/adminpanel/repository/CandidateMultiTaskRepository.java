package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.CandidateMultiTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateMultiTaskRepository extends JpaRepository<CandidateMultiTask, Long> {
    List<CandidateMultiTask> findAllByCandidateId(Long id);

}
