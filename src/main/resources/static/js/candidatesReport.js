$(document).ready(function () {
    let url_string = window.location.href.split('/');
    let id = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/testreport/report/" + id,
        success: function (response) {
            fillSkillsTables(response.skillsResultsDTO.skillGroupAnswers, "Technical");
            fillSkillsTables(response.skillsResultsDTO.skillGroupAnswers, "Tool");
            fillSkillsTables(response.skillsResultsDTO.skillGroupAnswers, "Soft");
            fillSkillsTables(response.skillsResultsDTO.skillGroupAnswers, "Language");
            fillSingleChoiceTables(response.singleChoiceResultsDTO.singleChoiceAnswers);
            fillMultiChoiceTable(response.multiChoiceResultsDTO.multiChoiceAnswers);
            fillCustomChoiceTable(response.customChoiceResultsDTO.customAnswerDTOList);
            fillSQLTaskTable(response.sqlResultsDTO.sqlAnswers);
            fillCodeTaskTable(response.codeTasksResultsDTO.codeSingleTaskDTOList);
        }
    })
    ;
});

function fillSkillsTables(skillList, skillType) {
    let tbody = "";
    for (let i = 0; i < skillList.length; i++) {
        if (skillList[i].skillType === skillType) {
            for (let j = 0; j < skillList[i].skillCandidateList.length; j++) {
                tbody += "<tr>";
                tbody += "<td>" + (j + 1) + "</td>";
                tbody += `<td>${skillList[i].skillCandidateList[j].skillName}</td>`;
                tbody += `<td>${skillList[i].skillCandidateList[j].level}</td>`;
                tbody += "</tr>";
                $("#tbodyFor" + skillType + "Skills").html(tbody);
            }
        }
    }
}

function fillSingleChoiceTables(singleChoices) {
    let tbody = "";
    for (let i = 0; i < singleChoices.length; i++) {
        tbody += `<div class="singleChoiceClass"><h4 class="questionClass">${singleChoices[i].textSingleChoiceQuestion}</h4><hr>`;
        if (singleChoices[i].idSelectedAO === singleChoices[i].idCorrectAO) {
            tbody += `<h5 id="selectedAOid${i}" hidden></h5>
                      <label class="checkbox-inline answerCorrectClass"><input type="checkbox" checked>${singleChoices[i].selectedAnswerOptionText}</label>`;
        }
        if (singleChoices[i].idSelectedAO !== singleChoices[i].idCorrectAO) {
            tbody += `<h5 id="selectedAOid${i}" hidden></h5>
                       <label class="checkbox-inline answerWrongClass"><input type="checkbox" checked>${singleChoices[i].selectedAnswerOptionText}</label>
                        <label class="checkbox-inline answerCorrectClass"><input type="checkbox"> ${singleChoices[i].correctAnswerOptionText}</label>`;
        }
        tbody += `</div>`;
    }
    $("#singleChoiceSector").html(tbody);
}

function fillMultiChoiceTable(multiChoices) {
    let tbody = ``;
    for (let i = 0; i < multiChoices.length; i++) {
        tbody += `<div class="multiChoiceClass"><h4 class="questionClass">${multiChoices[i].textMultiChoiceQuestion}</h4><hr>`;
        for (let j = 0; j < multiChoices[i].candidateAnswerOptions.length; j++) {
            if (multiChoices[i].candidateAnswerOptions[j].selected === true
                && multiChoices[i].candidateAnswerOptions[j].correct === true) {
                tbody += `<lable class="checkbox-inline answerCorrectClass"><input type="checkbox" checked="true"> 
                        ${multiChoices[i].candidateAnswerOptions[j].answerOptionText}</lable>`;
            }
            if (multiChoices[i].candidateAnswerOptions[j].selected === true
                && multiChoices[i].candidateAnswerOptions[j].correct === false) {
                tbody += `<label class="checkbox-inline answerWrongClass"><input type="checkbox" checked="true"> 
                    ${multiChoices[i].candidateAnswerOptions[j].answerOptionText}</label>`;
            }
            if (multiChoices[i].candidateAnswerOptions[j].selected === false
                && multiChoices[i].candidateAnswerOptions[j].correct === true) {
                tbody += `<label class="checkbox-inline answerCorrectClass"><input type="checkbox"> ${multiChoices[i].candidateAnswerOptions[j].answerOptionText}</label>`;
            }
        }
        tbody += `</div>`;
    }
    $("#multiChoiceSector").html(tbody);
}

function fillCustomChoiceTable(customChoice) {
    let tbody = ``;
    for (let i = 0; i < customChoice.length; i++) {
        tbody += `<div class="customChoiceClass">`;
        tbody += `<input hidden id="idCandidateCustomTask${i}" value="${customChoice[i].idCandidateCustomTask}">`;
        tbody += `<h4 class="questionClass">${customChoice[i].customTaskText}</h4><hr>`;
        tbody += `<textarea class="customAnswer" id="messageCustom${i}" rows="4" cols="70">${customChoice[i].candidateCustomAnswer}</textarea><hr>`;
        if (customChoice[i].correct === true) {
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxCustomQuestion${i}"><input type="checkbox" id="checkboxCustomQuestion${i}" checked> Correct</label></div>`;
        } else {
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxCustomQuestion${i}"><input type="checkbox" id="checkboxCustomQuestion${i}"> Correct</label></div>`;
        }
        tbody += `</div>`;
    }
    $("#customQuestionSector").html(tbody);
}

