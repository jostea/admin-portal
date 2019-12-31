$(document).ready(function () {
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/tasksrest/allCode",
        success: function (response) {
            fillCodeTable(response);
        }
    });
});

function fillCodeTable(data) {
    let tbody = "";
    for (let i=0; i<data.length; i++) {
        tbody+="<tr>";
        tbody+="<input type='hidden' value=" + data[i].id + ">";
        tbody+="<td>" + (i+1) + "</td>";
        tbody+="<td>"+ data[i].title +"</td>";
        tbody+="<td>"+ data[i].complexity +"</td>";
        tbody+="<td>"+ data[i].enabled +"</td>";
        tbody+="<td>" + data[i].technology + "</td>";
        tbody += "<td>" + "<button class='viewCodeTask btn btn-light'> View </button>" + "</td>";
        tbody += "<td>" + "<button class='editCodeTask btn btn-primary'> Edit </button>" + "</td>";
        if (data[i].enabled) {
            tbody += "<td>" + "<button class='deleteCodeTask btn btn-warning'>Disable</button>" + "</td>";
        } else {
            if (!data[i].enabled) {
                tbody += "<td>" + "<button class='deleteCodeTask btn btn-success'>Enable</button>" + "</td>";
            }
        }
        tbody+="</tr>";
    }
    $("#tasksCode tbody").html(tbody);
}

$("#tasksCode").on("click", ".viewCodeTask", function () {
    let idTask = $(this).closest('tr').find('input[type="hidden"]').eq(0).val();
    window.location.href = "viewCodeTask/" + idTask;
});

$("#tasksCode").on("click", ".editCodeTask", function () {
    let idTask = $(this).closest('tr').find('input[type="hidden"]').eq(0).val();
    window.location.href = "editCodeTask/" + idTask;
});

$("#tasksCode").on("click", ".deleteCodeTask", function () {
    let idTask = $(this).closest('tr').find('input[type="hidden"]').eq(0).val();
    if (this.innerHTML === 'Disable') {
        this.innerHTML = 'Enable';
        this.classList.replace('btn-warning', 'btn-success');
    } else {
        if (this.innerHTML === 'Enable') {
            this.innerHTML = 'Disable';
            this.classList.replace('btn-success', 'btn-warning');
        }
    }
    disableEnableCodeTask(idTask);
});

function disableEnableCodeTask(idTask) {
    $.ajax({
        method: "PUT",
        url: gOptions.aws_path + "/tasksrest/disableCodeTask/" + idTask,
        success: function () {
            $("#myModal").modal('show');
            async function sl() {
                window.location.href = gOptions.aws_path + "/tasks/";
            }
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

