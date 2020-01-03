package com.internship.adminpanel.service;

import com.internship.adminpanel.model.Candidate;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.ReportDTO;
import com.internship.adminpanel.model.enums.TestStatusEnum;
import com.internship.adminpanel.repository.CandidateRepository;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ReportsServiceTest {
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ReportsService reportsService;


    @Test
    public void shouldReturnedAllReports() {
        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .stream(Stream.builder()
                        .id(1L)
                        .name("Java")
                        .discipline(Discipline.
                                builder()
                                .name("Developer")
                                .build())
                        .build())
                .firstName("Muhtar")
                .lastName("Verbliud")
                .email("Doggy@email.com")
                .testStatus(TestStatusEnum.TEST_FINISHED)
                .build();
        List<Candidate> mockedList = new ArrayList<>();
        mockedList.add(mockedCandidate);
        ReportDTO reportDTO = ReportDTO.builder()
                .candidateId(1L)
                .streamId(1L)
                .stream("Java")
                .discipline("Developer")
                .name("Muhtar Verbliud")
                .email("Doggy@email.com")
                .build();
        List<ReportDTO> expectedList = new ArrayList<>();
        expectedList.add(reportDTO);
        when(candidateRepository.findAll()).thenReturn(mockedList);
        List<ReportDTO> returnedResult = reportsService.getAllReports();
        verify(candidateRepository).findAll();
        assertThat(expectedList).isEqualTo(returnedResult);
    }

    @Test
    public void shouldReturnedAllReportsSpecifiedByEmail() {
        Candidate mockedCandidate = Candidate.builder()
                .id(1L)
                .stream(Stream.builder()
                        .id(1L)
                        .name("Java")
                        .discipline(Discipline.
                                builder()
                                .name("Developer")
                                .build())
                        .build())
                .firstName("Muhtar")
                .lastName("Verbliud")
                .email("Doggy@email.com")
                .testStatus(TestStatusEnum.TEST_FINISHED)
                .build();

        List<Candidate> mockedList = new ArrayList<>();
        mockedList.add(mockedCandidate);

        ReportDTO reportDTO = ReportDTO.builder()
                .candidateId(1L)
                .streamId(1L)
                .stream("Java")
                .discipline("Developer")
                .name("Muhtar Verbliud")
                .email("Doggy@email.com")
                .build();
        List<ReportDTO> expectedList = new ArrayList<>();
        expectedList.add(reportDTO);

        when(candidateRepository.findCandidateByEmailContainingIgnoreCase("Dogg")).thenReturn(mockedList);
        List<ReportDTO> returnedResult = reportsService.getAllReportsByEmail("Dogg");
        verify(candidateRepository).findCandidateByEmailContainingIgnoreCase("Dogg");
        assertThat(expectedList).isEqualTo(returnedResult);
    }
}