function fillSQLTaskTable(sqlTask) {
    let tbody = ``;
    for (let i = 0; i < sqlTask.length; i++) {
        tbody += `<div class="sqlTaskClass"><input id="idCandidateSqlTask${i}" hidden value="${sqlTask[i].idCandidateSqlTask}">`;
        tbody += `<h4 class="questionClass">${sqlTask[i].textQuestion}</h4><hr>`;
        tbody += `<div class="container-fluid"> 
                       <div class="row">
                        <div class="col-sm-1"></div>
                            <div class="col-sm-4 sqlStatements"><h6 class="text-center">Expected Answer</h6><hr><h5>${sqlTask[i].correctStatement}</h5></div>
                            <div class="col-sm-2"></div>
                            <div class="col-sm-4 sqlStatements"><h6 class="text-center">Candidate Answer</h6><hr><h5>${sqlTask[i].candidateStatement}</h5></div>
                            <div class="col-sm-1"></div>
                        </div>
                  </div>`;
        tbody += `<h5 class="titleMessage">Message</h5><textarea id="messageSQL${i}" class="taskMessage" rows="4" cols="100">${sqlTask[i].message}</textarea><hr>`;
        if (sqlTask[i].correct === true) {
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxSqlTask${i}"><input type="checkbox" id="checkboxSqlTask${i}" checked> Correct</label></div>`;
        } else {
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxSqlTask${i}"><input type="checkbox" id="checkboxSqlTask${i}"> Correct</label></div>`;
        }
        tbody += `</div>`;
    }

    $("#SQLTaskSector").html(tbody);
}

function fillCodeTaskTable(codeTask) {
    let tbody = ``;
    for (let i = 0; i < codeTask.length; i++) {
        tbody += `<div class="codeTaskClass"><input id="codeTaskId${i}" hidden value="${codeTask[i].codeTaskId}">`;
        tbody += `<h5 class="titleMessage">Code Provided</h5><textarea  class="taskMessage" rows="10" cols="100">${codeTask[i].codeProvided}</textarea><hr>`;
        tbody += `<h5 class="titleMessage">Message</h5><textarea id="messageCode${i}" class="taskMessage" rows="4" cols="100">${codeTask[i].message}</textarea><hr>`;
        if (codeTask[i].isCorrect === true || codeTask[i].isCorrect !== null) {
            tbody += `<h5 id="rtOfCorrectness">Rate of correctness: ${codeTask[i].rateCorrectness}</h5>`;
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxCodeTask${i}"><input type="checkbox" id="checkboxCodeTask${i}" checked> Correct</label></div>`;
        } else {
            tbody += `<h5 id="rtOfCorrectness">Rate of correctness: ${codeTask[i].rateCorrectness}</h5>`;
            tbody += `<div><label class="checkbox-inline" style="margin-left: 5px" for="checkboxCodeTask${i}"><input type="checkbox" id="checkboxCodeTask${i}"> Correct</label></div>`;
        }
        tbody += `</div>`;
    }
    $("#CodeTaskSector").html(tbody);
}

function getStatements() {
    let url_string = window.location.href.split('/');
    let candidateId = url_string[url_string.length - 1];

    let customArray = [];
    $("#customQuestionSector").children()
        .each(function (index, el) {
            if ($(this).hasClass("customChoiceClass")) {
                if ($("#checkboxCustomQuestion" + index).is(":checked") === true) {
                    customArray.push(createCustomObject(true, $("#messageCustom" + index).val(), $("#idCandidateCustomTask" + index).val()));
                } else {
                    customArray.push(createCustomObject(false, $("#messageCustom" + index).val(), $("#idCandidateCustomTask" + index).val()));
                }
            }
        });

    let sqlArray = [];
    $("#SQLTaskSector").children()
        .each(function (index, el) {
            if ($(this).hasClass("sqlTaskClass")) {
                if ($("#checkboxSqlTask" + index).is(":checked") === true) {
                    sqlArray.push(createSqlObject(true, $("#messageSQL" + index).val(), $("#idCandidateSqlTask" + index).val()));
                } else {
                    sqlArray.push(createSqlObject(false, $("#messageSQL" + index).val(), $("#idCandidateSqlTask" + index).val()));
                }
            }
        });

    let codeArray = [];
    $("#CodeTaskSector").children()
        .each(function (index, el) {
            if ($(this).hasClass("codeTaskClass")) {
                if ($("#checkboxCodeTask" + index).is(":checked") === true) {
                    codeArray.push(createCodeObject(true, $("#messageCode" + index).val(), $("#codeTaskId" + index).val()));
                } else {
                    codeArray.push(createCodeObject(false, $("#messageCode" + index).val(), $("#codeTaskId" + index).val()));
                }

            }
        });

    return {
        candidateId: candidateId,
        customReport: customArray,
        sqlReport: sqlArray,
        codeReport: codeArray
    }
}

function createCodeObject(isCorrect, message, codeTaskId) {
    return {
        message: message,
        correct: isCorrect,
        codeTaskId: codeTaskId
    }
}

function createCustomObject(isCorrect, message, idCandidateCustomTask) {
    return {
        message: message,
        correct: isCorrect,
        idCandidateCustomTask: idCandidateCustomTask
    }
}

function createSqlObject(isCorrect, message, idCandidateSqlTask) {
    return {
        message: message,
        correct: isCorrect,
        idCandidateSqlTask: idCandidateSqlTask
    }
}

function saveReport() {
    getStatements();
    window.location.href = gOptions.aws_path + "/reportsView";
}