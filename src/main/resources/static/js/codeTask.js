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