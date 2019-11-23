$(document).ready(getAllUsers());
$("#addNewUser").on('click', function () {
    window.location.href ="/users/add";
});

$(".view-single-user-div").on("click", '.enable-disable-user',  function() {
    if(confirm("Are you sure you want to perform this action?")) {
        var val = document.querySelector("#db-id-edit").textContent;
        $.ajax({
            method: "PUT",
            url: "/users/delete/" + val,
            data: JSON.stringify(),
            contentType: "application/json",
            success: function () {
                getAllUsers();
            }
        });
    }
});

$("#usersTable").on("click", ".view-user", function () {
    $(".view-single-user-div").show();
    $(".view-users-div").hide();
    $(".edit-user-div").hide();
    var val = this.parentNode.parentElement.querySelector(".db-id").textContent;
    $.ajax({
        method: "GET",
        url: "/users/" + val,
        success: function (response) {
            fillTableForOne(response)
        }
    });
});

$("#editUser").on("click", ".edit-user-apply", function () {
    var val = this.parentNode.parentElement.querySelector("#edit-db-id").textContent;
    $.ajax({
        method: "PUT",
        url: "/users/edit/" + val,
        data: JSON.stringify(prepareDataToEditUser()),
        contentType: "application/json",
        success: function () {
            getAllUsers();
        }
    });
});

$("#edit-user").on("click", function () {
    $(".view-single-user-div").hide();
    $(".view-users-div").hide();
    $(".edit-user-div").show();
    var val = document.querySelector("#db-id-edit").textContent;
    $.ajax({
        method: "GET",
        url: "/users/edit/" + val,
        success: function (response) {
            showUserDetails(response);
        }
    });
});

function getAllUsers() {
    $(".view-single-user-div").hide();
    $(".view-users-div").show();
    $(".edit-user-div").hide();
    $.ajax({
        method: "GET",
        url: "/users/all",
        success: function (response) {
            if (response.length===0) {
                $("#usersTable").hide();
                $("#usersInfoParagraph").html("No users in the system yet");
            } else if (response.length>0) {
                fillTable(response);
            }
        }
    });
}

function fillTableForOne(data) {
    let tbody = "";
    let disable_button = "";
    tbody += "<td hidden id='db-id-edit'>" + data.id + "</td>";
    tbody += "<td>" + data.username + "</td>";
    tbody += "<td>" + data.email + "</td>";
    tbody += "<td>" + data.active + "</td>";
    if (data.active) {
        disable_button += "<button class='enable-disable-user btn btn-danger'>Disable this user</button>"
    } else if(!data.active) {
        disable_button += "<button class='enable-disable-user btn btn-success'>Enable this user</button>"
    }
    $("#enable-disable-user-button").html(disable_button);
    $("#singleUserTable tbody tr").html(tbody);
}

function fillTable(data) {
    let tbody = "";
    var order = 1;
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr id='row" + data[i].id + "'>";
        tbody += "<td hidden class='db-id'>" + data[i].id + "</td>";
        tbody += "<td>" + order + "</td>";
        tbody += "<td><a class='view-user'>" + data[i].username + "</a></td>";
        tbody += "</tr>";
        order++;
    }
    $("#usersTable tbody").html(tbody);
}

function showUserDetails(data) {
    let tbody = "";
    tbody += "<form>";
    tbody += "<tr id='row" + data.id + "'>";
    tbody += "<td hidden id='edit-db-id'>" + data.id + "</td>";
    tbody += "<tr><label for='edit-username'></label>" +
        "<mark>Username:</mark> <input type='text' id='edit-username' name='edit-username' value='" + data.username + "'></tr>";
    tbody += "<tr><label for='edit-email'></label>" +
        "<mark>Email:</mark> <input type='text' id='edit-email' name='edit-email' value='" + data.email + "'></tr>";
    tbody += "<tr><label for='edit-password'></label>" +
        "<mark>Password:</mark> <input type='text' id='edit-password' name='edit-password' placeholder='New Password Here'></tr>";
    tbody += "<tr><button type='submit' class='edit-user-apply btn btn-primary'>Apply Changes</button></tr>";
    tbody += "</tr>";
    tbody += "</form>";
    $("#editUser tbody").html(tbody);
}

function prepareDataToEditUser(){
    return {
        username: $("#edit-username").val(),
        email: $("#edit-email").val(),
        password: document.querySelector("#edit-password").value
    }
}
