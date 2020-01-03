$(document).ready(function () {
    let url_string = window.location.href.split('/');
    let taskId = url_string[url_string.length - 1];
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/tasksrest/singleCode/" + taskId,
        success: function (response) {
            fillCodeSingle(response);
        }
    });
});

function fillCodeSingle(data) {
    let url_string = window.location.href.split('/');
    let taskId = url_string[url_string.length - 1];
    let header = "";
    let footer = "";
    header += "<input type='hidden' value='" + data.id + "'>";
    header += "<h2>View code task</h2>";
    header += "<h3 id='codeTaskTitle'><strong>Title: </strong> " + data.title + "</h3>";
    header += "<h5><strong>Description: </strong> " + data.description + "</h5>";
    header += "<h6 class='technologyH'>" + data.technology + ", " + data.complexity + "</h6>"
    if (data.enabled) {
        header += "<h5>This task is currently active <button class='btn btn-danger btn-xs' id='enableDisableButton'>Disable</button></h5>";
    } else {
        header += "<h5>This task is currently disabled <button class='btn btn-success btn-xs' id='enableDisableButton'>Enable</button></h5>";
    }
    header += "<a href='" + gOptions.aws_path + "/tasks/editCodeTask/" + taskId +"'>Edit this task</a>"
    footer += "<h6>Signature: <span class='codeContainer'>" + encodeSignatureHtml(data.signature) + "</span></h6>";
    footer += "<h6>Code tests:</h6>";
    footer += "<ul class='list-group'>";
    for (let i =0; i<data.correctCodes.length; i++) {
            footer += "<li class='list-group-item'><p><mark>Input:</mark> " + data.correctCodes[i].input + "</p><p><mark>Output:</mark> "+ data.correctCodes[i].output +"</p></li>"
    }
    footer += "</ul>";
    footer += "<h6>Attached streams:</h6>";
    for (let i =0; i<data.streams.length; i++) {
        footer += "<li class='list-group-item'>" + data.streams[i].disciplineName + " " + data.streams[i].name + "</li>"
    }
    $("#codeTaskHeader").html(header);
    $("#codeTaskFooter").html(footer);
}

function encodeSignatureHtml(signature) {
    signature = signature.replace(/>/g, '&gt');
    signature = signature.replace(/</g, '&lt');
    return signature;
}

$("#codeTaskHeader").on("click", "#enableDisableButton", function () {
    if (confirm("Are you sure you want to change this task's state?")) {
        let url_string = window.location.href.split('/');
        let taskId = url_string[url_string.length - 1];
        disableEnableCodeTask(taskId);
        window.location.reload();
    }
});

function disableEnableCodeTask(idTask) {
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/tasksrest/disableCodeTask/" + idTask,
        success: function () {
            $("#myModal").modal('show');
            async function sl() {
                window.location.href = gOptions.aws_path + "/tasks/";
            }
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}