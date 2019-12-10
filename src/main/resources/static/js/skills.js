$(document).ready(getAllSkills());

let skillIdOnDL = "";

function fillTableSkills(data) {
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + data[i].typeStr + "</td>";
        tbody += "<td>" + returnedNamesOfStream(data[i].streams) + "</td>";
        tbody += `<td><button type='button' class="btn btn-info" 
                    onclick='prepareForUpdate(${data[i].id},"${data[i].name}","${data[i].typeStr}")'
            data-target="#skillUpdate" data-toggle="modal">Edit</button></td>`;
        tbody += `<td><button type='button' class="btn" onclick="confirmDeleteSkill(${data[i].id})"
                    class='btn'    data-target="#deleteModalSkill"   data-toggle="modal">Delete</button></td>`;
        tbody += "</tr>";
    }
    $("#skillsTable tbody").html(tbody);
}

let flagSkillEdit;

function prepareForUpdate(skillId, skillOldName, skillType) {
    createExistTable();
    flagSkillEdit = skillId;
    $("#skillNewName").val(skillOldName);
    $("#NewTypeSkill").val(skillType);
    getAllStreamsForEditSkill();
    $.ajax({
        method: "GET",
        url: "/skillsView/skill/" + flagSkillEdit,
        success: function (response) {
            fillTableWithExistSkills(response.streams);
        }
    });
}

function createExistTable() {
    let body = "<form>\n" +
        "                            <table class=\"table\" id=\"skillsExist\">\n" +
        "                                <thead>\n" +
        "                                <tr>\n" +
        "                                    <th>Streams</th>\n" +
        "                                    <th>Discipline</th>\n" +
        "                                    <th>Remove</th>\n" +
        "                                </tr>\n" +
        "                                </thead>\n" +
        "                                <tbody id=\"tbodySkillOfExist\"><tr><input  type='hidden' value='0'></tr></tbody>\n" +
        "                            </table>";
    $("#div-update-table-selected-streams").html(body);
}

function fillTableWithExistSkills(data) {
    for (let i = 0; i < data.length; i++) {
        $('#tbodySkillOfExist tr:last').after(
            '<tr>' +
            `<input  type='hidden'  value='${data[i].id}'>` +
            "<td>" + data[i].name + "</td>" +
            "<td>" + data[i].disciplineName + "</td>" +
            "<td><button type='button' class='btn' onclick='deleteRowInSkillAddModal(this)'>Remove</button></td>" +
            '</tr>'
        );
    }
}

function returnedNamesOfStream(streams) {
    let str = "";
    for (let i = 0; i < streams.length; i++) {
        str += `<table>`;
        str += `<tr>`;
        str += `<td>` + streams[i].name + " - " + streams[i].disciplineName + `</td>`;
        str += `</tr>`;
        str += "</table>";
    }
    return str;
}

$("#confirm-delete-skill-button").on("click", function () {
    deleteSkill(skillIdOnDL);
});

function confirmDeleteSkill(par1) {
    skillIdOnDL = par1;
}

function deleteSkill(par) {
    $.ajax({
        method: "DELETE",
        url: "/skillsView/delete/" + par,
        success: function () {
            getAllSkills();
        }
    });
}

function getAllSkills() {
    $.ajax({
        method: "GET",
        url: "/skillsView/all",
        success: function (response) {
            fillTableSkills(response);
            $("#alertSkill").html("");
        }
    });
}

function addSkill() {
    $.ajax({
        method: "POST",
        url: "/skillsView/add",
        data: JSON.stringify(prepareDataForAddSkill()),
        contentType: "application/json",
        success: function () {
            $("#skillName").val("");
            getAllSkills();
            $("#tbodySkill").html("");
        }, error(response) {
            $("#alertSkill").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function updateSkill() {
    $.ajax({
        method: "PUT",
        url: "/skillsView/update/" + flagSkillEdit,
        data: JSON.stringify(prepareDataForUpdateSkill()),
        contentType: "application/json",
        success: function () {
            getAllSkills();
            $("#tbodySkillOfExist").html("");
        }
    });
}

function getAllStreamsForAddSkill() {
    $.ajax({
        method: "GET",
        url: "/discipline/disciplines",
        success: function (response) {
            createAddedTable();
            fillSelectorDiscipline(response);
            fillSelectorAddStreamForSkill();
        }
    });
}


function getAllStreamsForEditSkill() {
    $.ajax({
        method: "GET",
        url: "/discipline/disciplines",
        success: function (response) {
            fillSelectorEditDisciplineForSkill(response);
            fillSelectorEditStreamForSkill();
        }
    });
}



$("#disciplineForSkill").on("click", function () {
    fillSelectorAddStreamForSkill();
});

$("#newDisciplineForSkill").on("click", function () {
    fillSelectorEditStreamForSkill();
});

function fillSelectorDiscipline(data) {
    let option = "";
    for (let i = 0; i < data.length; i++) {
        option += "<option value=" + data[i].id + ">" + data[i].name + "</option>";
    }
    $("#disciplineForSkill").html(option);
}

function fillSelectorEditDisciplineForSkill(data) {
    let option = "";
    for (let i = 0; i < data.length; i++) {
        option += "<option value=" + data[i].id + ">" + data[i].name + "</option>";
    }
    $("#newDisciplineForSkill").html(option);
}

function fillSelectorEditStreamForSkill() {
    let option = "";
    $.ajax({
        method: "GET",
        url: "/streamView/streams",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                if (($("#newDisciplineForSkill").val()) === (response[i].disciplineId + "")) {
                    option += "<option value=" + response[i].id + ">" + response[i].name + "</option>";
                }
            }
            $("#newStreamsForSkill").html(option);
        }
    });
}

function fillSelectorAddStreamForSkill() {
    let option = "";
    $.ajax({
        method: "GET",
        url: "/streamView/streams",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                if (($("#disciplineForSkill").val()) === (response[i].disciplineId + "")) {
                    option += "<option value=" + response[i].id + ">" + response[i].name + "</option>";
                }
            }
            $("#streamsForSkill").html(option);
        }
    });
}

