$(document).ready(getAllStreams());

function deletedById(val) {
    $.ajax({
        method: "DELETE",
        url: "/streamView/streams/delete/" + val,
        success: function (response) {
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

function fillTable(data) {
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += "<td>" + data[i].disciplineName + "</td>";
        tbody +=
            "<td>" + "<button type='button' " +
            "onclick='deletedById(" + data[i].id + ");' " +
            "class='btn btn-default'>" +
            "Delete" +
            "</button>" + "</td>";
        tbody += "</tr>";
    }
    $("#streamsTable tbody").html(tbody);
}