$(document).ready(function () {
    $(".spinner").hide();
    $(".main-div").show();
});

$("#addUser").on("click", function () {
    $.ajax({
        method: "POST",
        url: "/users/add",
        data: JSON.stringify(prepareDataToAddUser()),
        contentType: "application/json",
        beforeSend: function() {
            $(".spinner").show();
            $(".main-div").hide();
        },
        success: function () {
            window.location.replace("/users/")
        },
        error: function(response) {
            $("#saveMessage").html("<div class='alert alert-danger' role='alert'><p>" + response.responseText + "</p></div>")
        },
        complete: function () {
            $(".spinner").hide();
            $(".main-div").show();
        }
    })
});


function prepareDataToAddUser(){
    return {
        username: $("#username").val(),
        email: $("#email").val()
    }
}