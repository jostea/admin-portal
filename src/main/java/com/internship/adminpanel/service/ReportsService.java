package com.internship.adminpanel.service;

import com.internship.adminpanel.model.Candidate;
import com.internship.adminpanel.model.dto.ReportDTO;
import com.internship.adminpanel.model.enums.TestStatusEnum;
import com.internship.adminpanel.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {

    private final CandidateRepository candidateRepository;

    public List<ReportDTO> getAllReports() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<ReportDTO> reports = new ArrayList<>();
        for (Candidate candidate : candidates) {
            if (TestStatusEnum.TEST_FINISHED.getType().equalsIgnoreCase(candidate.getTestStatus().getType()))
                reports.add(ReportDTO.builder()
                        .streamId(candidate.getStream().getId())
                        .stream(candidate.getStream().getName())
                        .discipline(candidate.getStream().getDiscipline().getName())
                        .candidateId(candidate.getId())
                        .name(candidate.getFirstName() + " " + candidate.getLastName())
                        .email(candidate.getEmail())
                        .build());
        }
        return reports;
    }

    public List<ReportDTO> getAllReportsByEmail(String email) {
        List<ReportDTO> reports = new ArrayList<>();
        for (Candidate candidate : candidateRepository.findCandidateByEmailContainingIgnoreCase(email)) {
            if (TestStatusEnum.TEST_FINISHED.getType().equalsIgnoreCase(candidate.getTestStatus().getType()))
                reports.add(ReportDTO.builder()
                        .streamId(candidate.getStream().getId())
                        .stream(candidate.getStream().getName())
                        .discipline(candidate.getStream().getDiscipline().getName())
                        .candidateId(candidate.getId())
                        .name(candidate.getFirstName() + " " + candidate.getLastName())
                        .email(candidate.getEmail())
                        .build());
        }
        return reports;
    }
}
