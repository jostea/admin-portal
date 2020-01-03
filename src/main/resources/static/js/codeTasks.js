let accessModifier = "";
let keyword = "";
let returnType = "";
let methodName = "";
let args = [];
const rets = ["int", "double", "char", "Integer", "Double", "Char", "String", "Number", "Object"];
let readingSignature = true;

$(document).ready(function () {
    let url_string = window.location.href.split('/');
    let taskId = url_string[url_string.length - 1];
    if(!isNaN(taskId) && taskId) {
        $.ajax({
            method: "GET",
            url: gOptions.aws_path + "/tasksrest/singleCode/" + taskId,
            success: function (response) {
                fillCodeInputs(response);
            }
        });
    } else {
        $("#submitTask").hide();
        $(".allargs").hide();
    }
});

function populateStreamsTable(data) {
    let tbody = "";
    for (let i=0; i<data.length; i++) {
        tbody += "<tr>";
        tbody += "<input type='hidden' value =" + data[i].id + ">";
        tbody += "<td>" + data[i].disciplineName + "</td>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + "<button class='removeDiscStream btn btn-danger'>Remove</button>" + "</td>";
        tbody += "</tr>";
    }
    $("#disc_streams_selected tbody").html(tbody);
}

function fillCodeInputs(data) {
    $("#title").val(data.title);
    $("#descripiton").val(data.description);
    $("#complexity").val(data.complexity.toUpperCase());
    $("#technology").val(data.technology.toUpperCase());
    populateStreamsTable(data.streams);
    if (data.enabled) {
        document.getElementById("isEnabledCode").checked = true;
    }
    $("#themeEditor").val(data.signature);
    validateSignature(data.signature);
    drawInput(data.correctCodes);
}

function drawInput(data) {
    let tbody = "";
    let thead = "";
    let div = "";
    for (let i = 0; i < args.length; i++) {
        div+="<div class='input-group'>";
        div+="<span class='input-group-addon' id='basic-addon3'>" + "(" + prepareHTML(args[i].type) + ") " + args[i].name + ": </span>";
        div+="<input type='text' class='form-control' id='arg" + i + "' aria-describedby='basic-addon3'>";
        div+="</div>";
    }
    div+="<div class='input-group'>";
    div+="<span class='input-group-addon' id='basic-addon3'>(" + prepareHTML(returnType) + ") Given input, method must return: </span>";
    div+="<input type='text' class='form-control' id='correctReturn' aria-describedby='basic-addon3'>";
    div+="</div>";
    div+="<div>";
    div+="<button id='submitCorrectAnswer' class='btn btn-primary'>Add answer</button>";
    div+="</div>";
    $("#introduceParamsDiv").html(div);
    $(".allargs").show();

    thead += "<tr>";
    for (let j = 0; j<data.length; j++) {
        tbody+="<tr>";
        for (let i = 0; i < args.length; i++) {
            tbody+="<td>" + "(" + prepareHTML(args[i].type) + ") " + data[j].input.split(";")[i] + "</td>";
        }
        tbody+="<td>("+ prepareHTML(returnType) + ") " + data[j].output + "</td>";
        tbody+="<td><button id='disableAnswer' class='btn btn-danger'>Cancel Answer</button></td>";
        tbody+="</tr>";
    }
    for (let i = 0; i < args.length; i++) {
        thead += "<th>" + prepareHTML(args[i].type) + " "+ args[i].name + "</th>";
    }
    thead += "<th>" + prepareHTML(returnType) + " - return type " +"</th>";
    thead += "<th>Cancel Answer</th>";
    tbody+="</tr>";
    thead+="</tr>";
    $("#correctAnswersTable tbody").html(tbody);
    $("#correctAnswersTable thead").html(thead);
}

$("#submitSignature").on("click", function drawInput() {
    readingSignature = true;
    if (validateSignature($("#themeEditor").val())) {
        let div = "";
        for (let i = 0; i < args.length; i++) {
            div+="<div class='input-group'>";
            div+="<span class='input-group-addon' id='basic-addon3'>" + "(" + prepareHTML(args[i].type) + ") " + args[i].name + ": </span>";
            div+="<input type='text' class='form-control' id='arg" + i + "' aria-describedby='basic-addon3'>";
            div+="</div>";
        }
        div+="<div class='input-group'>";
        div+="<span class='input-group-addon' id='basic-addon3'>(" + prepareHTML(returnType) + ") Given input, method must return: </span>";
        div+="<input type='text' class='form-control' id='correctReturn' aria-describedby='basic-addon3'>";
        div+="</div>";
        div+="<div>";
        div+="<button id='submitCorrectAnswer' class='btn btn-primary'>Add answer</button>";
        div+="</div>";
        $("#introduceParamsDiv").html(div);
        $(".allargs").show();
    } else {
        cleanParams();
    }
});

