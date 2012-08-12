$(function () {
    Headmaster.setupSearchField(
        $("#search-field"), $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("students"), "q",
        function (student) {
            return $(
                    "<tr><td>" +
                    student.firstName + " " + student.lastName +
                    "</td></tr>"
                ).click(function () {
                    location = student.id;
                });
        }
    );
});
