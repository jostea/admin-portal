$(document).ready(function () {
    $("#correctAnswersTable").hide();
    $("#methodReturn").html(dataTypes());
    $("#ajaxCodeTask").hide();
    $("#addCodeTaskThirdPart").hide();
    $("#addCodeTaskFourthPart").hide();
});

$("#addArgs").on("click", function () {
    if(validateInputsSignature()) {
        let args = "";
        args+="<h3>Specify the data type and name for arguments</h3>";
        for(var i=0; i<$("#numberOfArgs").val(); i++) {
            args+="<form class='form-inline'>";
            args+="<h3>Argument #" + i + "</h3>";
            args+="<label for='argType" + i + "'>Data type : </label>";
            args+="<select class='form-control' id='argType" + i + "'>";
            args+=dataTypes();
            args+="</select>";
            args+="<label for='argName" + i + "'>;      Name : </label>";
            args+="<input class='form-control' id='argName" + i + "' type='text' required='required'>";
            args+="</form>";
        }
        args+="<button id='submitArgs' class='btn btn-default'>Submit arguments</button>"
        $("#addCodeTaskThirdPart").html(args);
        $("#addCodeTaskThirdPart").show();
    }
});

$("#addCodeTaskThirdPart").on("click", "#submitArgs", function () {
    if (validateInputsArgsName()) {
        let correctCode = "";
        correctCode+="<form class='form-inline'>";
        correctCode+="<h3>Input at least 2 correct answers</h3>"
        for (var i=0; i<$("#numberOfArgs").val(); i++) {
            correctCode+="<span id='helpBlock' class='help-block'>Give a value for " + $("#argName" + i + "").val() + "</span>";
            correctCode+="<label class='sr-only' for='argValue" + i + "'></label>";
            correctCode+="<input class='form-control' id='argValue" + i + "' type='text' required='required' placeholder='Input for " + $("#argType" + i).val() + " " + $("#argName" + i).val() + "'>";
        }
        correctCode+="<div class='has-warning'>";
        correctCode+="<span id='helpBlock' class='help-block'>Set the return for given values</span>";
        correctCode+="<label class='sr-only' for='argOutput'></label>";
        correctCode+="<input class='form-control' id='argOutput' type='text' required='required' placeholder='Expected Return'>";
        correctCode+="</div>";
        correctCode+="</form>";
        correctCode+="<button id='submitCorrectAnswer' class='btn btn-default'>Submit answer</button>";
        $("#insertAnswers").html(correctCode);
        $("#addCodeTaskFourthPart").show();
    }
});

$("#insertAnswers").on("click", "#submitCorrectAnswer", function () {
    let showSubmitted = $("#correctAnswersTable tbody").html();
    showSubmitted+="<tr>";
    showSubmitted+="<td>";
    for(var i=0; i<$("#numberOfArgs").val(); i++) {
        showSubmitted+=$("#argValue" + i + "").val();
        if (i<$("#numberOfArgs").val()-1) {
            showSubmitted+=", ";
        }
    }
    showSubmitted+="</td>";
    showSubmitted+="<td>" + $("#argOutput").val() + "</td>";
    showSubmitted+="<td><button id='cancelCorrectAnswer' class='btn btn-danger'>Cancel</button></td>";
    showSubmitted+="</tr>";
    $("#correctAnswersTable tbody").html(showSubmitted);
    $("#correctAnswersTable").show();
    $("#ajaxCodeTask").show();
    for(var i=0; i<$("#numberOfArgs").val(); i++) {
        document.getElementById("argValue" + i + "").value = "";
    }
    document.getElementById("argOutput").value = "";
});

function generateSignature() {
    let signature = "public static " + $("#methodReturn").val() + " " + $("#methodName").val() + "(";
    for(var i=0; i<$("#numberOfArgs").val(); i++) {
        signature+=$("#argType" + i).val() + " " + $("#argName" + i).val();
        if (i<$("#numberOfArgs").val()-1) {
            signature+=", ";
        }
    }
    signature+=");";
    return signature;
}

function prepareDataForTask() {
    return {
        title: $("#title").val(),
        description: $("#descripiton").val(),
        complexity: $("#complexity").val(),
        technology: $("#technology").val(),
        isEnabled: document.getElementById("isEnabledCode").checked,
        signature: generateSignature(),
        streams: getStreamIds(),
        correctCodes: listCorrectCodes()
    }
}

function getStreamsByDiscipline(discname) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/discipline/" + discname,      //TODO: recheck
        //new url: RECHECK it!!!
        success: function (response) {
            populateStreamsDropdown(response);
        },
        error: function (response) {
            showPopUp("Error", "task could not be saved " + response.responseText);
        }
    });
}

