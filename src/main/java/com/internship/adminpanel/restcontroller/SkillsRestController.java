package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.exception.SkillNotFound;
import com.internship.adminpanel.model.dto.skill.SkillDTO;
import com.internship.adminpanel.model.dto.skill.SkillDTOFromUI;
import com.internship.adminpanel.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.adminpanel.service.SkillsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/skillsView")
public class SkillsRestController {

    private final SkillsService skillsService;

    @GetMapping("/skill/{id}")
    public ResponseEntity<SkillDTO> findById(@PathVariable("id") Long id, Authentication authentication) {
        try {
            return new ResponseEntity<>(skillsService.findById(id), HttpStatus.OK);
        } catch (SkillNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' found skill with id '" + id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SkillDTO>> findAll(Authentication authentication) {
        return new ResponseEntity<>(skillsService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody SkillDTOFromUI skillDTOFromUI, Authentication authentication) {
        try {
            skillsService.add(skillDTOFromUI);
            log.info("User '" + authentication.getName() + "' add new skill '" + skillDTOFromUI.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyName e) {
            log.error("Error when user '" + authentication.getName() + "' add skill with empty name;", e);
            return new ResponseEntity<>("Stream name is required", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            log.error("Error when user '" + authentication.getName() + "' add  skill; error message: ", e);
            return new ResponseEntity<>("Skill '" + skillDTOFromUI.getName() + "' already exist", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' add new skill;", e);
            return new ResponseEntity<>("The specified skill cannot be applied.", HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody SkillDTOFromUI skillDTOFromUI,
                                         Authentication authentication) {
        try {
            skillsService.update(id, skillDTOFromUI);
            log.info("User '" + authentication.getName() + "' update skill with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SkillNotFound e) {
            log.error("Error when user '" + authentication.getName() + "'  update skill by id " + id, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EmptyName e) {
            log.error("Error when user '" + authentication.getName() + "'  update skill by id " + id + " ;\nerror message:", e);
            return new ResponseEntity<>("Skill name is required", HttpStatus.BAD_REQUEST);
        } catch (TransactionSystemException e) {
            log.error("Error when user '" + authentication.getName() + "' edit new  skill;", e);
            return new ResponseEntity<>("The specified skill cannot be applied.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, Authentication authentication) {
        try {
            skillsService.delete(id);
            log.info("User '" + authentication.getName() + "' deleted skill with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SkillNotFound e) {
            log.error("Error when user '" + authentication.getName() + "'  delete skill by id " + id, e);
            return new ResponseEntity<>("Couldn't delete skill", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/skillsforStream/{stream_id}")
    public ResponseEntity<List<SkillsSpecifiedByStreamDTO>> getSkillSpecifiedByStreamId(@PathVariable("stream_id") Long streamId
            , Authentication authentication) {
        return new ResponseEntity<>(skillsService.getSkillForSpecifiedStream(streamId), HttpStatus.OK);
    }
}