$("#introduceParamsDiv").on("click", "#submitCorrectAnswer", function () {
    let tbody = $("#correctAnswersTable tbody").html();
    let thead = "";
    thead += "<tr>";
    tbody+="<tr>";
    for (let i = 0; i < args.length; i++) {
        tbody+="<td>" + "(" + prepareHTML(args[i].type) + ") " + $("#arg"+ i + "").val() + "</td>";
        thead += "<th>" + prepareHTML(args[i].type) + " "+ args[i].name + "</th>";
    }
    tbody+="<td>("+ prepareHTML(returnType) + ") " + $("#correctReturn").val() + "</td>";
    thead += "<th>" + prepareHTML(returnType) + " - return type " +"</th>";
    tbody+="<td><button id='disableAnswer' class='btn btn-danger'>Cancel Answer</button></td>";
    thead += "<th>Cancel Answer</th>";
    tbody+="</tr>";
    thead+="</tr>";
    $("#correctAnswersTable tbody").html(tbody);
    $("#correctAnswersTable thead").html(thead);
    $("#submitTask").show();
    for (let i=0; i<args.length; i++) {
        document.getElementById("arg" + i + "").value = "";
    }
    document.getElementById("correctReturn").value = "";
});

$("#correctAnswersTable").on("click", "#disableAnswer", function () {
    $(this).closest('tr').remove();
});

function cleanParams() {
    args.length=0;
    keyword = "";
    methodName = "";
    returnType = "";
    readingSignature = true;
}

$("#submitTaskEdit").on("click", function () {
    if (ajaxSubmitValidation() && validateSignature($("#themeEditor").val())) {
        let url_string = window.location.href.split('/');
        let taskId = url_string[url_string.length - 1];
        $.ajax({
            method: "POST",
            url: gOptions.aws_path + "/tasksrest/editCodeTask/" + taskId,
            data: JSON.stringify(prepareDataForTask()),
            contentType: "application/json",
            success: function () {
                window.location.replace(gOptions.aws_path + "/tasks/");
            },
            error: function (response) {
                showPopUp("Error", response.responseText);
            }
        });
    }
});

