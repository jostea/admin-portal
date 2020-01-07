$(document).ready(function () {
    $("#spinner").hide();
    let url_string = window.location.href.split('/');
    let id = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/testreport/report/" + id,
        success: function (response) {
            console.log(response);
            drawReport(response);
        }
    })
    ;
});

function drawReport(data) {
    $("#candidateName").html(data.candidateName);
    $("#reEvaluateTestToken").val(data.candidateToken);
    drawSkills(data.skillsResultsDTO.skillGroupAnswers);
    drawSingleChoice(data.singleChoiceResultsDTO.singleChoiceAnswers);
    drawMultiChoice(data.multiChoiceResultsDTO.multiChoiceAnswers);
    drawCustomQuestion(data.customChoiceResultsDTO.customAnswerDTOList);
    drawSqlQuestion(data.sqlResultsDTO.sqlAnswers);
    drawCodeQuestion(data.codeTasksResultsDTO.codeSingleTaskDTOList);
}

function drawCodeQuestion(data) {
    let body = "";
    for (let i=0; i<data.length; i++) {
        if (data[i].isCorrect) {
            body += "<div class='panel panel-default correct'>";
        } else {
            body += "<div class='panel panel-default wrong'>";
        }
        body+="<div class='panel-body'>";
        body+="<h4>" + data[i].taskMessage + "</h4>";
        body+="<h6><strong>Type:</strong> Code Task</h6>";
        body += "<div class='panel panel-default'>";
        body+="<div class='panel-heading'><h5>Candidate's submitted code</h5></div>";
        body+="<div class='panel-body'>";
        body+="<h5 class='text-mute'><samp style='white-space: pre-wrap;'>" + data[i].codeProvided + "</samp></h5>";
        body+="</div>";
        body+="</div>";
        body+="<input hidden value='" + data[i].id + "'>";
        if (data[i].isCorrect) {
            body+="<h6>Is correct: " + data[i].isCorrect + " <button class='btn btn-danger btn-xs updateCode'>Set false</button></h6>";
        } else {
            body+="<h6>Is correct: " + data[i].isCorrect + " <button class='btn btn-success btn-xs updateCode'>Set correct</button></h6>";
        }
        if (data[i].rateCorrectness === 1) {
            body+="<h6 class='text-success'>Rate of correctness: " + data[i].rateCorrectness + "</h6>";
            body+="<h6 class='text-success'>Message: " + data[i].message + "</h6>";
        } else if (data[i].rateCorrectness > 0.6) {
            body+="<h6 class='text-warning'>Rate of correctness: " + data[i].rateCorrectness + "</h6>";
            body+="<h6 class='text-warning'>Message: " + data[i].message + "</h6>";
        } else {
            body+="<h6 class='text-danger'>Rate of correctness: " + data[i].rateCorrectness + "</h6>";
            body+="<h6 class='text-danger'>Message: " + data[i].message + "</h6>";
        }
        body+="</div>";
        body+="</div>";
        body+="</div>";
        body+="</div>";
    }
    $("#codeTask").html(body);
}

function drawSqlQuestion(data) {
    let body = "";
    for (let i=0; i<data.length; i++) {
        if (data[i].correct) {
            body += "<div class='panel panel-default sqlQuestionPanel correct'>";
        } else {
            body += "<div class='panel panel-default sqlQuestionPanel wrong'>";
        }
        body+="<div class='panel-body'>";
        body+="<h4><strong>Sql Task:</strong> " + data[i].textQuestion + "</h4>";
        body+="<h5><strong>Processing message: </strong>" + data[i].message + "</h5>"
        body+="<h6><strong>Type:</strong> Sql Task</h6>";
        body += "<div class='panel panel-default'>";
        body+="<div class='panel-heading' style='white-space: pre-wrap;'><h5>Correct statement</h5></div>";
        body+="<div class='panel-body'>";
        body+="<h5 class='text-success' style='white-space: pre-wrap;'><samp>" + data[i].correctStatement + "</samp></h5>";
        body+="</div>";
        body+="</div>";
        body += "<div class='panel panel-default'>";
        body+="<div class='panel-heading'><h5>Candidate's statement</h5></div>";
        body+="<div class='panel-body'>";
        body+="<h5 class='text-muted'><samp>" + data[i].candidateStatement + "</samp></h5>";
        body+="<input hidden value='" + data[i].idCandidateSqlTask + "'>";
        if (data[i].correct) {
            body+="<h6>Is correct: " + data[i].correct + " <button class='btn btn-danger btn-xs updateSql'>Set false</button></h6>";
        } else {
            body+="<h6>Is correct: " + data[i].correct + " <button class='btn btn-success btn-xs updateSql'>Set correct</button></h6>";
        }
        body+="</div>";
        body+="</div>";
        body+="</div>";
        body+="</div>";
    }
    $("#sqlTask").html(body);
}

