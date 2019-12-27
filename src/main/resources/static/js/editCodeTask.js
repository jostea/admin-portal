$(document).ready(function () {
    let url_string = window.location.href.split('/');
    let taskId = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/tasksrest/singleCode/" + taskId,
        success: function (response) {
            fillCodeInputs(response);
        }
    });
});

function fillCodeInputs(data) {
    $("#title").val(data.title);
    $("#descripiton").val(data.description);
    $("#complexity").val(data.complexity.toUpperCase());
    $("#technology").val(data.technology.toUpperCase());
    populateStreamsTable(data.streams);
    if (data.enabled) {
        $("#isEnabledCode").checked = true;
    }
    $("#themeEditor").val(data.signature);
}

function populateStreamsTable(data) {
    let tbody = "";
    for (let i=0; i<data.length; i++) {
        tbody += "<tr>";
        tbody += "<input type='hidden' value =" + data[i].id + ">";
        tbody += "<input type='hidden' value =" + data[i].disciplineId + ">";
        tbody += "<td>" + data[i].disciplineName + "</td>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + "<button class='removeDiscStream btn btn-danger'>Remove</button>" + "</td>";
        tbody += "</tr>";
    }
    $("#disc_streams_selected tbody").html(tbody);
}

$("#disc_streams_selected").on('click', '.removeDiscStream', function x() {
    $(this).closest('tr').remove();
});

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
    let listStreamIds = [];
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

function getStreamsByDiscipline(discname) {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/streamView/streams/discipline/" + discname,      //TODO: recheck
        //new url: RECHECK it!!!
        success: function (response) {
            populateStreamsDropdown(response);
        },
        error: function (response) {
            showPopUp("Error", "task could not be saved " + response.responseText);
        }
    });
}

function prepareDataForTask() {
    return {
        title: $("#title").val(),
        description: $("#descripiton").val(),
        complexity: $("#complexity").val(),
        technology: $("#technology").val(),
        isEnabled: document.getElementById("isEnabledCode").checked,
        signature: cleanSignature($("#themeEditor").val()),
        streams: getStreamIds(),
        answersSubmit: listCorrectCodes()
    }
}

function cleanSignature(signature) {
    if (signature.length>1) {
        signature = signature.replace(/\s\s+/g, ' ');
        signature = signature.replace(/\s*,\s*/g, ",");
        signature = signature.replace(/\(\s*\s*/, '(');
        signature = signature.replace(/\s*\)\s*/, ')');
    }
    return signature;
}

function listCorrectCodes() {
    let codes = [];
    let responses = [];
    for (let i=0; i<$('#correctAnswersTable tbody')[0].children.length; i++) {
        for (let j=0; j<$('#correctAnswersTable tbody')[0].children[0].children.length-2; j++) {
            responses.push(
                {
                    type: $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.split(" ").shift().substring(1, $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.split(" ").shift().length-1),
                    value: $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.split(" ").pop()
                }
            );
        }
        codes.push(
            {
                input: responses,
                output: {
                    type: $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.split(" ").shift().substring(1, $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.split(" ").shift().length-1),
                    value: $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.split(" ").pop()
                }
            }
        );
        responses = [];
    }
    return codes;
}