let streamIdForAdd = "";
$("#addStreamButton").on("click", function () {
    streamIdForAdd = $("#streamsForSkill").val();
    getIdOfAddedStreams(streamIdForAdd);
});

let streamIdForEdit = "";
$("#updateSkillButton").on("click", function () {
    streamIdForEdit = $("#newStreamsForSkill").val();
    getIdOfUpdatedStreams(streamIdForEdit);
});

function getIdOfAddedStreams(id) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/" + id,
        success: function (response) {
            fillTableWithAddedSkills(response);
        }
    });
}

function getIdOfUpdatedStreams(id) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/" + id,
        success: function (response) {
            fillTableWithUpdatedSkills(response);
        }
    });
}

function createAddedTable() {
    let body = "<form>\n" +
        "                            <table class=\"table\" id=\"skillsInserts\">\n" +
        "                                <thead>\n" +
        "                                <tr>\n" +
        "                                    <th>Stream</th>\n" +
        "                                    <th>Discipline</th>\n" +
        "                                    <th>Remove</th>\n" +
        "                                </tr>\n" +
        "                                </thead>\n" +
        "                                <tbody id=\"tbodySkill\"><tr><input  type='hidden' value='0'></tr></tbody>\n" +
        "                            </table>";
    $("#div-add-table-selected-streams").html(body);
}

function fillTableWithAddedSkills(data) {
    let arrayOfCells = getCells();
    let exist = false;
    for (let i = 0; i < arrayOfCells.length; i++) {
        if (data.name === arrayOfCells[i] && data.disciplineName === arrayOfCells[i + 1]) {
            exist = true;
            break;
        }
    }
    if (!exist) {
        $('#tbodySkill tr:last').after(
            '<tr>' +
            "<input  type='hidden'  value=" + $("#streamsForSkill").val() + ">" +
            "<td>" + data.name + "</td>" +
            "<td>" + data.disciplineName + "</td>" +
            "<td><button type='button' class='btn' onclick='deleteRowInSkillAddModal(this)'>Remove</button></td>" +
            '</tr>');
    }
    exist = false;
}

function fillTableWithUpdatedSkills(data) {
    let arrayOfCells = getUpdatedCells();
    let exist = false;
    for (let i = 0; i < arrayOfCells.length; i++) {
        if (data.name === arrayOfCells[i] && data.disciplineName === arrayOfCells[i + 1]) {
            exist = true;
            break;
        }
    }
    if (!exist) {
        $('#tbodySkillOfExist tr:last').after(
            '<tr>' +
            "<input  type='hidden'  value=" + $("#newStreamsForSkill").val() + ">" +
            "<td>" + data.name + "</td>" +
            "<td>" + data.disciplineName + "</td>" +
            "<td><button type='button' class='btn' onclick='deleteRowInSkillAddModal(this)'>Remove</button></td>" +
            '</tr>');
    }
    exist = false;
}

function prepareDataForAddSkill() {
    return {
        name: $("#skillName").val().trim(),
        typeStr: $("#typeForSkill").val(),
        streamsId: getAddedStreamIds()
    };
}

function prepareDataForUpdateSkill() {
    return {
        name: $("#skillNewName").val().trim(),
        typeStr: $("#newTypeSkill").val(),
        streamsId: getUpdatedStreamIds()
    };
}

function getAddedStreamIds() {
    let listStreamIds = [];
    $("#skillsInserts").find('tbody').find('tr').each(function () {
        let currentStreamId = $(this).find('input[type="hidden"]').eq(0).val();
        listStreamIds.push(currentStreamId.valueOf());
    });
    listStreamIds.splice(0, 1);
    return listStreamIds;
}

function getUpdatedStreamIds() {
    let listStreamIds = [];
    $("#skillsExist").find('tbody').find('tr').each(function () {
        let currentStreamId = $(this).find('input[type="hidden"]').eq(0).val();
        listStreamIds.push(currentStreamId.valueOf());
    });
    listStreamIds.splice(0, 1);
    return listStreamIds;
}

function deleteRowInSkillAddModal(btn) {
    var row = btn.parentNode.parentNode;
    row.parentNode.removeChild(row);
}

function getUpdatedCells() {
    let cellArray = [];
    var table = document.getElementById("skillsExist");
    for (var r = 0, n = table.rows.length; r < n; r++) {
        for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
            if ((table.rows[r].cells[c].innerHTML) !== "Stream" && (table.rows[r].cells[c].innerHTML) !== "Discipline"
                && (table.rows[r].cells[c].innerHTML) !== "Remove")
                cellArray.push(table.rows[r].cells[c].innerHTML);
        }
    }
    return cellArray;
}

function getCells() {
    let cellArray = [];
    var table = document.getElementById("skillsInserts");
    for (var r = 0, n = table.rows.length; r < n; r++) {
        for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
            if ((table.rows[r].cells[c].innerHTML) !== "Stream" && (table.rows[r].cells[c].innerHTML) !== "Discipline"
                && (table.rows[r].cells[c].innerHTML) !== "Remove")
                cellArray.push(table.rows[r].cells[c].innerHTML);
        }
    }
    return cellArray;
}