function drawCustomQuestion(data) {
    let body = "";
    for (let i=0; i<data.length; i++) {
        if (data[i].correct) {
            body += "<div class='panel panel-default correct'>";
        } else {
            body += "<div class='panel panel-default wrong'>";
        }
        body+="<div class='panel-body'>";
        body+="<h4><strong>Question:</strong> " + data[i].customTaskText + "</h4>";
        body+="<h6><strong>Type:</strong> Custom question</h6>";
        if (!data[i].correct) {
            body+="<h5><strong>Candidate's answer:</strong></h5>";
            body+="<input hidden value='" + data[i].idCandidateCustomTask + "'>";
            body+="<h5 class='text-danger'>" + data[i].candidateCustomAnswer + " <button class='btn btn-success btn-xs updateCustom'>Set correct</button></h5>";
        } else {
            body+="<h5><strong>Candidate's answer:</strong></h5>";
            body+="<input hidden value='" + data[i].idCandidateCustomTask + "'>";
            body+="<h5 class='text-success'>" + data[i].candidateCustomAnswer + " <button class='btn btn-danger btn-xs updateCustom'>Set false</button></h5>";
            body+="<h6>Info: this answer has been evaluated to be true earlier.</h6>"
        }
        body+="</div>";
        body+="</div>";
    }
    $("#customTask").html(body);
}

function drawSkills(data) {
    if (data.length === 0) {
        $("#skillsDivFragment").html("<p><i>No skills provided</i></p>");
    } else {
        for (let i=0; i<data.length; i++) {
            let tbody = "";
            if (data[i].skillCandidateList.length>0) {
                for (let j = 0; j < data[i].skillCandidateList.length; j++) {
                    tbody += "<tr>";
                    tbody += "<td>" + data[i].skillCandidateList[j].skillName + "</td>";
                    let table = $("#" + data[i].skillType + "Table");
                    for (let k = 1; k < table[0].tHead.children[0].cells.length; k++) {
                        if (data[i].skillCandidateList[j].level !== table[0].tHead.children[0].cells[k].innerText) {
                            tbody += "<td>-</td>";
                        } else {
                            tbody += "<td>" + data[i].skillCandidateList[j].level + "</td>";
                        }
                    }
                }
            } else {
                tbody = "<p><i>Candidate has not selected any " + data[i].skillType + " skills</i></p>"
            }
            $("#" + data[i].skillType + "Table tbody").html(tbody);
        }
    }
}

function drawSingleChoice(data) {
    let body  = "";
    for (let i=0; i<data.length; i++) {
        body += "<div class='panel panel-default'>";
        body+="<div class='panel-body'>";
        body+="<h4><strong>Task:</strong> " + data[i].textSingleChoiceQuestion + "</h4>";
        body+="<h6><strong>Type:</strong> Single Choice</h6>";
        if (data[i].correct) {
            body+="<h5>Answer option selected by candidate</h5>";
            body+="<div class='alert alert-success' role='alert'>" + data[i].selectedAnswerOptionText + "</div>";
        } else {
            body+="<h6>Selected answer</h6>";
            body+="<div class='alert alert-danger' role='alert'>" + data[i].selectedAnswerOptionText + "</div>";
        }
        body+="<h5>All answer options</h5>";
        body+="<ul class='list-group answersList'>";
        for (let j=0; j<data[i].allAnswerOptions.length; j++) {
            if (data[i].allAnswerOptions[j].correct) {
                body+="<li class='list-group-item'>" + data[i].allAnswerOptions[j].answer + "<span class='badge'>Correct</span></li>";
            } else {
                body+="<li class='list-group-item'>" + data[i].allAnswerOptions[j].answer + "</li>";
            }
        }
        body+="</ul>";
        body+="</div>";
        body+="</div>";
    }
    $("#singleTasks").html(body);
}

