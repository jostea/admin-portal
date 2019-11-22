function getAllDisciplinesAdd() {
    $.ajax({
        method: "GET",
        url: "/discipline/disciplines",
        success: function (response) {
            fillSelectorAdd(response);
        }
    });
}

function fillSelectorAdd(data) {
    let option = "";
    for (let i = 0; i < data.length; i++) {
        option += "<option value=" + data[i].id + ">" + data[i].name + "</option>";
    }
    $("#disciplines").html(option);
}

function addStream() {
    $.ajax({
        method: "POST",
        url: "/streamView/stream/add",
        data: JSON.stringify(prepareDataAdd()),
        contentType: "application/json",
        success: function () {
            $("#name").val("");
            getAllStreams();
        },
        error: function (xhr, error) {
            if (xhr.status === 400) {
                alert("Couldn't add this stream");
            }
        }
    })
}

function prepareDataAdd() {
    return {
        name: $("#name").val(),
        disciplineId: $("#disciplines").val()
    }
}

