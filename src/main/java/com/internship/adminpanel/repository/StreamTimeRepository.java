package com.internship.adminpanel.repository;

import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.StreamTime;
import jdk.nashorn.internal.ir.Optimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamTimeRepository extends JpaRepository<StreamTime, Long> {
    StreamTime findStreamTimeByStream(Stream stream);
    void deleteByStreamId(Long id);
}
