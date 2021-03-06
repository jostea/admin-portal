$(document).ready(getTestStructure());
$(document).ready(getTimeForTest());
$(document).ready(function () {
    let elTaskType = document.getElementById("testTypeId");
    let selectedTaskType = elTaskType.options[elTaskType.selectedIndex].text;
    let elComplexity = document.getElementById("complexityId");
    let selectedComplexity = elComplexity.options[elComplexity.selectedIndex].text;
    getNrStreamSpecificTasks(selectedTaskType, selectedComplexity, 'badgeNr');
});


function getTimeForTest() {
    $("#alertTestStructure").html("");
    let url_string = window.location.href.split('/');
    let streamId = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/testStructure/{stream_id}/streamtime/" + streamId,
        success: function (response) {
            viewTime(response);
        }
    });
}

function viewTime(data) {
    let inputTime = "";
    inputTime += `<label for="inputTimeId">Time for test in minute:</label>
                <input id="inputTimeId" value="` + data.time + `">
                <button id="addTimeTest"  onclick="setTimeForTest();">Set Time</button>`;
    $("#testTime").html(inputTime);
}

function setTimeForTest() {
    let url_string = window.location.href.split('/');
    let streamId = url_string[url_string.length - 1];
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/testStructure/{stream_id}/setTime/" + streamId,
        data: JSON.stringify($("#inputTimeId").val()),
        contentType: "application/json",
        success: function () {
            getTimeForTest();
            $("#pop-up-message").html("<p>The time has been set to " + $("#inputTimeId").val() + " minutes</p>");
            $("#simple-pop-up").modal("show");
        }, error(response) {
            $("#alertTestStructure").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function getTestStructure() {
    $("#alertTestStructure").html("");
    let url_string = window.location.href.split('/');
    let streamId = url_string[url_string.length - 1];
    fillAddForm();
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/streamView/streams/" + streamId,
        success: function (response) {
            $("#testTitle").html(`<h3>Test Structure for ${response.name} ${response.disciplineName}</h3>`);
            fillTestTable(response.testStructures);
        }
    });
}

function fillTestTable(data) {
    let tableBody = "";
    if (data != null)
        for (let i = 0; i < data.length; i++) {
            tableBody += "<tr>";
            tableBody += `<td id="taskTypeTable` + i + `">${data[i].taskType}</td>`;
            tableBody += `<td id="complexityTable` + i + `">${data[i].complexity}</td>`;
            tableBody += `<td id="idOfNrOfQuestions` + i + `">${data[i].nrQuestions}</td>`;
            tableBody += `<td><p><span id="badgeNr` + i + `" class="badge" style="background-color: red; visibility: hidden">0</span></p></td>`;
            tableBody += `<td id="spaceForConfirmUpdate` + i + `"><button class="btn" onclick="updateStructureTest(${i},${data[i].id});">Update</button></td>`;
            tableBody += `<td><button class="btn" onclick="confirmDeleteStructure(${data[i].id})" data-toggle="modal" data-target="#deleteStructure">Delete</button></td>`;
            tableBody += "</tr>";
        }
    $("#testStructureTable tbody").html(tableBody);
}

let testStructureIdForDelete = '';

function confirmDeleteStructure(structureId) {
    testStructureIdForDelete = structureId;
}

$("#confirm-delete-structure-button").on("click", function () {
    deleteStructure(testStructureIdForDelete);
});

