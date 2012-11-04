$(function () {
    // Retrieve the ID that we were given.
    var grantId = $("#grant-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",
        YES = "Yes",
        NO = "No",

        /*
         * Checks the state of the student search result tables.
         * An empty table is hidden and replaced with its accompanying empty
         * indicator. If it is not empty, it is displayed and the empty
         * indicator is hidden.
         */
        showOrHideStudentTables = function () {
            Headmaster.toggleElements(
                $("#grant-students > tbody > tr").length,
                $("#grant-students"),
                $("#grant-students-empty")
            );

            Headmaster.toggleElements(
                $("#search-results > tbody > tr").length,
                $("#search-results"),
                $("#search-empty")
            );
        },

        /*
         * Creates a tr element representing a student.
         */
        createStudentTableRow = function (student) {
            var td = $("<td></td>").text(student.firstName + " " + student.lastName),
                tr = $("<tr></tr>");

            // Include a remove button.
            $('<i class="icon-remove-sign pull-right"></i>')
                .appendTo(td)
                .click(function () {
                    tr.remove();
                    showOrHideStudentTables();
                });

            // Save the actual object as data on that row.
            return tr.data("student", student).append(td).click(function () {
                window.open("../../students/" + student.id, "_blank");
            });
        },

        /*
         * Checks whether the given student already has a row in the students
         * table.
         */
        isStudent = function (student) {
            // We don't use $.each because we might want to break out of the
            // loop.
            var rows = $("#grant-students > tbody > tr"), i, max;

            // Comparison is by student id only.
            for (i = 0, max = rows.length; i < max; i += 1) {
                if ($(rows[i]).data("student").id === student.id) {
                    return true;
                }
            }

            // If we get here, we did not find the student.
            return false;
        },

        /*
         * Helper function for updating elements that depend on the value of
         * another element.
         */
        updateDependentElements = function () {
            var updateTableVisibility = function (table, empty) {
                    Headmaster.toggleElements(table.find("tbody > tr").length, table, empty);
                };

            updateTableVisibility($("#grant-types"), $("#grant-types-empty"));
        },

        // Indicates whether the current user may edit the student record.
        canEditGrantRecord = false,

        /*
         * Helper function for creating a row removal element for the given
         * table row (tr).
         */
        createRemoveElement = function (tr) {
            return $("<i></i>")
                .addClass("icon-remove-sign pull-right")
                .click(function () {
                    tr.remove();
                });
        },

        /*
         * Helper function for creating the standard in-place editing controls.
         */
        addEditableRowIcons = function (container, confirmFunction, restoreFunction) {
            container.append(
                $("<button></button>").addClass("btn btn-danger")
                    .append($("<i></i>").addClass("icon-ban-circle icon-white"))
                    .click(function (event) {
                        // No harm no foul---just restore the row.
                        restoreFunction(event);
                    })
            ).append(
                $("<button></button>").addClass("btn btn-success")
                    .append($("<i></i>").addClass("icon-ok icon-white"))
                    .click(function (event) {
                        // Finalize the edit.
                        confirmFunction();
                        restoreFunction(event);
                    })
            );
        },

        /*
         * Sets up the given (presumed input) element as a typeahead for the
         * given uri.
         */
        setUpTypeahead = function (input, uri) {
            input.typeahead({
                source: function (query, process) {
                    $.getJSON(
                        Headmaster.serviceUri(uri),
                        { q: query },
                        process
                    );
                }
            });
        },

        /*
         * Helper function that changes a minor table row from a read-only to an
         * editable one.  Much less involved than the one for majors.
         */
        makeTypeTableRowEditable = function (tr) {
            tr.click(function () {
                var td = tr.find("td"),
                    type = td.text(),

                    // Create the editable element.
                    rowType = $("<input/>")
                        .attr({ type: "text" })
                        .addClass("input-xlarge search-query")
                        .val(type),

                    // Get this row back to its pre-editable state.  We also stop propagation on
                    // the event that triggered the restore so that we don't cycle back to being
                    // editable.
                    restoreTypeTableRow = function (grant) {
                        td.empty().removeClass("form-search")
                            .text(type)
                            .append(createRemoveElement(tr));
                        makeTypeTableRowEditable(tr);
                        grant.stopPropagation();
                    },

                    // Create an input-append container.
                    container = $("<div></div>").addClass("input-append").append(rowType);

                // Set up typeahead elements.
                setUpTypeahead(rowType, "terms/disciplines");

                // Clear what was there...
                td.empty()
                    .addClass("form-search")
                    .append(container);

                // Finally, the buttons.
                addEditableRowIcons(
                    container,
                    function () {
                        type = rowType.val();
                    },
                    restoreTypeTableRow
                );

                // Finally, disengage this very handler.
                tr.unbind("click");
            });
        },

        /*
         * Helper function for creating a table row displaying a type.
         */
        createTypeTableRow = function (string) {
            var tr = $("<tr></tr>");

            // Support in-place edits.
            makeTypeTableRowEditable(tr);

            return tr.append($("<td></td>")
                    .text(string)
                    .append(createRemoveElement(tr)));
        };


    if (grantId) {
        $.getJSON(
            Headmaster.serviceUri("grants/" + grantId),
            function (data, textStatus, jqXHR) {
                $("#grant-submissiondate").val(data.submissiondate ?
                        Date.parse(data.submissiondate).toString(DATE_FORMAT) : BLANK);
                $("#grant-title").val(data.title || BLANK);
                $("#grant-description").val(data.description || BLANK);
                $("#grant-amount").val(data.amount || BLANK);
                $("#grant-notes").val(data.notes || BLANK);
                $("#grant-facultymentor").val(data.facultymentor || BLANK);
                $("#grant-presented-" + (data.presented ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#grant-presented-" + (data.presented ? "no" : "yes"))
                    .removeAttr("checked");
                $("#grant-awarded-pending").removeAttr("checked");
                $("#grant-awarded-awarded").removeAttr("checked");
                $("#grant-awarded-declined").removeAttr("checked");
                $("#grant-awarded-" + (data.awarded).toLowerCase()).attr({ checked: "checked" });

                // List the types.
                Headmaster.loadArrayIntoTable(
                    data.type.split(","),
                    "grant-types",
                    "grant-types-empty",
                    createTypeTableRow
                );

                // List the students.
                Headmaster.loadArrayIntoTable(
                    data.students,
                    "grant-students",
                    "grant-students-empty",
                    createStudentTableRow
                );
            }
        );
    } else {
        // Otherwise, we start empty.
        $("#grant-notes, #grant-amount, #grant-facultymentor, #grant-submissiondate, #grant-title, #grant-description").val(BLANK);
        $("#grant-students").fadeOut();
        $("#grant-students-empty").fadeIn();
    }

    // Search field setup.
    Headmaster.setupSearchField(
        $("#search-field"),
        $("#search-progress"),
        $("#search-empty"),
        $("#search-results"),
        Headmaster.serviceUri("students"),
        "q",
        function (student) {
            return isStudent(student) ? null :
                $("<tr></tr>")
                    .append($("<td></td>").text(student.firstName + " " + student.lastName))
                    .click(function () {
                        // Add the clicked student to the students table...
                        $("#grant-students > tbody")
                            .append(createStudentTableRow(student));

                        // ...then remove it from this one.
                        $(this).remove();
                        showOrHideStudentTables();
                    });
        }
    );

    // Button click handling.
    $("#grant-cancel").click(function (grant) {
        history.go(-1);
        grant.preventDefault();
    });

    $("#grant-save").click(function (grant) {
        // Grab the data from the web page.
        var grantSubmissionDate = $("#grant-submissiondate").val(),
            grantData = {
            id: grantId,
            title: $("#grant-title").val(),
            description: $("#grant-description").val(),
            submissionDate: grantSubmissionDate || new Date(),
            facultymentor: $("#grant-facultymentor").val(),
            amount: parseInt($("#grant-amount").val().replace(/[^0-9]/g,"")),
            notes: $("#grant-notes").val(),
            presented: Headmaster.isChecked("grant-presented-yes"),
            awarded: $("input:radio[name=grant-awarded]").val().toUpperCase(),
            type: "",
            students: []
        },

        // Gather type data.
        // Not using Headmaster's loadTableIntoArray because I didn't want to
        // bother learning how to work it.
        grantTypes = $("#grant-types > tbody > tr").map(function() {
            return $(this).text().replace(/[,]/g,"");
        }).get().toString();
        grantTypes ? grantData.type = grantTypes : delete grantData.type;

        // Gather student data.
        Headmaster.loadTableIntoArray(
            grantData, "students", $("#grant-students > tbody > tr"),
            function (tr) {
                return $(tr).data("student");
            }
        );

        // Ditch the id attribute if it is empty.
        if (!grantData.id) {
            delete grantData.id;
        }

        // Convert the date into a string that the service will parse correctly.
        Headmaster.dateToDateString(grantData, "submissionDate");

        // Ajax call.
        $.ajax({
            type: grantData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("grants" + (grantData.id ? "/" + grantData.id : "")),
            data: JSON.stringify(grantData),
            success: function () {
                $("#grant-success").fadeIn();

                // If there is no grantId, then we are creating grants, in which
                // case we clear the form in case more grants are to be created.
                if (!grantId) {
                    $("form input, form textarea").val("");
                } else {
                    location = "../" + eventId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#grant-success").fadeOut();
                }, 5000);
            },
            contentType: "application/json",
            dataType: "json"
        });

        grant.preventDefault();
    });

    // Grant types can be manually ordered---something that is doable more
    // easily than with Bootstrap.
    $("grant-types > tbody").sortable({
        // For the helper, we provide almost the same thing, but without the
        // remove element.
        helper: function (event, element) {
            return $("<tr></tr>").append($("<td></td>").text(element.text()));
        }
    });

    // Typeahead setup.
    // TODO: @Dondi, I thought about making another resource endpoint, but couldn't
    //       imagine what typical end points would be used, so I'm using disciplines
    setUpTypeahead($("#grant-types-entry"), "terms/disciplines");

    // Button click handling.
    $("#grant-types-add-button").click(function (event) {
        // Create a new minor (really just a string) then add it to the minors table.
        var grantType = $("#grant-types-entry").val();

        if (grantType) {
            // Add a row for that types to the table.
            $("#grant-types > tbody").append(createTypeTableRow(grantType));
            updateDependentElements();

            // Clear the add section.
            $("#grant-types-container > div > input").val("");
        }
    });

    // Set up other interactive components.
    $(".collapse").collapse();
});
