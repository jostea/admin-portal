$("#goUsers").on("click", function () {
    window.location.href = "/users/";
});

$("#goStreams").on("click", function () {
    window.location.href = "/streamView/";
});

$("#goTasks").on("click", function () {
    window.location.href = "/tasks/";
});

$("#goLogout").on("click", function () {
    $.ajax({
        method: "POST",
        url: "/logout",
        success: function () {
            window.location.replace("/");
        }
    })
});

$("#goHome").on("click", function () {
    window.location.replace("/landing");
});

$("#goProfile").on("click", function () {
    window.location.replace("/profile")
});

$("#goDisciplines").on("click", function () {
    window.location.replace("/disciplineView/");
});

$("#goSkills").on("click", function () {
    window.location.replace("/skillsView/");
});

$("#goSwagger").on("click", function () {
    window.open("/swagger-ui.html#/",'_blank')
});






