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
                $("#successMessage").html("<div class='alert alert-success' role='alert'><p>User's activity has been successfully changed</p></div>")
            }
        });
    }
});

$(".view-users-div").on("click", ".view-user", function () {
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
    $("#editUserMessage").html("");
    if (editRequestValidation()) {
        $.ajax({
            method: "PUT",
            url: "/users/edit/" + val,
            data: JSON.stringify(prepareDataToEditUser()),
            contentType: "application/json",
            success: function () {
                getAllUsers();
                $("#successMessage").html("<div class='alert alert-success' role='alert'><p>User <b>" + $("#edit-username").val() + "</b> has been edited successfully</p></div>")
            },
            error: function (response) {
                $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Failed to edit. Looks like the credentials you're trying to set are already present in the system</p></div>")
            }
        });
    }
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
                $("#usersTableEnabled").hide();
                $("#usersTableDisabled").hide();
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
    tbody += "<td>" + yesNoVal(data.active) + "</td>";
    if (data.active) {
        disable_button += "<button class='enable-disable-user btn btn-danger'>Disable this user</button>"
    } else if(!data.active) {
        disable_button += "<button class='enable-disable-user btn btn-success'>Enable this user</button>"
    }
    $("#enable-disable-user-button").html(disable_button);
    $("#singleUserTable tbody tr").html(tbody);
}

function fillTable(data) {
    let tbodyEnabled = "";
    let tbodyDisabled = "";
    for (let i = 0; i < data.length; i++) {
        if (data[i].active) {
            tbodyEnabled += "<tr id='row" + data[i].id + "'>";
            tbodyEnabled += "<td hidden class='db-id'>" + data[i].id + "</td>";
            tbodyEnabled += "<td><a class='view-user'>" + data[i].username + "</a></td>";
            tbodyEnabled += "</tr>";
        } else {
            tbodyDisabled += "<tr id='row" + data[i].id + "'>";
            tbodyDisabled += "<td hidden class='db-id'>" + data[i].id + "</td>";
            tbodyDisabled += "<td><a class='view-user'>" + data[i].username + "</a></td>";
            tbodyDisabled += "</tr>";
        }
    }
    if (tbodyEnabled === "") {
        $("#usersTableEnabled tbody").html("No enabled users yet");
    } else {
        $("#usersTableEnabled tbody").html(tbodyEnabled);
    }
    if (tbodyDisabled === "") {
        $("#usersTableDisabled tbody").html("No disabled users yet");
    } else {
        $("#usersTableDisabled tbody").html(tbodyDisabled);
    }
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

function yesNoVal(val) {
    if (val) {
        return "Yes";
    }
    return "No";
}

function validateEmail(mail)
{
    if (/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(mail))
    {
        return true;
    }
    return false;
}

function editRequestValidation() {
    if($("#edit-username").val() && $("#edit-email").val() && !(/\s/.test($("#edit-username").val()))) {
        if (validateEmail($("#edit-email").val())) {
            if ($("#edit-username").val().length >= 6) {
                if (document.querySelector("#edit-password").value === "" || document.querySelector("#edit-password").value.length >= 8) {
                    return true;
                } else  {
                    $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Password too short, min eight characters</p></div>")
                }
            } else {
                $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Username must contain at least six characters</p></div>")
            }
        } else {
            $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Looks like you have provided an invalid email</p></div>");
        }
    } else {
        $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>You cannot leave any field empty or add whitespaces</p></div>")
    }
    return false;
}