function drawMultiChoice(data) {
    let body  = "";
    for (let i=0; i<data.length; i++) {
        body += "<div class='panel panel-default'>";
        body+="<div class='panel-body'>";
        body+="<h4><strong>Task:</strong> " + data[i].textMultiChoiceQuestion + "</h4>";
        body+="<h6><strong>Type:</strong> Multi Choice</h6>";
        body+="<h5>Answers selected by candidate</h5>";
        for (let j=0; j<data[i].candidateAnswerOptions.length; j++) {
            if (data[i].candidateAnswerOptions[j].selected) {
                if (data[i].candidateAnswerOptions[j].correct) {
                    body+="<div class='alert alert-success' role='alert'>" + data[i].candidateAnswerOptions[j].answerOptionText + "</div>";
                } else {
                    body+="<div class='alert alert-danger' role='alert'>" + data[i].candidateAnswerOptions[j].answerOptionText + "</div>";
                }
            }
        }
        body+="<h5>All answer options</h5>";
        body+="<ul class='list-group answersList'>";
        for (let j=0; j<data[i].candidateAnswerOptions.length; j++) {
            if (data[i].candidateAnswerOptions[j].correct) {
                body+="<li class='list-group-item'>" + data[i].candidateAnswerOptions[j].answerOptionText + "<span class='badge'>Correct</span></li>";
            } else {
                body+="<li class='list-group-item'>" + data[i].candidateAnswerOptions[j].answerOptionText + "</li>";
            }
        }
        body+="</ul>";
        body+="</div>";
        body+="</div>";
    }
    $("#multiTask").html(body);
}

$(".container").on("click", ".updateSql", function () {
    let el = this.parentNode.parentNode.parentNode.children[1];
    let id = el.querySelector("input").value;
    $('#confirmSqlUpdateModal').modal('show');
    $(".container").on("click", "#updateSqlConfirmButton", function () {
        if ($("#sqlTaskMessageProvided").val()) {
            $('#confirmSqlUpdateModal').modal('hide');
            $.ajax({
                method: "PUT",
                url: gOptions.aws_path + "/testreport/updateSqlState",
                data: JSON.stringify({id: id, message: $("#sqlTaskMessageProvided").val(), type: "sql"}),
                contentType: "application/json",
                success: function () {
                    window.location.reload();
                }
            });
        } else {
            $("#updateSqlMessage").show();
        }
    });
});

$(".container").on("click", ".updateCode", function () {
    let el = this.parentNode.parentNode.parentNode;
    let id = el.querySelector("input").value;
    $('#confirmCodeUpdateModal').modal('show');
    $(".container").on("click", "#updateCodeConfirmButton", function () {
        if ($("#codeTaskMessageProvided").val()) {
            $('#confirmCodeUpdateModal').modal('hide');
            $.ajax({
                method: "PUT",
                url: gOptions.aws_path + "/testreport/updateCodeState",
                data: JSON.stringify({id: id, message: $("#codeTaskMessageProvided").val(), type: "code"}),
                contentType: "application/json",
                success: function () {
                    window.location.reload();
                }
            });
        } else {
            $("#updateCodeMessage").show();
        }
    });
});

$(".container").on("click", ".updateCustom", function () {
    let el = this.parentNode.parentNode.parentNode;
    let id = el.querySelector("input").value;
    $('#confirmCustomUpdateModal').modal('show');
    $(".container").on("click", "#updateCustomConfirmButton", function () {
        $('#confirmCustomUpdateModal').modal('hide');
        $.ajax({
            method: "PUT",
            url: gOptions.aws_path + "/testreport/updateCustomState",
            data: JSON.stringify({id: id, type: "custom"}),
            contentType: "application/json",
            success: function () {
                window.location.reload();
            }
        });
    });
});

$("#reEvaluateTest").on("click", function () {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/testreport/reEvaluateCandidate/" + $("#reEvaluateTestToken").val(),
        beforeSend: function() {
            $("#spinner").show();
            $("#mainDiv").hide();
        },
        success: function () {
            window.location.reload();
        },
        error: function() {

        },
        complete: function () {
            $("#spinner").hide();
            $("#mainDiv").show();
        }
    });
});