$("#submitTask").on("click", function () {
    if (ajaxSubmitValidation() && validateSignature($("#themeEditor").val())) {
        $.ajax({
            method: "POST",
            url: gOptions.aws_path + "/tasksrest/addCodeTask",
            data: JSON.stringify(prepareDataForTask()),
            contentType: "application/json",
            success: function () {
                window.location.replace(gOptions.aws_path + "/tasks/");
            },
            error: function (response) {
                showPopUp("Error", response.responseText);
            }
        });
    }
});

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
    $('#disc_streams_selected').find('tbody').find('tr').each(function () {
        let currentStreamId = $(this).find('input[type="hidden"]').eq(0).val();
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

function listCorrectCodes() {
    let codes = [];
    let responses = [];
    for (let i=0; i<$('#correctAnswersTable tbody')[0].children.length; i++) {
        for (let j=0; j<$('#correctAnswersTable tbody')[0].children[0].children.length-2; j++) {
            let type = $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.split(" ").shift().substring(1, $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.split(" ").shift().length-1)
            let value = $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.substring(type.length+3, $('#correctAnswersTable tbody')[0].children[i].children[j].innerText.length);
            responses.push(
                {
                    type: type,
                    value: value
                }
            );
        }
        let retType = $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.split(" ").shift().substring(1, $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.split(" ").shift().length-1);
        let retVal = $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.substring(retType.length+3, $('#correctAnswersTable tbody')["0"].children[i].children[$('#correctAnswersTable tbody')["0"].children[i].children.length-2].innerText.length);
        codes.push(
            {
                input: responses,
                output: {
                    type: retType,
                    value: retVal
                }
            }
        );
        responses = [];
    }
    return codes;
}

function showPopUp(title, message) {
    $("#myModal").find('h4').text(title);
    $("#myModal").find('h3').text(message);
    $("#myModal").modal('show');
}

function noRepetitionForArgs(args) {
    for (var i = 0; i < args.length; i++) {
        for(var j=0; j<args.length; j++) {
            for(var k=0; k<args.length; k++) {
                if(args[j].name === args[k].name && j!==k) {
                    alert("Different Arguments cannot have the same name");
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

function validateSignature(signature) {
    if (signature.split("(").length>1 && signature.split(" ").length>=3) {
        signature = cleanSignature(signature);
        let splittedArgs = signature.split("(")[1].split(")")[0];
        signature = signature.split("(")[0];
        let splitted = signature.split(" ");
        readArgs(splittedArgs);
        if (threeArgsValidation(splitted) && readingSignature) {
            keyword = splitted[0];
            returnType = prepareHTML(splitted[1]);
            methodName = splitted[2];
            return true;
        } else if (fourArgsValidation(splitted) && readingSignature) {
            accessModifier = splitted[0];
            keyword = splitted[1];
            returnType = prepareHTML(splitted[2]);
            methodName = splitted[3];
            return true;
        } else {
            cleanParams();
        }
    }
    alert("invalid signature");
    return false;
}

function returnTypes(ret) {
    if (rets.indexOf(ret)>=0 || validateSimpleGenerics(ret) || validateArray(ret) || validateDoubleGenerics(ret)) {
        return true;
    }
    return false;
}

function simpleGeneric(input) {
    var params = ["List", "ArrayList", "Set", "HashSet", "Collection", "TreeSet"];
    if (params.indexOf(input)!==-1) {
        return true;
    }
    return false;
}

function validateSimpleGenerics(input) {
    let holder = input.split("<")[0];
    let type = customSplitForSimpleGenerics(input);
    if (!(input.length===1 && returnTypes(input))) {
        if(!simpleGeneric(holder)) {
            return false;
        } else {
            if(returnTypes(type)) {
                return true;
            }
        }
    }
}

function customSplitForSimpleGenerics(input) {
    var arr = input.split("");
    var finalStr = "";
    var concat = false;
    for(var i=0; i<arr.length; i++) {
        if(arr[i]==="<" && !concat) {
            concat = true;
            arr.pop();
            arr.splice(i, 1);
        }
        if (concat) {
            finalStr+=arr[i];
        }
    }
    return finalStr;
}

function validateMethodName(name) {
    return name !== "main" && !returnTypes(name) && isNaN(name.charAt(0)) && /^\w+$/.test(name);

}

function threeArgsValidation(sign) {
    if (sign.length===3) {
        if (sign[0]==="static") {
            if (returnTypes(sign[1])) {
                if (validateMethodName(sign[2])) {
                    return true
                }
            }
        }
    }
    return false;
}

function fourArgsValidation(sign) {
    if (sign.length===4) {
        if (sign[0]==="public") {
            if (sign[1]==="static") {
                if (returnTypes(sign[2])) {
                    if (validateMethodName(sign[3])) {
                        return true;
                    }
                }
            }
        }
    }
    return false;
}

function prepareHTML(splitElement) {
    splitElement = splitElement.replace(/>/g, '&gt');
    splitElement = splitElement.replace(/</g, '&lt');
    return splitElement;
}

function readArgs(holder) {
    if (holder.split(" ").length-1 === parse(holder).length) {
        args.length = 0;
        for (let i=0; i<parse(holder).length; i++) {
            if (returnTypes(parse(holder)[i].split(" ")[0]) && validateArgsName(parse(holder)[i].split(" ")[1])) {
                args.push({
                    type: parse(holder)[i].split(" ")[0],
                    name: parse(holder)[i].split(" ")[1]
                });
            } else {
                cleanParams();
                readingSignature = false;
            }
        }
    } else {
        cleanParams();
        readingSignature = false;
    }
    if (!noRepetitionForArgs(args)) {
        cleanParams();
        readingSignature = false;
    }
}
function validateArgsName(name) {
    return isNaN(name.charAt(0)) && /^\w+$/.test(name);
}

function validateArray(val) {
    val = val.replace(/\s*\[\s*/g, "\[");
    let isValid = true;
    if (rets.indexOf(val.split("[")[0])!==-1 && val.split("[").length>1) {
        let brackets = val.split(val.split("[")[0])[1];
        for(let i=0; i<brackets.length; i+=2) {
            if (!(brackets.split("")[i] === "[" && brackets.split("")[i+1] === "]")) {
                isValid = false;
            }
        }
    } else {
        isValid = false;
    }
    return isValid;
}

function parse(str) {
    let result = [], item = '', depth = 0;
    function push() {
        if (item) {
            result.push(item);
        }
        item = '';
    }
    for (let i = 0, c; c = str[i], i < str.length; i++) {
        if (!depth && c === ',') {
            push();
        }
        else {
            item += c;
            if (c === '<') depth++;
            if (c === '>') depth--;
        }
    }
    push();
    return result;
}

function validateDoubleGenerics(input) {
    let params = ["Map", "HashMap"];
    let cont = "";
    let key = "";
    let value = "";
    if (params.indexOf(input.split("<")[0])===-1) {
        return false;
    } else if (params.indexOf(input.split("<")[0])!==1) {
        for (let i = input.split("<")[0].length+1; i<input.length-1; i++) {
            cont+=input.split("")[i];
        }
        key = cont.split(",")[0];
        value = cont.split(",")[1];
        if (returnTypes(key) && returnTypes(value) && cont.split(",").length === 2) {
            return true;
        }
    }
    return false;
}