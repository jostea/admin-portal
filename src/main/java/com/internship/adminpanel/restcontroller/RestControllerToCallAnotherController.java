package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.skill.SkillDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calledController")
public class RestControllerToCallAnotherController {

    @GetMapping("/getSkillById/{id}")
    public ResponseEntity<SkillDTO> callAnotherController(@PathVariable("id") Long id) {
        final String uri = "http://localhost:8080/devadmin/skillsView/skill/" + id;
        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
        return restTemplate.getForEntity(uri, SkillDTO.class);
    }
}
