$(function () {
    var addressList = "",

        /*
         * Helper function for building email address links. Also builds up the
         * cumulative email list.
         */
        getEmailElement = function (email) {
            addressList += (addressList ? "," : "") + email;
            return Headmaster.getEmailElement(email).addClass("email");
        },

        /*
         * Helper function for building elements indicating a student's college(s).
         */
        getCollegeElements = function (student) {
            return Headmaster.loadArrayIntoUnorderedList(student.majors, "collegeOrSchool");
        },

        /*
         * Helper function for building elements indicating a student's major(s).
         */
        getMajorElements = function (student) {
            return Headmaster.loadArrayIntoUnorderedList(student.majors, "discipline");
        },

        /*
         * UI feedback function that updates components that depend on others,
         * such as the "Send Email to Checked" button (which depends on whether
         * there are checked rows).
         */
        updateDependentElements = function () {
            // We iterate because we want to break out.
            var checkboxes = $("input.rowcheck"), i, max, jCheckbox;

            for (i = 0, max = checkboxes.length; i < max; i += 1) {
                jCheckbox = $(checkboxes[i]);

                // Derive the email address(es) associated with that checkbox.
                if (jCheckbox.attr("checked") === "checked") {
                    // A single check does the trick.
                    $("#email-checked-button").removeAttr("disabled").removeClass("disabled");
                    return;
                }
            }

            // If we reach here, nothing was checked.
            $("#email-checked-button").attr({ disabled: "disabled" }).addClass("disabled");
        },

        /*
         * Convenience function for stopping "click-through" in row checkboxes.
         */
        stopProp = function (event) {
            event.stopPropagation();
            updateDependentElements();
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
                )
                .append($('<td class="emailcol"></td>').append(
                        student.primaryEmail ? getEmailElement(student.primaryEmail) : null
                    ).append(
                        student.primaryEmail && student.secondaryEmail ? $("<br/>") : null
                    ).append(
                        student.secondaryEmail ? getEmailElement(student.secondaryEmail) : null
                    )
                )
                .append($("<td></td>").append(getCollegeElements(student)))
                .append($("<td></td>").append(getMajorElements(student)))
                .append($("#student-query").data("columns") ?
                        $("#student-query").data("columns")(student) : null)
                .click(function () {
                    location = student.id;
                });
        },

        $("#student-query").data("query"),

        function (studentArray) {
            // Assign the list of all addresses.
            if (addressList) {
                $("#email-all-button").attr({ href: "mailto:" + addressList });
            } else {
                $("#email-all-button, #email-checked-button").fadeOut();
            }

            // Set the student count.
            $("#student-list-count").text(studentArray.length);

            // Update any elements whose state depends on others.
            updateDependentElements();
        }
    );

    // Sending email to checked can't just be an anchor; it requires additional
    // logic.
    $("#email-checked-button").click(function () {
        var checkedAddresses = "";

        // Gather the checked rows.
        //
        // FIXME For some reason jQuery cannot select the
        // already-checked checkboxes---that would tighten this
        // code up a bit.
        $("input.rowcheck").each(function (index, checkbox) {
            var jCheckbox = $(checkbox);

            // Derive the email address(es) associated with that checkbox.
            if (jCheckbox.attr("checked") === "checked") {
                jCheckbox.parent().parent().find("td.emailcol > a.email").each(
                    function (index, a) {
                        checkedAddresses += (checkedAddresses ? "," : "") + $(a).text();
                    }
                );
            }
        });

        // Issue the email link, if we have addresses.
        if (checkedAddresses) {
            location = "mailto:" + checkedAddresses;
        }
    });

});
