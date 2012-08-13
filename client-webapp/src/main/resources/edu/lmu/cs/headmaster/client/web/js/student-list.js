$(function () {
    // Load up students based on the "query" data property of the #student-query
    // element.
    Headmaster.loadJsonArrayIntoTable(
        Headmaster.serviceUri("students"),
        "student-list-progress",
        "student-list",
        "student-list-empty",

        function (student) {
            return $(
                "<tr><td>" +
                student.firstName + " " + student.lastName +
                "</td><td>" +
                student.primaryEmail +
                        (student.secondaryEmail ? ", " + student.secondaryEmail : "") +
                "</td></tr>"
            ).click(function () {
                location = student.id;
            })
        },

        $("#student-query").data("query")
    );
});
