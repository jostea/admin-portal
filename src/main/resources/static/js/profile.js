$(document).ready(function () {
        var val = document.querySelector("#goProfile").textContent;
        $.ajax({
            method: "GET",
            url: "/profile/" + val,
            success: function (response) {
                getProfile(response);
            }
        })
    }
);

$("#editPasswordSpan").on("click", "#edit-password-apply", function () {
    var passnew = document.querySelector("#edit-password-new").value;
    var passrep = document.querySelector("#edit-password-repeat").value;
    if(passrep === passnew) {
        var val = document.querySelector("#goProfile").textContent;
        $.ajax({
            method: "PUT",
            url: "/profile/" + val,
            data: JSON.stringify(prepareDataToEditPassword()),
            contentType: "application/json",
            success: function () {
                window.location.replace("/landing");
            },
            error: function (response) {
                alert(response.responseText);
                window.location.replace("/landing");
            }
        });
    } else {
        alert("New passwords don't match");
        window.location.replace("/profile");
    }
});

$("#goEditPassword").on("click", function () {
    let span = "";
    span += "<label for='edit-password-old'></label>" +
        "<input required='required' type='password' id='edit-password-old' name='edit-password-old' placeholder='Old Password'>";
    span += "<label for='edit-password-new'></label>" +
        "<input required='required' type='password' id='edit-password-new' name='edit-password-new' placeholder='New Password'>";
    span += "<label for='edit-password-repeat'></label>" +
        "<input required='required' type='password' id='edit-password-repeat' name='edit-password-repeat' placeholder='Repeat New Password'>";
    span += "<button id='edit-password-apply' class='btn btn-primary'>Change Password</button>"
    $("#main-div div #test").html(span);
});

function getProfile(data) {
    let tbody = "";
    tbody += "<td hidden id='db-id-edit'>" + data.id + "</td>";
    tbody += "<td>" + data.username + "</td>";
    tbody += "<td>" + data.role + "</td>";
    $("#profileTable tbody").html(tbody);
}

function prepareDataToEditPassword() {
    return {
        oldPassword: $("#edit-password-old").val(),
        newPassword: $("#edit-password-new").val()
    }
}

