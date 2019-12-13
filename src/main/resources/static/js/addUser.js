$(document).ready(function () {
    $(".spinner").hide();
    $(".main-div").show();
});

$("#addUser").on("click", function () {
    if (addUserRequestValidation()) {
        $.ajax({
            method: "POST",
            url: gOptions.aws_path + "/users/add",
            data: JSON.stringify(prepareDataToAddUser()),
            contentType: "application/json",
            beforeSend: function () {
                $(".spinner").show();
                $(".main-div").hide();
            },
            success: function () {
                window.location.replace(gOptions.aws_path + "/users/?added");
            },
            error: function (response) {
                $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>" + response.responseText + "</p></div>")
            },
            complete: function () {
                $(".spinner").hide();
                $(".main-div").show();
            }
        });
    }
});


function prepareDataToAddUser(){
    return {
        username: $("#username-add").val(),
        email: $("#email-add").val()
    }
}

function validateEmail(mail)
{
    if (/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(mail))
    {
        return true;
    }
    return false;
}

function addUserRequestValidation() {
    if($("#username-add").val() && $("#email-add").val() && !(/\s/.test($("#username-add").val()))) {
        if (validateEmail($("#email-add").val())) {
            if ($("#username-add").val().length>=6) {
                if (/^[a-z.\-_!#]+$/.test($("#username-add").val())) {
                    if (repetitionTest($("#username-add").val())) {
                        return true;
                    } else {
                        $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>Use more letters</p></div>")
                    }
                } else {
                    $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>Username can only contain lowercase letters and next symbols: [.-_!#]</p></div>")
                }
            } else {
                $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>Username must contain at least six characters</p></div>")
            }
        } else {
            $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>Looks like you have provided an invalid email</p></div>")
        }
    } else {
        $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>You cannot leave any field empty or add whitespaces</p></div>");
    }
    return false;
}

function repetitionTest(username) {
    let repcounter = 0;
    var usernameArr = username.split("");
    usernameArr.forEach(function (a) {
        if (!(/^[a-z]+$/.test(a))) {
            repcounter++;
        }
    });
    if (repcounter>username.length/1.5) {
        return false;
    }
    return true
}