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

$("#goEditPassword").on("click", function () {
    let span = "";
    span += "<form>"
    span += "<label for='edit-password-old'></label>" +
        "<input type='text' id='edit-password-old' name='edit-password-old' placeholder='Old Password'>";
    span += "<label for='edit-password-new'></label>" +
        "<input type='text' id='edit-password-new' name='edit-password-new' placeholder='New Password'>";
    span += "<label for='edit-password-repeat'></label>" +
        "<input type='text' id='edit-password-repeat' name='edit-password-repeat' placeholder='Repeat New Password'>";
    span += "<button id='submit-edit' type='submit'>Submit</button>"
    span += "</form>"
    $("#main-div span").html(span);
});

function getProfile(data) {
    let tbody = "";
    tbody += "<td hidden id='db-id-edit'>" + data.id + "</td>";
    tbody += "<td>" + data.username + "</td>";
    tbody += "<td>" + data.role + "</td>";
    $("#profileTable tbody").html(tbody);
}