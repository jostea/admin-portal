$(document).ready(getAllStreams());
let tbody = "";

function deletedById(val) {
    $.ajax({
        method: "DELETE",
        url: "/streamView/streams/delete/" + val,
        success: function (response) {
            console.log(response);
            getAllStreams();
        }
    });
}

function getById(val) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/" + val,
        success: function (response) {
            console.log(response);
            // showStream(response);
        }
    });
}


function findIdByName(val) {
    $.ajax({
        method: "GET",
        url: "/streamView/streams/name/" + val,
        success: function (response) {
            console.log(response);
                fillTable(response);
            // tbody = "";
            // for (let i = 0; i < response.length; i++) {
            //     if (val.trim() === "") {
            //         getAllStreams();
            //     } else if (response[i].name.toLowerCase().includes((val.toLowerCase()))) {
            //         getById(response[i].id);
            //     }
            // }
        }
    });
}

function showStream(data) {
    tbody += "<tr>";
    tbody += "<td>" + data.name + "</td>";
    tbody += "<td>" + data.disciplineName + "</td>";
    tbody += "<td>" + "<button type='button' " +
        "onclick='deletedById(" + data.id + ");' " +
        "class='btn btn-default'>" +
        "Delete" +
        "</button>" + "</td>";
    tbody += "</tr>";
    $("#streamsTable tbody").html(tbody);
}

function getAllStreams() {
    $.ajax({
        method: "GET",
        url: "/streamView/streams",
        success: function (response) {
            console.log(response);
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