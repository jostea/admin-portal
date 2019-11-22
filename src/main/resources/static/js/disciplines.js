$(document).ready(getAllDisciplines());

let flagDiscipline;

function getElementsFromUi(par1, par2) {
    flagDiscipline = par1;
    let modalDiv = "<form>\n" +
        "                        <label for=\"discipline-input-edit\">\n" +
        "                            Name:\n" +
        "                        </label>\n" +
        "                        <input type=\"text\" id=\"discipline-input-edit\" value='" + par2 + "'/>\n" +
        "                    </form>";
    $("#formInModalDiscipline").html(modalDiv);
}

function fillTableDiscipline(data) {
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
        url: "/discipline/delete/" + par,
        success: function () {
            getAllDisciplines();
        }, error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't delete discipline");
            }
        }
    });
}

function addDiscipline() {
    $.ajax({
        method: "POST",
        url: "/discipline/add",
        data: JSON.stringify(prepareDataDisciplineAdd()),
        contentType: "application/json",
        success: function (response) {
            getAllDisciplines()
            $("#discipline-input-add").val("");
        }, error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't add discipline");
            }
        }
    });
}

function getAllDisciplines() {
    $.ajax({
        method: "GET",
        url: "/discipline/disciplines",
        success: function (response) {
            fillTableDiscipline(response);
        }, error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't delete view discipline");
            }
        }
    });
}

function editDiscipline(id) {
    $.ajax({
        method: "PUT",
        url: "/discipline/update/" + id,
        data: JSON.stringify(prepareDataDiscipline()),
        contentType: "application/json",
        success: function (response) {
            getAllDisciplines();
        }, error(xhr) {
            if (xhr.status === 400) {
                alert("Couldn't update discipline");
            }
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
        name: $("#discipline-input-add").val()
    }
}

function findByNameDiscipline(val) {
    if (val.trim() === "") {
        getAllDisciplines();
    } else {
        $.ajax({
            method: "GET",
            url: "/discipline/filterByName" +
                "/" + val,
            success: function (response) {
                fillTableDiscipline(response);
            }
        });
    }
}
