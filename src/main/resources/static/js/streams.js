$(document).ready(getAllStreams());

let flagIdStream;
let streamIdOnDL;

function deleteStream(val) {
    $.ajax({
        method: "DELETE",
        url: gOptions.aws_path + "/streamView/streams/delete/" + val,
        success: function () {
            getAllStreams();
        }, error(response) {
            $("#alertStream").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function filterByName(val) {
    if (val.trim()==="") {
        getAllStreams();
    } else {
        $.ajax({
            method: "GET",
            url: gOptions.aws_path + "/streamView/streams/name/" + val,
            success: function (response) {
                fillTable(response);
            }, error: function (response) {
                if (response.status === 404) {
                    $("#alertStream").html(`<div class="alert alert-danger" role="alert"><p>Nothing found</p></div>`);
                }
            }
        });
    }
}

function getAllStreams() {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/streamView/streams",
        success: function (response) {
            fillTable(response);
        },
        error(response) {
            if (response.status === 400) {
                $("#alertStream").html(`<div class="alert alert-danger" role="alert"><p>Couldn't get streams</p></div>`);
            }
        }
    });
}

function edit(id) {
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/streamView/stream/edit/" + id,
        data: JSON.stringify(prepareDataE()),
        contentType: "application/json",
        success: function () {
            getAllStreams();
        }, error: function (response) {
            $("#alertStream").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function saveIdStream(par1, par2, par3, par4) {
    getAllDisciplinesEdit(par3, par4);
    flagIdStream = par1;
    let modalDiv = " <div class=\"container-fluid\">\n" +
        "                        <form>\n" +
        "                            <div class=\"col-md-4\">\n" +
        "                                <label for=\"name\">Enter stream title:</label>" +
        "                        <input type=\"text\" id=\"stream-input\" value='" + par2 + "'/></div>\n" +
        "                      <div class=\"col-md-6\"><label for=\"editDisciplines\">Disciplines:</label>\n" +
        "                        <select id=\"editDisciplines\" class=\"form-control\">\n" +
        "                        </select></div>\n" +
        "                    </form></div>";
    $("#formInModal").html(modalDiv);
}

function fillTable(data) {
    $("#alertStream").html("");
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + data[i].disciplineName + "</td>";
        tbody += `<td><button type='button' class="btn btn-info " data-toggle="modal" data-target="#editModal" 
                        onclick='saveIdStream(${data[i].id},"${data[i].name}","${data[i].disciplineName}",
                        ${data[i].disciplineId});'>Edit</button></td>`;
        tbody += `<td><button type='button' 
                        onclick='confirmDeleteStream(${data[i].id});' class='btn' 
                        data-target="#deleteModalStream"   data-toggle="modal">Delete</button></td>`;
        tbody += `<td><button type='button' onclick="goTestStructurePage(${data[i].id})" class='btn'>Test Structure</button></td>`;
        tbody += "</tr>";
    }
    $("#streamsTable tbody").html(tbody);
}

function goTestStructurePage(id) {
    window.location.href = gOptions.aws_path + "/testStructure/" + id;
}

$("#confirm-delete-stream-button").on("click", function () {
    deleteStream(streamIdOnDL);
});
function confirmDeleteStream(par1) {
    streamIdOnDL = par1;
}

function getAllDisciplinesEdit(DisciplineName, DisciplineId) {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/discipline/disciplines",
        success: function (response) {
            fillSelectorEdit(response, DisciplineName, DisciplineId);
        }
    });
}

function fillSelectorEdit(data, DisciplineName, DisciplineId) {
    let option = "";
    option += "<option value=" + DisciplineId + ">" + DisciplineName + "</option>";
    for (let i = 0; i < data.length; i++) {
        if (DisciplineName === data[i].name) {
            continue;
        }
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

