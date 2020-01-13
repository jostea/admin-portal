package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.ReportDTO;
import com.internship.adminpanel.service.ReportsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportsRestController {
    private final ReportsService reportsService;

    @GetMapping("/all")
    public ResponseEntity<List<ReportDTO>> getAll(Authentication authentication) {
        log.info("User '" + authentication + "' get all reports");
        return new ResponseEntity<>(reportsService.getAllReports(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<ReportDTO>> getByEmail(@PathVariable("email") String email, Authentication authentication) {
        log.info("User '" + authentication + "' get all reports specified by email '" + email + "'");
        return new ResponseEntity<>(reportsService.getAllReportsByEmail(email), HttpStatus.OK);
    }
}


