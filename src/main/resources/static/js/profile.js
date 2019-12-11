$(document).ready(function () {
        var val = document.querySelector("#goProfile").textContent;
        $.ajax({
            method: "GET",
            url: gOptions.aws_path + "/profile/" + val,
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
        if (passnew.length >=8) {
            if (!(/\s/.test(passnew))) {
                var val = document.querySelector("#goProfile").textContent;
                $.ajax({
                    method: "PUT",
                    url: gOptions.aws_path + "/profile/" + val,
                    data: JSON.stringify(prepareDataToEditPassword()),
                    contentType: "application/json",
                    success: function () {
                        window.location.replace("/landing");
                    },
                    error: function (response) {
                        $("#succErrMessage").html("<div class='alert alert-danger' role='alert'><p>" + response.responseText + "</p></div>");
                    }
                });
            } else {
                $("#succErrMessage").html("<div class='alert alert-danger' role='alert'><p>The password cannot contain spaces</p></div>");
            }
        } else {
            $("#succErrMessage").html("<div class='alert alert-danger' role='alert'><p>New password should contain at least 8 characters</p></div>");
        }
    } else {
        $("#succErrMessage").html("<div class='alert alert-danger' role='alert'><p>New passwords don't match</p></div>")
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

function validatePasswords(passold, passnew, passrep) {

}

