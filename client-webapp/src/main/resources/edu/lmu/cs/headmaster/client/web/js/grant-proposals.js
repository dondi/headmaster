$(function () {
var TABLE_FORMAT = "M/d/yyyy",
    getGrants = function (tableId) {
        Headmaster.loadJsonArrayIntoTable(
            Headmaster.serviceUri("grants"),
            tableId + "-progress",
            tableId,
            tableId + "-empty",

            function (grant) {
                var title = grant.title || "",
                    description = grant.description || "",
                    submissionDate = grant.submissionDate || "",
                    type = grant.type || "",
                    facultyMentor = grant.facultyMentor || "",
                    students = grant.students || "",
                    notes = grant.notes || "";
                return $("<tr></tr>")
                    .append($("<td></td>").text(title))
                    .append($("<td></td>").text(description))
                    .append($("<td></td>").text(submissionDate))
                    .append($("<td></td>").text(type))
                    .append($("<td></td>").text(facultyMentor))
                    //this is going to be a problem
                    .append($("<td></td>").text(students))
                    .append($("<td></td>").text(notes))
                    .click(function () {
                        location = grant.id;
                    });
            },

            {
                awarded: "PENDING",
            }
        );
    };
    getGrants("list-this");
});
