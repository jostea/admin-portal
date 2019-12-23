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
                    success: function (response) {
                        $("#succErrMessage").html("<div class='alert alert-success' role='alert'><p>Password changed successfully</p></div>");
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
    span += "<input class='form-control' required='required' type='password' id='edit-password-old' name='edit-password-old' placeholder='Old Password'>";
    span += "<input class='form-control' required='required' type='password' id='edit-password-new' name='edit-password-new' placeholder='New Password'>";
    span += "<input class='form-control' required='required' type='password' id='edit-password-repeat' name='edit-password-repeat' placeholder='Repeat New Password'>";
    span += "<button id='edit-password-apply' class='btn btn-primary'>Change Password</button>";
    $("#main-div div #test").html(span);
});

function getProfile(data) {
    let body = "";
    body+="<div class='panel panel-default'>";
    body += "<div hidden id='db-id-edit'>" + data.id + "</div>";
    body+="<div class='panel-heading'><h1 class='profileH'><span class='glyphicon glyphicon-user'></span> <strong>PROFILE</strong></h1></div>";
    body+="<div class='panel-body'>";
    body += "<p>Here the currently signed user details are displayed. Only the password may be changed here.</p>"
    body += "<h4 class='nameH'>" + data.username + "</h4>";
    body += "<h6 class='roleH'><span class='glyphicon glyphicon-ok-circle'></span> " + data.role + "</h6>";
    body+="</div>";
    body+="</div>";
    $("#profileDetails").html(body);
}

function prepareDataToEditPassword() {
    return {
        oldPassword: $("#edit-password-old").val(),
        newPassword: $("#edit-password-new").val()
    }
}

function validatePasswords(passold, passnew, passrep) {

}

