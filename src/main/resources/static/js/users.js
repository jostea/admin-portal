$(document).ready(function () {
        if (this.URL.split("?").length>1) {
            const paramName = this.URL.split("?")[1].split("=")[0];
            let param = this.URL.split("?")[1].split("=")[1];
            let editUsername = "";
            switch (paramName) {
                case "all":
                    getAllUsers();
                    $(".view-users-div").show();
                    $(".view-single-user-div").hide();
                    $(".edit-user-div").hide();
                    if (param === "added") {
                        $("#successMessage").html("<div class='alert alert-success' role='alert'><p>User <b>" + editUsername + "</b> has been edited successfully</p></div>")
                    }
                    break;
                case "one":
                    $.ajax({
                        method: "GET",
                        url: gOptions.aws_path + "/users/" + param,
                        success: function (response) {
                            fillTableForOne(response);
                            history.pushState({page: 1}, "one", "?one=" + param);
                        }
                    });
                    $(".view-users-div").hide();
                    $(".view-single-user-div").show();
                    $(".edit-user-div").hide();
                    break;
                case "edit":
                    $.ajax({
                        method: "GET",
                        url: gOptions.aws_path + "/users/edit/" + param,
                        success: function (response) {
                            showUserDetails(response);
                            history.pushState({page: 2}, "edit", "?edit=" + param);
                        }
                    });
                    $(".view-users-div").hide();
                    $(".view-single-user-div").hide();
                    $(".edit-user-div").show();
                    break;
                default:
                    window.location.replace(gOptions.aws_path + "/users/?all");
            }
        } else {
            window.location.replace(gOptions.aws_path +"/users/?all")
        }
    }
);

$("#addNewUser").on('click', function () {
    window.location.href = gOptions.aws_path + "/users/add";
});

$(".view-single-user-div").on("click", '.enable-disable-user',  function() {
    if(confirm("Are you sure you want to perform this action?")) {
        var val = document.querySelector("#db-id-edit").textContent;
        $.ajax({
            method: "PUT",
            url: gOptions.aws_path + "/users/delete/" + val,
            data: JSON.stringify(),
            contentType: "application/json",
            success: function () {
                location.reload();
            }
        });
    }
});

$(".view-users-div").on("click", ".view-user", function () {
    var val = this.parentNode.parentElement.querySelector(".db-id").textContent;
    window.location.href = gOptions.aws_path + "/users/?one=" + val;
});

$("#editUser").on("click", ".edit-user-apply", function () {
    var val = this.parentNode.parentElement.querySelector("#edit-db-id").textContent;
    $("#editUserMessage").html("");
    if (editRequestValidation()) {
        $.ajax({
            method: "PUT",
            url: gOptions.aws_path + "/users/edit/" + val,
            data: JSON.stringify(prepareDataToEditUser()),
            contentType: "application/json",
            success: function () {
                window.location = gOptions.aws_path + "/users/?all=added";
            },
            error: function (response) {
                $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Failed to edit. Looks like the credentials you're trying to set are already present in the system " + response.responseText + "</p></div>")
            }
        });
    }
});


$("#edit-user").on("click", function () {
    var val = document.querySelector("#db-id-edit").textContent;
    window.location.href = gOptions.aws_path + "/users/?edit=" + val;
});

function getAllUsers() {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/users/all",
        success: function (response) {
            if (response.length===0) {
                $("#usersInfoParagraph").html("No users in the system yet");
                console.log(response);
            } else if (response.length>0) {
                fillTable(response);
                history.pushState({page: 0}, "all", "?all");
            }
        }
    });
}

function fillTableForOne(data) {
    let body = "";
    let disable_button = "";
    body+="<div class='panel panel-default'>";
    body += "<div hidden id='db-id-edit'>" + data.id + "</div>";
    body+="<div class='panel-heading'><h3>Username: <strong>" + data.username + "</strong></h3></div>";
    body+="<div class='panel-body'>";
    body += "<p><mark>Email:</mark> "+ data.email +"</p>";
    body += "<p><mark>Activity:</mark> " + yesNoVal(data.active) + "</p>"
    body+="</div>";
    body+="</div>";
    if (data.active) {
        disable_button += "<button class='enable-disable-user btn-custom btn btn-danger'>Disable this user</button>"
    } else if(!data.active) {
        disable_button += "<button class='enable-disable-user btn-custom btn btn-success'>Enable this user</button>"
    }
    $("#enable-disable-user-button").html(disable_button);
    $("#displaySingleUser").html(body);
}

function fillTable(data) {
    let tbodyEnabled = "";
    let tbodyDisabled = "";
    for (let i = 0; i < data.length; i++) {
        if (data[i].active) {
            tbodyEnabled += "<tr id='row" + data[i].id + "'>";
            tbodyEnabled += "<td hidden class='db-id'>" + data[i].id + "</td>";
            tbodyEnabled += "<td><a class='view-user' style='cursor: pointer'>" + data[i].username + "</a></td>";
            tbodyEnabled += "</tr>";
        } else {
            tbodyDisabled += "<tr id='row" + data[i].id + "'>";
            tbodyDisabled += "<td hidden class='db-id'>" + data[i].id + "</td>";
            tbodyDisabled += "<td><a class='view-user' style='cursor: pointer'>" + data[i].username + "</a></td>";
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
    tbody+="<div class='row'></div>"
    tbody += "<tr id='row" + data.id + "'>";
    tbody += "<td hidden id='edit-db-id'>" + data.id + "</td>";
    tbody += "<tr><label for='edit-username'>Username:</label>" +
        "<input class='form-control' type='text' id='edit-username' name='edit-username' value='" + data.username + "'></tr>";
    tbody += "<tr><label for='edit-email'>Email:</label>" +
        "<input class='form-control' type='text' id='edit-email' name='edit-email' value='" + data.email + "'></tr>";
    tbody += "<tr><label for='edit-password'>Password:</label>" +
        "<input class='form-control' type='text' id='edit-password' name='edit-password' placeholder='New Password Here'></tr>";
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
        return "<span class='enabled'>Enabled</span>";
    }
    return "<span class='disabled'>Disabled</span>";
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
                    if (!(/\s/.test(document.querySelector("#edit-password").value))) {
                        return true;
                    } else {
                        $("#editUserMessage").html("<div class='alert alert-danger' role='alert'><p>Password cannot contain spaces</p></div>")
                    }
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
