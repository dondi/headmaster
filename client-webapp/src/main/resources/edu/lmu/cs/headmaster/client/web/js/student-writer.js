$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",
        YES = "Yes",
        NO = "No",

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

    // Datepicker setup.
    $("#student-honorsentrydate, #student-thesis-submissiondate").datepicker();

    // Majors and minors can be manually ordered---something that is doable more
    // easily than with Bootstrap.
    $("#student-majors > tbody, #student-minors > tbody").sortable({
        // For the helper, we provide almost the same thing, but without the
        // remove element.
        helper: function (event, element) {
            return $("<tr></tr>").append($("<td></td>").text(element.text()));
        }
    });

    // If supplied, load up the student with that ID.
    if (studentId) {
        $.getJSON(
            Headmaster.serviceUri("students/" + studentId),
            function (data, textStatus, jqXHR) {
                // Student name and graduation year.
                $("#student-firstname").val(data.firstName || BLANK);
                $("#student-middlename").val(data.middleName || BLANK);
                $("#student-lastname").val(data.lastName || BLANK);
                $("#student-gradyear").val(data.expectedGraduationYear);
                $("#student-active")
                    .removeAttr(data.active ? null : "checked")
                    .attr(data.active ? { checked: "checked" } : null);

                // Contact information.
                $("#student-email1").val(data.primaryEmail || BLANK);
                $("#student-email2").val(data.secondaryEmail || BLANK);
                $("#student-schoolid").val(data.schoolId || BLANK);
                $("#student-campus-box").val(data.campusBox || BLANK);
                $("#student-address").val(data.address || BLANK);
                $("#student-city").val(data.city || BLANK);
                $("#student-state").val(data.state || BLANK);
                $("#student-zip").val(data.zip || BLANK);
                $("#student-phone-main").val(data.mainPhone || BLANK);
                $("#student-phone-cell").val(data.cellPhone || BLANK);

                // Academic information.
                $("#student-advisor").val(data.advisor || BLANK);
                $("#student-gpa").val(data.cumulativeGpa ? data.cumulativeGpa.toFixed(2) : BLANK);
                $("#student-status").val(data.academicStatus || BLANK);

                // Majors and minors.
                Headmaster.loadArrayIntoTable(
                    data.majors, "student-majors", "student-majors-empty", createMajorTableRow
                );

                Headmaster.loadArrayIntoTable(
                    data.minors, "student-minors", "student-minors-empty", createMinorTableRow
                );

                // Status information.
                $("#student-compact-" + (data.compactSigned ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-compact-" + (data.compactSigned ? "no" : "yes"))
                    .removeAttr("checked");

                $("#student-inllc-" + (data.inLivingLearningCommunity ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-inllc-" + (data.inLivingLearningCommunity ? "no" : "yes"))
                    .removeAttr("checked");

                $("#student-transfer-" + (data.transferStudent ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-transfer-" + (data.transferStudent ? "no" : "yes"))
                    .removeAttr("checked");

                // TODO This is better done as a select element.
                $("#student-residencycode").val(data.residencyCode);

                $("#student-studyabroad-" + (data.hasStudiedAbroad ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-studyabroad-" + (data.hasStudiedAbroad ? "no" : "yes"))
                    .removeAttr("checked");

                // Demographics information.
                $("#student-sex-" + (data.sex === "FEMALE" ? "female" : "male"))
                    .attr({ checked: "checked" });
                $("#student-sex-" + (data.sex === "FEMALE" ? "male" : "female"))
                    .removeAttr("checked");

                $("#student-raceorethnicity").val(data.raceOrEthnicity || BLANK);

                // Entry information.
                $("#student-entryyear").val(data.entryYear || BLANK);
                $("#student-honorsentrydate").val(data.honorsEntryDate ?
                        Date.parse(data.honorsEntryDate).toString(DATE_FORMAT) : BLANK);
                $("#student-hsgpa").val(data.highSchoolGpa || BLANK);
                $("#student-act").val(data.actScore || BLANK);
                $("#student-sat-verbal").val(data.satVerbalScore || BLANK);
                $("#student-sat-math").val(data.satMathScore || BLANK);
                $("#student-sat-writing").val(data.satWritingScore || BLANK);
                $("#student-scholarship").val(data.scholarship || BLANK);

                // Notes.
                $("#student-notes").val(data.notes || BLANK);

                // Grade information.
                Headmaster.loadArrayIntoTable(
                    data.grades, "student-grades", "student-grades-empty",
                    function (gpa) {
                        return $("<tr></tr>")
                            .append($("<td></td>").text(gpa.term + " " + gpa.year))
                            .append($("<td></td>").text(gpa.gpa.toFixed(2)))

                            // Save the actual object as data on that row.
                            .data("gpa", gpa);
                    }
                );

                // Thesis information.
                $("#student-thesis-title").val(data.thesisTitle || BLANK);
                $("#student-thesis-term-" + (data.thesisTerm === "FALL" ? "fall" : "spring"))
                    .attr({ checked: "checked" });
                $("#student-thesis-term-" + (data.thesisTerm === "FALL" ? "spring" : "fall"))
                    .removeAttr("checked");
                $("#student-thesis-year").val(data.thesisYear || data.expectedGraduationYear);
                $("#student-thesis-advisor").val(data.thesisAdvisor || BLANK);

                $("#student-thesis-inmajor-" + (data.thesisInMajor ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-thesis-inmajor-" + (data.thesisInMajor ? "no" : "yes"))
                    .removeAttr("checked");
                $("#student-thesis-course").val(data.thesisCourse || BLANK);

                $("#student-thesis-submitted-" + (data.thesisSubmissionDate ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-thesis-submitted-" + (data.thesisSubmissionDate ? "no" : "yes"))
                    .removeAttr("checked");
                $("#student-thesis-submissiondate").val(data.thesisSubmissionDate ?
                        Date.parse(data.thesisSubmissionDate).toString(DATE_FORMAT) : BLANK);

                $("#student-thesis-notes").val(data.thesisNotes || BLANK);

                // Set up listeners so that as an accordion opens, the right Ajax call is made
                // (except for grades and thesis, which are known right away).
                $("#student-attendance-container").on("show", function () {
                    Headmaster.loadJsonArrayIntoTable(
                        Headmaster.serviceUri("students/" + studentId + "/attendance"),
                        "student-attendance-progress",
                        "student-attendance",
                        "student-attendance-empty",
                        function (event) {
                            return $("<tr></tr>")
                                .append(
                                    $("<td></td>").text(
                                        event.dateTime ?
                                                Date.parse(event.dateTime).toString(DATE_FORMAT) :
                                                UNSPECIFIED
                                    )
                                ).append(
                                    $("<td></td>").text(event.title || UNSPECIFIED)
                                ).click(function () {
                                    // View that event if the row is clicked.
                                    location = "../../events/" + event.id;
                                });
                        }
                    );

                    // We only load once.
                    $(this).unbind("show");
                });

                // TODO grants

                // Now that values have been set, put dependent user interface
                // elements in the correct initial state.
                updateDependentElements();
            }
        );
    } else {
        // When there is no id, we update the dependent elements and clear out the lists.
        // Update dependent elements in the case that there is no id.
        updateDependentElements();
        $(".progress").fadeOut();
    }

    // Set up event handling so that the above function gets called when necessary.
    $('input[type="radio"]').click(updateDependentElements);

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
        $("#student-majors-container > input").val("");
    });

    $("#student-minors-add-button").click(function (event) {
        // Create a new minor (really just a string) then add it to the minors table.
        var minor = $("#student-minors-discipline").val();

        // Add a row for that minor to the table.
        $("#student-minors > tbody").append(createMinorTableRow(minor));
        updateDependentElements();

        // Clear the add section.
        $("#student-minors-container > input").val("");
    });

    $("#student-cancel").click(function (event) {
        history.go(-1);
        event.preventDefault();
    });

    $("#student-save").click(function (event) {
        // Grab the data from the web page.
        var studentData = {
            id: studentId,

            // Student name and graduation year.
            firstName: $("#student-firstname").val(),
            middleName: $("#student-middlename").val(),
            lastName: $("#student-lastname").val(),
            expectedGraduationYear: $("#student-gradyear").val(),
            active: Headmaster.isChecked("student-active"),

            // Contact information.
            primaryEmail: $("#student-email1").val(),
            secondaryEmail: $("#student-email2").val(),
            schoolId: $("#student-schoolid").val(),
            campusBox: $("#student-campus-box").val(),
            address: $("#student-address").val(),
            city: $("#student-city").val(),
            state: $("#student-state").val(),
            zip: $("#student-zip").val(),
            mainPhone: $("#student-phone-main").val(),
            cellPhone: $("#student-phone-cell").val(),

            // Academic information.
            advisor: $("#student-advisor").val(),
            cumulativeGpa: $("#student-gpa").val(),
            academicStatus: $("#student-status").val(),

            // Majors and minors (to be gathered later).
            majors: [],
            minors: [],

            // Status information.
            compactSigned: Headmaster.isChecked("student-compact-yes"),
            inLivingLearningCommunity: Headmaster.isChecked("student-inllc-yes"),
            transferStudent: Headmaster.isChecked("student-transfer-yes"),
            hasStudiedAbroad: Headmaster.isChecked("student-studyabroad-yes"),
            residencyCode: $("#student-residencycode").val(),

            // Demographics information.
            sex: Headmaster.isChecked("student-sex-female") ? "FEMALE" : "MALE",
            raceOrEthnicity: $("#student-raceorethnicity").val(),

            // Entry information.
            entryYear: $("#student-entryyear").val(),
            honorsEntryDate: Date.parse($("#student-honorsentrydate").val()),
            highSchoolGpa: $("#student-hsgpa").val(),
            actScore: $("#student-act").val(),
            satVerbalScore: $("#student-sat-verbal").val(),
            satMathScore: $("#student-sat-math").val(),
            satWritingScore: $("#student-sat-writing").val(),
            scholarship: $("#student-scholarship").val(),

            // Notes.
            notes: $("#student-notes").val(),

            // Grades (to be gathered later).
            grades: [],

            // Thesis information.
            thesisTitle: $("#student-thesis-title").val(),
            thesisTerm: Headmaster.isChecked("student-thesis-term-fall") ? "FALL" : "SPRING",
            thesisYear: $("#student-thesis-year").val(),
            thesisAdvisor: $("#student-thesis-advisor").val(),
            thesisInMajor: Headmaster.isChecked("student-thesis-inmajor-yes"),
            thesisCourse: $("#student-thesis-course").val(),
            thesisSubmissionDate: Headmaster.isChecked("student-thesis-submitted-yes") ?
                    Date.parse($("#student-thesis-submissiondate").val()) : null,
            thesisNotes: $("#student-thesis-notes").val()
        };

        // Gather array data. Empty arrays don't travel well, though, so
        // we eliminate those.
        Headmaster.loadTableIntoArray(
            studentData, "majors", $("#student-majors > tbody > tr"),
            function (td) {
                return $(td).data("major");
            }
        );

        Headmaster.loadTableIntoArray(
            studentData, "minors", $("#student-minors > tbody td"),
            function (td) {
                return $(td).text();
            }
        );

        Headmaster.loadTableIntoArray(
            studentData, "grades", $("#student-grades > tbody > tr"),
            function (tr) {
                return $(tr).data("gpa");
            }
        );

        // Ditch the id attribute if it is empty.
        if (!studentData.id) {
            delete studentData.id;
        }

        // Convert the dates into strings that the service will parse correctly.
        $.each(["honorsEntryDate", "thesisSubmissionDate"], function (index, propertyName) {
            Headmaster.dateToDateString(studentData, propertyName);
        });

        // Ajax call.
        $.ajax({
            type: studentData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("students" + (studentData.id ? "/" + studentData.id : "")),
            data: JSON.stringify(studentData),
            success: function (data, textStatus, jqXHR) {
                $("#student-success").fadeIn();

                // If there is no studentId, then we are creating students,
                // in which case we clear the form in case more students
                // are to be created.
                if (!studentId) {
                    $("input, textarea").val("");
                    $("#student-new-link")
                        .attr({ href: jqXHR.getResponseHeader("Location").split("/").pop() })
                        .fadeIn();
                } else {
                    location = "../" + studentId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#student-new-link, #student-success").fadeOut();
                }, 5000);
            },
            contentType: "application/json",
            dataType: "json"
        });

        event.preventDefault();
    });

    // Set up other interactive components.
    $(".collapse").collapse();
});
