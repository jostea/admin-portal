$(document).ready(function () {
    getAllReports();
});

function getAllReports() {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/reports/all",
        success: function (response) {
            fillTable(response);
        }
    });
}

function fillTable(data) {
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += `<tr>`;
        tbody += `<td>${i + 1}</td>`;
        tbody += `<td>${data[i].discipline}</td>`;
        tbody += `<td>${data[i].stream}</td>`;
        tbody += `<td>${data[i].name}</td>`;
        tbody += `<td>${data[i].email}</td>`;
        tbody += `<td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="viewTest(${data[i].candidateId});">View results</button>
                   </td>`;
        tbody += `</tr>`;
    }
    $("#tbodyOfReports").html(tbody);
}

function viewTest(candidateId) {
    window.location.href = gOptions.aws_path + "/candidatesReport/" + candidateId;
}

function filterByEmail(email) {
    if (email.trim() !== "") {
        $.ajax({
            method: "GET",
            url: gOptions.aws_path + "/reports/email/" + email,
            success: function (response) {
                fillTable(response);
            }
        });
    } else {
        getAllReports();
    }
}

function refreshReports() {
    getAllReports();
    $("#email-input").val("");
}