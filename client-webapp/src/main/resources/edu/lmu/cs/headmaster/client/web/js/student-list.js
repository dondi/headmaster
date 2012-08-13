$(function () {
    var addressList = "",

        // Helper function for building email address links. Also builds up the
        // cumulative email list.
        getEmailMarkup = function (email) {
            addressList += (addressList ? "," : "") + email;
            return '<a class="email" href="mailto:' + email + '">' + email + "</a>";
        };

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
                (student.primaryEmail ? getEmailMarkup(student.primaryEmail) : "") +
                (student.secondaryEmail ?
                        (student.primaryEmail ? ", " : "") +
                                getEmailMarkup(student.secondaryEmail) : "") +
                "</td>" +
                ($("#student-query").data("columns") ?
                        $("#student-query").data("columns")(student) : "") +
                "</tr>"
            ).click(function () {
                location = student.id;
            })
        },

        $("#student-query").data("query"),

        function () {
            if (addressList) {
                $("#email-button").attr({ href: "mailto:" + addressList });
            } else {
                $("#email-button").fadeOut();
            }
        }
    );
});
