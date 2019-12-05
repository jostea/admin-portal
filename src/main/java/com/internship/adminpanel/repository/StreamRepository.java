package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {
    List<Stream> findStreamByNameContainingIgnoreCase(String name);
}

