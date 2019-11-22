$(document).ready(getAllStreams());

let flagIdStream;
let streamIdOnDL;

function deleteStream(val) {
    $.ajax({
        method: "DELETE",
        url: "/streamView/streams/delete/" + val,
        success: function () {
            getAllStreams();
        }, error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't delete this stream");
            }
        }
    });
}

function filterByName(val) {
    if (val.trim()==="") {
        getAllStreams();
    } else {
        $.ajax({
            method: "GET",
            url: "/streamView/streams/name/" + val,
            success: function (response) {
                fillTable(response);
            }, error: function (xhr) {
                if (xhr.status === 404) {
                    alert("Nothing found");
                    getAllStreams();
                }
            }
        });
    }
}

function getAllStreams() {
    $.ajax({
        method: "GET",
        url: "/streamView/streams",
        success: function (response) {
            fillTable(response);
        },
        error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't get streams");
            }
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
        }, edit(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't update stream");
            }
        }
    });
}

function saveIdStream(par1, par2, par3, par4) {
    flagIdStream = par1;
    let modalDiv = "<form>\n" +
        "                        <label for=\"stream-input\">\n" +
        "                            Name:\n" +
        "                        </label>\n" +
        "                        <input type=\"text\" id=\"stream-input\" value='" + par2 + "'/>\n" +
        "                        <label for=\"editDisciplines\">Disciplines:</label>\n" +
        "                        <select id=\"editDisciplines\">\n" +
        "                               <option value=" + par4 + ">" + par3 + "</option>" +
        "                        </select>\n" +
        "                            <button type='button' onclick='getAllDisciplinesEdit()'>View</button> " +
        "                    </form>";
    $("#formInModal").html(modalDiv);
}

function fillTable(data) {
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
        tbody += "</tr>";
    }
    $("#streamsTable tbody").html(tbody);
}


$("#confirm-delete-stream-button").on("click", function () {
    deleteStream(streamIdOnDL);
});
function confirmDeleteStream(par1) {
    streamIdOnDL = par1;
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

