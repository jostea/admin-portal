package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Candidate;
import com.internship.adminpanel.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findCandidateByStream(Stream stream);
}
