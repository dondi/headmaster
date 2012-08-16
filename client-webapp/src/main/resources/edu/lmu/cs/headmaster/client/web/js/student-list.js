$(function () {
    var addressList = "",

        /*
         * Helper function for building email address links. Also builds up the
         * cumulative email list.
         */
        getEmailMarkup = function (email) {
            addressList += (addressList ? "," : "") + email;
            return $('<a class="email"></a>').attr({ href: "mailto:" + email }).text(email);
        },

        /*
         * Convenience function for stopping "click-through" in row checkboxes.
         */
        stopProp = function (event) {
            event.stopPropagation();
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
                .append(
                    $("<td></td>").append(
                        $('<input type="checkbox" class="rowcheck" ' +
                                (student.primaryEmail || student.secondaryEmail ?
                                        "" : 'disabled="disabled"') + "/>").click(stopProp)
                    )
                ).append($("<td></td>").append(
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

        function (studentArray) {
            // Assign the list of all addresses.
            if (addressList) {
                $("#email-all-button").attr({ href: "mailto:" + addressList });
            } else {
                $("#email-all-button").fadeOut();
            }

            // Set the student count.
            $("#student-list-count").text(studentArray.length);
        }
    );

    // Sending email to checked can't just be an anchor; it requires additional
    // logic.
    $("#email-checked-button").click(function () {
        // Gather the checked rows.
        // Derive their email addresses.
        // Issue to email link.
    });

});