$("#addDiscStream").on("click", function (event) {
    event.preventDefault();

    // get data of the currently selected discipline in the dropdown
    let elDisc = document.getElementById("disciplines");
    let selectedDiscipline = elDisc.options[elDisc.selectedIndex].text;
    let selectedDisciplineId = elDisc.options[elDisc.selectedIndex].value;

    // get data of the currently selected stream in the dropdown
    let elStream = document.getElementById("streams");
    let selectedStream = elStream.options[elStream.selectedIndex].text;
    let selectedStreamId = elStream.options[elStream.selectedIndex].value;

    // If there are no already such combination of Discipline-Stream, then add to the view
    if (checkDiscAndStreams(selectedDiscipline, selectedStream) && selectedDisciplineId != '0') {
        let tbody = $("#disc_streams_selected tbody").html();
        tbody += "<tr>";
        tbody += "<input type='hidden' value =" + selectedDisciplineId + ">";
        tbody += "<input type='hidden' value =" + selectedStreamId + ">";
        tbody += "<td>" + selectedDiscipline + "</td>";
        tbody += "<td>" + selectedStream + "</td>";
        tbody += "<td>" + "<button class='removeDiscStream btn btn-danger'>Remove</button>" + "</td>";
        tbody += "</tr>";
        $("#disc_streams_selected tbody").html(tbody);
    }
});

function getStreamIds() {
    let listStreamIds = new Array();
    $('#disc_streams_selected').find('tbody').find('tr').each(function (i, el) {
        let currentStreamId = $(this).find('input[type="hidden"]').eq(1).val();
        listStreamIds.push(currentStreamId.valueOf());
    });
    return listStreamIds;
}

function checkDiscAndStreams(discText, streamText) {
    let result = true;
    $('#disc_streams_selected').find('tbody').find('tr').each(function (i, el) {
        let currentDiscRow = $(this).find('td').eq(0).text();       //discipline text
        let currentStreamRow = $(this).find('td').eq(1).text();     //stream text
        if (currentDiscRow === discText && currentStreamRow === streamText) {
            result = false;
            return false;
        }
    });

    return result;
}

function populateStreamsDropdown(data) {
    let tbody = $("#streams").html();
    tbody = "";

    for (let i = 0; i < data.length; i++) {
        tbody += "<option value=" + data[i].id + ">";
        tbody += data[i].name;
        tbody += "</option>";
    }
    $("#streams").html(tbody);
}

$("#disciplines").change(function (event) {
    event.preventDefault();

    //get selected discipline text
    let elDisc = document.getElementById("disciplines");
    let selectedDiscipline = elDisc.options[elDisc.selectedIndex].text;
    getStreamsByDiscipline(selectedDiscipline);
});

$("#disc_streams_selected").on('click', '.removeDiscStream', function x() {
    $(this).closest('tr').remove();
});

$("#addCodeTaskFourthPart").on("click", "#cancelCorrectAnswer", function () {
    $(this.closest('tr')).remove();
});

$("#ajaxCodeTask").on("click", function () {
    if (ajaxSubmitValidation()) {
        $.ajax({
            method: "POST",
            url: "/tasksrest/addCodeTask",
            data: JSON.stringify(prepareDataForTask()),
            contentType: "application/json",
            success: function () {
                window.location.href = "/tasks/";
            }
        });
    }
});

function listCorrectCodes() {
    let codes = [];
    for (var i=0; i<$('#correctAnswersTable tbody')[0].children.length; i++) {
        codes.push(
            {
                input: $('#correctAnswersTable tbody')["0"].children[i].children["0"].innerHTML,
                output: $('#correctAnswersTable tbody')["0"].children[i].children["1"].innerHTML
            }
        )
    }
    return codes;
}

function dataTypes() {
    let types = ["int", "double", "char", "float", "byte", "short", "long", "boolean", "String", "Object", "Integer", "Double", "Long", "Float", "Byte", "Custom"];
    let options = "";
    types.forEach(function (value) {
        options+="<option>" + value + "</option>";
    });
    return options;
}

function showPopUp(title, message) {
    $("#myModal").find('h4').text(title);
    $("#myModal").find('h3').text(message);
    $("#myModal").modal('show');
}

function hasNumber(myString) {
    return /\d/.test(myString);
}

function validateInputsSignature() {
    if($("#methodName").val() && !(/\s/.test($("#methodName").val()))) {
        if (!(hasNumber($("#methodName").val()))) {
            return true;
        } else {
            showPopUp("Invalid method name", "Method name cannot contain numbers");
        }
    } else {
        showPopUp("Invalid method name", "Method name cannot be empty or contain spaces");
    }
    return false;
}

function validateInputsArgsName() {
    for (var i = 0; i < $("#numberOfArgs").val(); i++) {
        if (!$("#argName" + i + "").val() || (/\s/.test($("#argName" + i + "").val()))) {
            showPopUp("Cannot submit", "Argument name cannot be empty or contain spaces");
            return false;
        }
        if (hasNumber($("#argName" + i + "").val())) {
            showPopUp("Cannot submit", "Argument name cannot contain numbers");
            return false;
        }
        for(var j=0; j<$("#numberOfArgs").val(); j++) {
            for(var k=0; k<$("#numberOfArgs").val(); k++) {
                if($("#argName"+j+"").val() === $("#argName"+k+"").val() && j!==k) {
                    showPopUp("Identical name", "Different Arguments cannot have the same name");
                    return false;
                }
            }
        }
    }
    return true;
}

function ajaxSubmitValidation() {
    if ($("#title").val()) {
        if ($('#disc_streams_selected tbody')[0].children.length>0) {
            if($('#correctAnswersTable tbody')["0"].children.length>=2) {
                return true;
            } else {
                showPopUp("I need answers", "please, provide at least two correct answers");
            }
        } else {
            showPopUp("No stream provided", "Please attach this task to a specific stream");
        }
    } else {
        showPopUp("No title", "Please provide a title for you task")
    }

    return false;
}