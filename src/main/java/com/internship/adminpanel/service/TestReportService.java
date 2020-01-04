package com.internship.adminpanel.service;

import com.internship.adminpanel.model.*;
import com.internship.adminpanel.model.dto.test_report.*;
import com.internship.adminpanel.model.enums.SkillsTypeEnum;
import com.internship.adminpanel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestReportService {

    private final CandidateRepository candidateRepository;
    private final TaskRepository taskRepository;

    public CandidateTestReportDTO getCandidateTestResults(Long candidateId) {
        CandidateTestReportDTO candidateTestReport = new CandidateTestReportDTO();
        Candidate candidate = candidateRepository.getOne(candidateId);

        candidateTestReport.setCandidateId(candidate.getId());
        candidateTestReport.setSkillsResultsDTO(new SkillsResultsDTO(getSkillsAnswers(candidate)));
        candidateTestReport.setSingleChoiceResultsDTO(new SingleChoiceResultsDTO(getAllSingleChoiceAnswers(candidate)));
        candidateTestReport.setMultiChoiceResultsDTO(new MultiChoiceResultsDTO(getAllMultiChoiceAnswers(candidate)));
        candidateTestReport.setCustomChoiceResultsDTO(new CustomChoiceResultsDTO(getCustomAnswers(candidate)));
        candidateTestReport.setSqlResultsDTO(new SqlTasksResultsDTO(getSqlAnswers(candidate)));
        candidateTestReport.setCodeTasksResultsDTO(new CodeTasksResultsDTO(getCodeAnswers(candidate)));

        return candidateTestReport;
    }

    //region Getting Candidate Skills
    private List<SkillGroupAnswerDTO> getSkillsAnswers(Candidate candidate) {
        List<SkillGroupAnswerDTO> listSkillGroups;
        List<CandidateSkill> candSkills = candidate.getCandidateSkills();
        listSkillGroups = processCandidateSkills(candSkills);
        return listSkillGroups;
    }

    private List<SkillGroupAnswerDTO> processCandidateSkills(List<CandidateSkill> candSkills) {
        List<SkillGroupAnswerDTO> listSkillGroupsProcessed = new ArrayList<>();

        List<SkillCandidateDTO> listTechnical = new ArrayList<>();
        List<SkillCandidateDTO> listSoft = new ArrayList<>();
        List<SkillCandidateDTO> listTool = new ArrayList<>();
        List<SkillCandidateDTO> listLanguage = new ArrayList<>();

        for (CandidateSkill candSkill : candSkills) {
            SkillCandidateDTO skillCandidateDTO = new SkillCandidateDTO();
            skillCandidateDTO.setSkillName(candSkill.getSkill().getName());
            skillCandidateDTO.setLevel(candSkill.getLevel());
            switch (candSkill.getSkill().getSkillType()) {
                case TECHNICAL:
                    listTechnical.add(skillCandidateDTO);
                    break;
                case SOFT:
                    listSoft.add(skillCandidateDTO);
                    break;
                case TOOL:
                    listTool.add(skillCandidateDTO);
                    break;
                case LANGUAGE:
                    listLanguage.add(skillCandidateDTO);
                    break;
            }
        }

        listSkillGroupsProcessed.add(new SkillGroupAnswerDTO(SkillsTypeEnum.TECHNICAL.getType(), listTechnical));
        listSkillGroupsProcessed.add(new SkillGroupAnswerDTO(SkillsTypeEnum.SOFT.getType(), listSoft));
        listSkillGroupsProcessed.add(new SkillGroupAnswerDTO(SkillsTypeEnum.TOOL.getType(), listTool));
        listSkillGroupsProcessed.add(new SkillGroupAnswerDTO(SkillsTypeEnum.LANGUAGE.getType(), listLanguage));

        return listSkillGroupsProcessed;
    }

    //endregion

    private List<CodeSingleTaskDTO> getCodeAnswers(Candidate candidate) {
        List<CandidateCodeTask> codeSingleTask = candidate.getCandidateCodeTasks();
        List<CodeSingleTaskDTO> codeTaskAnswers = new ArrayList<>();
        for (CandidateCodeTask candidateCodeTask:codeSingleTask) {
            codeTaskAnswers.add(new CodeSingleTaskDTO(candidateCodeTask));
        }
        return codeTaskAnswers;
    }


    //region Getting Sql Answers
    private List<SqlAnswerDTO> getSqlAnswers(Candidate candidate) {
        List<SqlAnswerDTO> sqlAnswers = new ArrayList<>();
        if (candidate != null) {
            List<CandidateSqlTask> candidateSqlTasks = candidate.getCandidateSqlTasks();
            for (CandidateSqlTask candSqlTask : candidateSqlTasks) {
                SqlAnswerDTO sqlAnswer = new SqlAnswerDTO(candSqlTask);
                sqlAnswers.add(sqlAnswer);
            }
        }
        return sqlAnswers;
    }
    //endregion

    //region Getting Custom Answers
    private List<CustomAnswerDTO> getCustomAnswers(Candidate candidate) {
        List<CustomAnswerDTO> customAnswers = new ArrayList<>();
        List<CandidateCustomTask> candidateCustomTasks = candidate.getCandidateCustomTasks();
        if (!candidateCustomTasks.isEmpty()) {
            for (CandidateCustomTask candTask : candidateCustomTasks) {
                CustomAnswerDTO customAnswer = new CustomAnswerDTO(candTask);
                customAnswers.add(customAnswer);
            }
        }
        return customAnswers;
    }
    //endregion

    //region Getting Multi Choice Candidate Results
    private List<MultiChoiceAnswerDTO> getAllMultiChoiceAnswers(Candidate candidate) {
        List<MultiChoiceAnswerDTO> candidateMultiChoiceAnswers = new ArrayList<>();
        List<CandidateMultiTask> candidateMultiTasks = candidate.getCandidateMultiTasks();

        for (CandidateMultiTask multiTask : candidateMultiTasks) {
            MultiChoiceAnswerDTO multiChoiceAnswerDTO = new MultiChoiceAnswerDTO();
            multiChoiceAnswerDTO.setTextMultiChoiceQuestion(multiTask.getTask().getDescription());
            multiChoiceAnswerDTO.setCandidateAnswerOptions(getCandidateTaskAnswerOptions(multiTask));
            candidateMultiChoiceAnswers.add(multiChoiceAnswerDTO);
        }

        return candidateMultiChoiceAnswers;
    }

    private List<CandidateAnswerOption> getCandidateTaskAnswerOptions(CandidateMultiTask multiTask) {
        Task multiTaskFromDB = taskRepository.getOne(multiTask.getTask().getId());

        //all Answer Options related to this task
        List<AnswersOption> allTaskRelatedAOs = new ArrayList<>();
        if (multiTaskFromDB != null) {
            allTaskRelatedAOs = multiTaskFromDB.getAnswersOptions();
        }

        List<AnswersOption> candidateSelectedAO = multiTask.getAnswersOptions();
        List<CandidateAnswerOption> candAOprocessed = new ArrayList<>();

        //create list of Candidate processed Answer Options
        for (AnswersOption ao : allTaskRelatedAOs) {
            CandidateAnswerOption candAO = new CandidateAnswerOption();
            candAO.setAnswerOptionText(ao.getAnswerOptionValue());
            candAO.setCorrect(ao.isCorrect());
            Optional<AnswersOption> optionalAO = candidateSelectedAO
                    .stream()
                    .filter(a -> a.getId().equals(ao.getId()))
                    .findFirst();

            if (optionalAO.isPresent()) {
                candAO.setSelected(true);
            } else {
                candAO.setSelected(false);
            }
            candAOprocessed.add(candAO);
        }
        return candAOprocessed;
    }
    //endregion

    //region Getting Single Choice Answers Results
    private List<SingleChoiceAnswerDTO> getAllSingleChoiceAnswers(Candidate candidate) {
        List<SingleChoiceAnswerDTO> result = new ArrayList<>();
        List<CandidateSingleTask> candidateSingleTasks = candidate.getCandidateSingleTasks();

        for (CandidateSingleTask candidateSingleTask : candidateSingleTasks) {
            if (candidateSingleTask != null) {
                SingleChoiceAnswerDTO singleChoiceAnswer = getSingleChoiceAnswer(candidateSingleTask);
                result.add(singleChoiceAnswer);
            }
        }
        return result;
    }

    private SingleChoiceAnswerDTO getSingleChoiceAnswer(CandidateSingleTask candidateSingleTask) {
        SingleChoiceAnswerDTO singleChoiceAnswer = new SingleChoiceAnswerDTO(candidateSingleTask);
        Optional<AnswersOption> correctOptionOpt = candidateSingleTask
                .getTask()
                .getAnswersOptions()
                .stream()
                .filter(ao -> ao.isCorrect())
                .findFirst();
        if (correctOptionOpt.isPresent()) {
            singleChoiceAnswer.setIdCorrectAO(correctOptionOpt.get().getId());
            singleChoiceAnswer.setCorrectAnswerOptionText(correctOptionOpt.get().getAnswerOptionValue());
            if (singleChoiceAnswer.getIdCorrectAO().equals(singleChoiceAnswer.getIdSelectedAO())) {
                singleChoiceAnswer.setCorrect(true);
            } else {
                singleChoiceAnswer.setCorrect(false);
            }
        }
        return singleChoiceAnswer;
    }
    //endregion

}
