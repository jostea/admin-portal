$(document).ready(getAllUsers());
$("#addNewUser").on('click', function () {
    window.location.href ="add";
});

function getAllUsers() {
    $.ajax({
        method: "GET",
        url: "/userView/users",
        success: function (response) {
            fillTable(response);
        }
    });
}

function fillTable(data) {
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr id='row" + data[i].id + "'>";
        tbody += "<td>" + data[i].id + "</td>";
        tbody += "<td>" + data[i].username + "</td>";
        tbody += "<td>" + data[i].email + "</td>";
        tbody += "</tr>";
    }
    $("#usersTable tbody").html(tbody);
}

$("#addUser").on("click", function (event) {
    event.preventDefault();
    $.ajax({
        method: "POST",
        url: "add",
        data: JSON.stringify(prepareData()),
        contentType: "application/json",
        success: function () {
            console.log("Success");
            getAllUsers();
        }
    })
});

function prepareData(){
    return {
        username: $("#username").val(),
        email: $("#email").val()
    }
}