$(function () {
    var addressList = "",

        // Helper function for building email address links. Also builds up the
        // cumulative email list.
        getEmailMarkup = function (email) {
            addressList += (addressList ? "," : "") + email;
            return $('<a class="email"></a>').attr({ href: "mailto:" + email }).text(email);
        };

    // Load up students based on the "query" data property of the #student-query
    // element.
    Headmaster.loadJsonArrayIntoTable(
        Headmaster.serviceUri("students"),
        "student-list-progress",
        "student-list",
        "student-list-empty",

        function (student) {
            return $("<tr></tr>")
                .append($("<td></td>").text(student.firstName + " " + student.lastName))
                .append($("<td></td>").append(
                        student.primaryEmail ? getEmailMarkup(student.primaryEmail) : null
                    ).append(
                        student.primaryEmail && student.secondaryEmail ? $("<br/>") : null
                    ).append(
                        student.secondaryEmail ? getEmailMarkup(student.secondaryEmail) : null
                    )
                ).append($("#student-query").data("columns") ?
                        $("#student-query").data("columns")(student) : null)
                .click(function () {
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
