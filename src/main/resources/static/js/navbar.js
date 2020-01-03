$("#goUsers").on("click", function () {
    window.location.href = gOptions.aws_path + "/users/";
});

$("#goStreams").on("click", function () {
    window.location.href = gOptions.aws_path + "/streamView/";
});

$("#goTasks").on("click", function () {
    window.location.href = gOptions.aws_path + "/tasks/";
});

$("#goLogout").on("click", function () {
    $.ajax({
        method: "POST",
        url: gOptions.aws_path + "/logout",
        success: function () {
            window.location.replace(gOptions.aws_path + "/");
        }
    })
});

$("#goHome").on("click", function () {
    window.location.replace(gOptions.aws_path + "/landing");
});

$("#goProfile").on("click", function () {
    window.location.replace(gOptions.aws_path + "/profile")
});

$("#goDisciplines").on("click", function () {
    window.location.replace(gOptions.aws_path + "/disciplineView/");
});

$("#goSkills").on("click", function () {
    window.location.replace(gOptions.aws_path + "/skillsView/");
});

$("#goSwagger").on("click", function () {
    window.open(gOptions.aws_path + "/swagger-ui.html#/",'_blank')
});






