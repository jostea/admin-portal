$(document).ready(getAllStreams());

function deletedById(val) {
    $.ajax({
        method: "DELETE",
        url: "/streamView/streams/delete/" + val,
        success: function () {
            getAllStreams();
        }
    });
}

function findIdByName(val) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/name/" + val,
        success: function (response) {
                fillTable(response);
        }
    });
}

function getAllStreams() {
    $.ajax({
        method: "GET",
        url: "/streamView/streams",
        success: function (response) {
            fillTable(response);
        }
    });
}

function edit(id) {
    $.ajax({
        method: "PUT",
        url: "/streamView/stream/edit/" + id,
        data: JSON.stringify(prepareDataE()),
        contentType: "application/json",
        success: function () {
            getAllStreams();
        }
    });
}

let flagIdStream;

function saveIdStream(par) {
    flagIdStream = par;
    getAllDisciplinesEdit();
}

function fillTable(data) {
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + data[i].disciplineName + "</td>";
        tbody +=
            "<td>" + "<button type='button' " +
            "onclick='deletedById(" + data[i].id + ");' " +
            "class='btn'>" +
            "Delete" +
            "</button>" + "</td>";
        tbody += `<td><button type='button' class="btn btn-info " data-toggle="modal" data-target="#editModal" 
                        onclick='saveIdStream(${data[i].id});'>Edit</button></td>`;
        tbody += "</tr>";
    }
    $("#streamsTable tbody").html(tbody);
}

function getAllDisciplinesEdit() {
    $.ajax({
        method: "GET",
        url: "/discipline/disciplines",
        success: function (response) {
            fillSelectorEdit(response);
        }
    });
}

function fillSelectorEdit(data) {
    let option = "";
    for (let i = 0; i < data.length; i++) {
        option += "<option value=" + data[i].id + ">" + data[i].name + "</option>";
    }
    $("#editDisciplines").html(option);
}

function prepareDataE() {
    return {
        name: $("#stream-input").val(),
        disciplineId: $("#editDisciplines").val()
    }
}

