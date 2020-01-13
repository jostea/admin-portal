package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.set_false.ChangeCorrectnessDTO;
import com.internship.adminpanel.model.dto.test_report.CandidateTestReportDTO;
import com.internship.adminpanel.service.TestReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/testreport")
public class TestReportController {

    private final TestReportService testReportService;

    @GetMapping(value = "/report/{id}")
    public ResponseEntity<?> startTest(@PathVariable("id") Long id) {
        try {
            CandidateTestReportDTO candidateTestReportDTO = testReportService.getCandidateTestResults(id);
            log.info("Test report for candidate with id {} was provided", id);
            return new ResponseEntity<>(candidateTestReportDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while getting test results for the candidate with id" + id, e);
            return new ResponseEntity("Error while getting test report", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateSqlState")
    public ResponseEntity<?> updateSqlState(@RequestBody ChangeCorrectnessDTO changeCorrectnessDTO) {
        try {
            testReportService.updateAnswerState(changeCorrectnessDTO);
            return new ResponseEntity<>("Sql task successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateCodeState")
    public ResponseEntity<?> updateCodeState(@RequestBody ChangeCorrectnessDTO changeCorrectnessDTO) {
        try {
            testReportService.updateAnswerState(changeCorrectnessDTO);
            return new ResponseEntity<>("Sql task successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateCustomState")
    public ResponseEntity<?> updateCustomState(@RequestBody ChangeCorrectnessDTO changeCorrectnessDTO) {
        try {
            testReportService.updateAnswerState(changeCorrectnessDTO);
            return new ResponseEntity<>("Task has been updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reEvaluateCandidate/{token}")
    public ResponseEntity<?> reEvaluateCandidate(@PathVariable String token) {
        try {
            testReportService.reEvaluateCandidate(token);
            return new ResponseEntity<>("Test re-evaluated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not re-evaluate", HttpStatus.BAD_REQUEST);
        }
    }
}