function deleteStructure(structureId) {
    $.ajax({
        method: "DELETE",
        url: gOptions.aws_path + "/testStructure/{stream_id}/delete/" + structureId,
        success: function () {
            getTestStructure();
        }, error(response) {
            $("#alertTestStructure").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function updateStructureTest(index, testStructureId) {

    let taskType = $("#taskTypeTable" + index).text();
    let complexity = $("#complexityTable" + index).text();
    document.getElementById('badgeNr' + index).style.visibility = "visible";
    getNrStreamSpecificTasks(taskType, complexity, 'badgeNr' + index);

    let oldAmountOfQuestions = $('#idOfNrOfQuestions' + index).text();
    $("#idOfNrOfQuestions" + index).html(`<input id="idOfInputNrQuestion` + index + `" value="${oldAmountOfQuestions}">`);

    $('#spaceForConfirmUpdate' + index).html(`<button class="btn" onclick="confirmUpdate(${index},${testStructureId})">Confirm</button>
                    <button class="btn" onclick="closeConfirmation(${index},${oldAmountOfQuestions},${testStructureId})">Close</button>`);
}

function closeConfirmation(index, oldAmountOfQuestions, testStructureId) {
    document.getElementById('badgeNr' + index).style.visibility = "hidden";
    let rollBackQuestionsNr = '';
    rollBackQuestionsNr += `${oldAmountOfQuestions}`;
    $('#idOfNrOfQuestions' + index).html(rollBackQuestionsNr);

    $('#spaceForConfirmUpdate' + index).html(`<button class="btn" onclick="updateStructureTest(${index},${testStructureId});">Update</button>`);
    $("#alertTestStructure").html("");
}

function confirmUpdate(index, testStructureId) {
    let oldAmountQuestions = $("#idOfInputNrQuestion" + index).val();
    if (oldAmountQuestions > $('#badgeNr' + index).val()) {
        $("#alertTestStructure").html(`<div class="alert alert-danger" role="alert"><p>` + "You cannot set up more questions then exist in DB" + `</p></div>`);
        document.getElementById("idOfInputNrQuestion" + index).style.border="2px solid red";
        document.getElementById("idOfInputNrQuestion" + index).style.color="red";
        document.getElementById("idOfInputNrQuestion" + index).style.fontWeight="bold";
    } else {
        document.getElementById('badgeNr' + index).style.visibility = "hidden";
        document.getElementById("idOfInputNrQuestion" + index).style="inherit";
        $("#alertTestStructure").html("");
        $.ajax({
            method: "PUT",
            url: gOptions.aws_path + "/testStructure/{stream_id}/update/" + testStructureId,
            data: JSON.stringify(prepareDataForUpdateStructureData(index)),
            contentType: "application/json",
            success: function () {
                $('#idOfNrOfQuestions' + index).html(`${oldAmountQuestions}`);
                $('#spaceForConfirmUpdate' + index).html(`<button class="btn" onclick="updateStructureTest(${index},${testStructureId});">Update</button>`);
            }, error(response) {
            }
        });
    }
}

function addStructureOfTest() {
    if ($("#numberOfQuestionId").val() > $("#badgeNr").val()) {
        $("#alertTestStructure").html(`<div class="alert alert-danger" role="alert"><p>` + "Number of questions is incorrect!" + `</p></div>`);
    } else {
        $.ajax({
            method: "POST",
            url: gOptions.aws_path + "/testStructure/{stream_id}/add",
            data: JSON.stringify(prepareDataForAddStructure()),
            contentType: "application/json",
            success: function () {
                getTestStructure();
            }, error(response) {
                $("#alertTestStructure").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
            }
        });
    }
}

function fillAddForm() {
    let form = "";
    form += `<div class="form-row row">
        <div class="col-md-4">
        <label for="testTypeId">Task type: </label><select id="testTypeId" class="form-control">
                    <option>Multi Choice Question</option>
                    <option>Single Choice Question</option>
                    <option>Custom Question</option>
                    <option>SQL question</option>
                    <option>Code question</option>
                </select>
                </div>
                <div class="col-md-4">
                    <label for="complexityId">Complexity: </label><select id="complexityId" class="form-control">
                    <option>Easy</option>
                    <option>Medium</option>
                    <option>Hard</option>
                </select>
                </div>
                <div class="col-md-2">
                    <label for="numberOfQuestionId">Number of questions: </label><input id="numberOfQuestionId"
                                                                                        value="1">
                                                                                        </div>
               <div class="col-md-1">
               <br>                                                                         
                    <button type="button" class="btn" onclick="addStructureOfTest();"> Add</button>
                </div>`;
    $("#divAddTestStructure").html(form);
}

function prepareDataForAddStructure() {
    let url_string = window.location.href.split('/');
    let streamId = url_string[url_string.length - 1];
    return {
        streamId: streamId,
        taskType: $("#testTypeId").val(),
        complexity: $("#complexityId").val(),
        nrQuestions: $("#numberOfQuestionId").val()
    }
}

function prepareDataForUpdateStructureData(id) {
    let url_string = window.location.href.split('/');
    let streamId = url_string[url_string.length - 1];
    return {
        streamId: streamId,
        taskType: $("#taskTypeTable" + id).text(),
        complexity: $("#complexityTable" + id).text(),
        nrQuestions: $("#idOfInputNrQuestion" + id).val()
    }
}

function getNrStreamSpecificTasks(selectedTaskType, selectedComplexity, badgeId) {
    let url_string = window.location.href.split('/');
    let idStream = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        data: {
            streamId: idStream,
            taskType: selectedTaskType,
            complexity: selectedComplexity
        },
        url: gOptions.aws_path + "/tasksrest/streamTypeComplexityTasks",
        success: function (response) {
            $("#" + badgeId).text(response);
            $("#" + badgeId).val(response);
            if(response == 0){
                document.getElementById(badgeId).style.backgroundColor="red";
            } else {
                document.getElementById(badgeId).style.backgroundColor="green";
            }
        }
    });
}

$("#testTypeId").on('change', function () {
    let elTaskType = document.getElementById("testTypeId");
    let selectedTaskType = elTaskType.options[elTaskType.selectedIndex].text;
    let elComplexity = document.getElementById("complexityId");
    let selectedComplexity = elComplexity.options[elComplexity.selectedIndex].text;
    getNrStreamSpecificTasks(selectedTaskType, selectedComplexity, 'badgeNr');
});

$("#complexityId").on('change', function () {
    let elTaskType = document.getElementById("testTypeId");
    let selectedTaskType = elTaskType.options[elTaskType.selectedIndex].text;
    let elComplexity = document.getElementById("complexityId");
    let selectedComplexity = elComplexity.options[elComplexity.selectedIndex].text;
    getNrStreamSpecificTasks(selectedTaskType, selectedComplexity, 'badgeNr');
});


