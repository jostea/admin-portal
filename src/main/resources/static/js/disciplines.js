$(document).ready(getAllDisciplines());

let flagDiscipline;

function getElementsFromUi(par1, par2) {
    flagDiscipline = par1;
    let modalDiv = "<form>\n" +
        "                        <label for=\"discipline-input-edit\">\n" +
        "                            Enter discipline title:\n" +
        "                        </label>\n" +
        "                        <input type=\"text\" id=\"discipline-input-edit\" value='" + par2 + "'/>\n" +
        "                    </form>";
    $("#formInModalDiscipline").html(modalDiv);
}

function fillTableDiscipline(data) {
    $("#alertDiscipline").html("");
    let tbody = "";
    for (let i = 0; i < data.length; i++) {
        tbody += "<tr>";
        tbody += "<td>" + data[i].name + "</td>";
        tbody += `<td><button type='button' class="btn btn-info " data-toggle="modal" data-target="#editModalDiscipline" 
                        onclick='getElementsFromUi(${data[i].id},"${data[i].name}");'>Edit</button></td>`;
        tbody += `<td><button type='button' 
                        onclick='callConfirmWindowDiscipline(${data[i].id});' class='btn' 
                        data-target="#deleteModalDiscipline" data-toggle="modal">Delete</button></td>`;
        tbody += "</tr>";
    }
    $("#disciplinesTable tbody").html(tbody);
}

let disciplineIdOnDL;

$("#confirm-delete-discipline-button").on("click", function () {
    deleteDiscipline(disciplineIdOnDL);
});

function callConfirmWindowDiscipline(par1) {
    disciplineIdOnDL = par1;
}


function deleteDiscipline(par) {
    $.ajax({
        method: "DELETE",
        url: gOptions.aws_path + "/discipline/delete/" + par,
        success: function () {
            getAllDisciplines();
        }, error(response) {
            $("#alertDiscipline").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function addDiscipline() {
    $.ajax({
        method: "POST",
        url: gOptions.aws_path + "/discipline/add",
        data: JSON.stringify(prepareDataDisciplineAdd()),
        contentType: "application/json",
        success: function (response) {
            getAllDisciplines();
            $("#discipline-input-add").val("");
        }, error(response) {
            $("#discipline-input-add").val("");
            $("#alertDiscipline").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function getAllDisciplines() {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/discipline/disciplines",
        success: function (response) {
            fillTableDiscipline(response);
        }, error() {
            $("#alertDiscipline").html(`<div class="alert alert-danger" role="alert"><p> Couldn't view discipline </p></div>`);

        }
    });
}

function editDiscipline(id) {
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/discipline/update/" + id,
        data: JSON.stringify(prepareDataDiscipline()),
        contentType: "application/json",
        success: function (response) {
            getAllDisciplines();
        }, error(response) {
            $("#alertDiscipline").html(`<div class="alert alert-danger" role="alert"><p>` + response.responseText + `</p></div>`);
        }
    });
}

function prepareDataDiscipline() {
    return {
        name: $("#discipline-input-edit").val()
    }
}

function prepareDataDisciplineAdd() {
    return {
        name: $("#discipline-input-add").val().trim()
    }
}

function findByNameDiscipline(val) {
    if (val.trim() === "") {
        getAllDisciplines();
    } else {
        $.ajax({
            method: "GET",
            url: gOptions.aws_path + "/discipline/filterByName" +
                "/" + val,
            success: function (response) {
                fillTableDiscipline(response);
            }
        });
    }
}
