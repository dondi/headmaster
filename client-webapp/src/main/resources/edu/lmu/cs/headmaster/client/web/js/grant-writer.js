$(function () {
    // Retrieve the ID that we were given.
    var grantId = $("#grant-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",
        YES = "Yes",
        NO = "No",

        /*
         * Checks the state of the attendees and student search result tables.
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
         * Creates a tr element representing an attending student.
         */
        createAttendeeTableRow = function (student) {
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
         * Checks whether the given student already has a row in the attendees
         * table.
         */
        isAttendee = function (student) {
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
        };

    if (grantId) {
        $.getJSON(
            Headmaster.serviceUri("grants/" + eventId),
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
                $("#grant-awarded-" + toLower(data.awarded)).attr({ checked: "checked" });

                // List the attendees.
                Headmaster.loadArrayIntoTable(
                    data.students,
                    "grant-students",
                    "grant-students-empty",
                    createAttendeeTableRow
                );
            }
        );
    } else {
        // Otherwise, we start empty.
        $("#grant-notes, #grant-amount, #grant-facultymentor, #grant-submissiondate, #grant-title, #grant-description").val(BLANK);
        $("#grant-students").fadeOut();
        $("#grant-students-empty").fadeIn();
    }

    // Datepicker setup.
//    $("#grant-submissiondate").datepicker();

    // Search field setup.
    Headmaster.setupSearchField(
        $("#search-field"), $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("grants"), "q",
        function (grant) {
            return isAttendee(grant) ? null :
                $("<tr></tr>")
                    .append($("<td></td>").text(student.firstName + " " + student.lastName))
                    .click(function () {
                        // Add the clicked student to the attendee table...
                        $("#grant-students > tbody")
                            .append(createAttendeeTableRow(student));

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
        var grantData = {
            id: grantId,
            title: $("#grant-title").val(),
            description: $("#grant-description").val(),
            submissionDate: new Date(),
            facultymentor: $("#grant-facultymentor").val(),
            amount: $("grant-amount").val(),
            notes: $("grant-notes").val(),
/*
    private String type;
    private Boolean awarded;
    private List<Student> students = new ArrayList<Student>();
    private Boolean presented;
*/
            // To be filled out below.
            attendees: []
        };

        // Gather attendee data.
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

                // If there is no eventId, then we are creating events,
                // in which case we clear the form in case more events
                // are to be created.
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

        // Indicates whether the current user may edit the student record.
        canEditGrantRecord = false,

        /*
         * Helper function for updating elements that depend on the value of
         * another element.
         */
        updateDependentElements = function () {
            var inMajor = Headmaster.isChecked("student-thesis-inmajor-yes"),
                thesisSubmitted = Headmaster.isChecked("student-thesis-submitted-yes"),

                updateTableVisibility = function (table, empty) {
                    Headmaster.toggleElements(table.find("tbody > tr").length, table, empty);
                };

            $("#student-thesis-course-container *")
                .removeClass(inMajor ? "disabled" : "")
                .addClass(inMajor ? "" : "disabled");
            $("#student-thesis-course")
                .removeAttr(!inMajor ? "" : "disabled")
                .attr(inMajor ? {} : { disabled: "disabled" });

            $("#student-thesis-submissiondate-container *")
                .removeClass(thesisSubmitted ? "disabled" : "")
                .addClass(!thesisSubmitted ? "" : "disabled");
            $("#student-thesis-submissiondate")
                .removeAttr(!thesisSubmitted ? "" : "disabled")
                .attr(thesisSubmitted ? {} : { disabled: "disabled" });

            // Show/hide table elements vs. their empty indicators.
            updateTableVisibility($("#student-majors"), $("#student-majors-empty"));
            updateTableVisibility($("#student-minors"), $("#student-minors-empty"));
            updateTableVisibility($("#student-attendance"), $("#student-attendance-empty"));
            updateTableVisibility($("#student-grades"), $("#student-grades-empty"));
            updateTableVisibility($("#student-grants"), $("#student-grants-empty"));
        },

        /*
         * Helper function for creating a row removal element for the given
         * table row (tr).
         */
        createRemoveElement = function (tr) {
            return $("<i></i>")
                .addClass("icon-remove-sign pull-right")
                .click(function () {
                    tr.remove();
                    updateDependentElements();
                });
        },

        /*
         * Helper function that provides the standardized text representation
         * for a major.
         */
        getMajorAsText = function (major) {
            return (major.degree ? major.degree + " " : BLANK) +
                    (major.discipline || BLANK) +
                    (major.collegeOrSchool ? ", " + major.collegeOrSchool : BLANK);
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
         * Helper function that changes a major table row from a read-only to an
         * editable one.
         */
        makeMajorTableRowEditable = function (tr) {
            tr.click(function () {
                // The center of it all: the current model object for the major.
                var major = tr.data("major"),

                    // Create the editable elements.
                    rowCollegeOrSchool = $("<input/>")
                        .attr({ type: "text" })
                        .addClass("input-small search-query")
                        .val(major.collegeOrSchool),

                    rowDegree = $("<input/>")
                        .attr({ type: "text" })
                        .addClass("input-mini")
                        .val(major.degree),

                    rowDiscipline = $("<input/>")
                        .attr({ type: "text" })
                        .addClass("input-medium")
                        .val(major.discipline),

                    td = tr.find("td"),

                    // Get this row back to its pre-editable state.  We also stop propagation on
                    // the event that triggered the restore so that we don't cycle back to being
                    // editable.
                    restoreMajorTableRow = function (event) {
                        td.empty().removeClass("form-search")
                            .text(getMajorAsText(major))
                            .append(createRemoveElement(tr));
                        makeMajorTableRowEditable(tr);
                        event.stopPropagation();
                    },

                    // Create an input-append container.
                    container = $("<div></div>").addClass("input-append major")
                        .append(rowCollegeOrSchool)
                        .append(rowDegree)
                        .append(rowDiscipline);

                // Set up the typeahead elements.
                setUpTypeahead(rowCollegeOrSchool, "terms/colleges-or-schools");
                setUpTypeahead(rowDegree, "terms/degrees");
                setUpTypeahead(rowDiscipline, "terms/disciplines");

                // Clear what was there...
                td.empty()
                    // ...then add the new elements.
                    .addClass("form-search")
                    .append(container);

                // Finally, the buttons.
                addEditableRowIcons(
                    container,
                    function () {
                        // Finalize the edit.  Note how this preserves the major's
                        // id, which is exactly how we want it to work.
                        major.collegeOrSchool = rowCollegeOrSchool.val();
                        major.degree = rowDegree.val();
                        major.discipline = rowDiscipline.val();
                    },
                    restoreMajorTableRow
                );

                // Finally, disengage this very handler.
                tr.unbind("click");
            });
        },

        /*
         * Helper function for creating a table row displaying a major.
         */
        createMajorTableRow = function (major) {
            var tr = $("<tr></tr>");

            // Support in-place edits.
            makeMajorTableRowEditable(tr);

            return tr.append($("<td></td>")
                    .text(getMajorAsText(major))
                    .append(createRemoveElement(tr))
                )

                // Save the actual object as data on that row.
                .data("major", major);
        },

        /*
         * Helper function that changes a minor table row from a read-only to an
         * editable one.  Much less involved than the one for majors.
         */
        makeMinorTableRowEditable = function (tr) {
            tr.click(function () {
                var td = tr.find("td"),
                    minor = td.text(),

                    // Create the editable element.
                    rowDiscipline = $("<input/>")
                        .attr({ type: "text" })
                        .addClass("input-xlarge search-query")
                        .val(minor),

                    // Get this row back to its pre-editable state.  We also stop propagation on
                    // the event that triggered the restore so that we don't cycle back to being
                    // editable.
                    restoreMinorTableRow = function (event) {
                        td.empty().removeClass("form-search")
                            .text(minor)
                            .append(createRemoveElement(tr));
                        makeMinorTableRowEditable(tr);
                        event.stopPropagation();
                    },

                    // Create an input-append container.
                    container = $("<div></div>").addClass("input-append").append(rowDiscipline);

                // Set up typeahead elements.
                setUpTypeahead(rowDiscipline, "terms/disciplines");

                // Clear what was there...
                td.empty()
                    // ...then add the editable element.
                    .addClass("form-search")
                    .append(container);

                // Finally, the buttons.
                addEditableRowIcons(
                    container,
                    function () {
                        minor = rowDiscipline.val();
                    },
                    restoreMinorTableRow
                );

                // Finally, disengage this very handler.
                tr.unbind("click");
            });
        },

        /*
         * Helper function for creating a table row displaying a minor.
         */
        createMinorTableRow = function (string) {
            var tr = $("<tr></tr>");

            // Support in-place edits.
            makeMinorTableRowEditable(tr);

            return tr.append($("<td></td>")
                    .text(string)
                    .append(createRemoveElement(tr)));
        };

    // Majors and minors can be manually ordered---something that is doable more
    // easily than with Bootstrap.
    $("#student-majors > tbody, #student-minors > tbody").sortable({
        // For the helper, we provide almost the same thing, but without the
        // remove element.
        helper: function (event, element) {
            return $("<tr></tr>").append($("<td></td>").text(element.text()));
        }
    });

    // Set up event handling so that the updateDependentElements function gets
    // called when radio buttons are clicked (because many elements depend on
    // them).
    $('input[type="radio"]').click(updateDependentElements);

    // Typeahead setup.
    setUpTypeahead($("#student-majors-college-or-school"), "terms/colleges-or-schools");
    setUpTypeahead($("#student-majors-degree"), "terms/degrees");
    setUpTypeahead($("#student-majors-discipline"), "terms/disciplines");
    setUpTypeahead($("#student-minors-discipline"), "terms/disciplines");

    // Button click handling.
    $("#student-majors-add-button").click(function (event) {
        // Create a new major from the fields then add it to the majors table.
        var major = {
            collegeOrSchool: $("#student-majors-college-or-school").val(),
            degree: $("#student-majors-degree").val(),
            discipline: $("#student-majors-discipline").val()
        };

        // Add a row for that major to the table.
        $("#student-majors > tbody").append(createMajorTableRow(major));
        updateDependentElements();

        // Clear the add section.
        $("#student-majors-container > div > input").val("");
    });

    $("#student-minors-add-button").click(function (event) {
        // Create a new minor (really just a string) then add it to the minors table.
        var minor = $("#student-minors-discipline").val();

        // Add a row for that minor to the table.
        $("#student-minors > tbody").append(createMinorTableRow(minor));
        updateDependentElements();

        // Clear the add section.
        $("#student-minors-container > div > input").val("");
    });

    // Set up other interactive components.
    $(".collapse").collapse();
});
