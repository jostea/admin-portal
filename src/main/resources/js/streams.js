$(document).ready(getAllStreams());

function getAllStreams() {
    $.ajax({
        method: "GET",
        url: "",
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
        tbody += "<td>" + data[i].streamName + "</td>";

        tbody += "</tr>";
    }
    $("#streamsTable tbody").html(tbody);
}