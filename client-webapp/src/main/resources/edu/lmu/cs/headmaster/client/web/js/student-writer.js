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
                thesisSubmitted = Headmaster.isChecked("student-thesis-submitted-yes");

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
        };

    // Datepicker setup.
    $("#student-honorsentrydate, #student-thesis-submissiondate").datepicker();

    // If supplied, load up the student with that ID.
    if (studentId) {
        $.getJSON(
            Headmaster.serviceUri("students/" + studentId),
            function (data, textStatus, jqXHR) {
                var createRowFromMajor = function (major) {
                    return $("<tr></tr>").append($("<td></td>")
                            .text(major.degree + " " + major.discipline));
                },
    
                createRowFromString = function (string) {
                    return $("<tr></tr>").append($("<td></td>")
                            .text(string));
                };

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
                $("#student-campus-box").val(data.campusBox || BLANK);
                $("#student-address").val(data.address || BLANK);
                $("#student-city").val(data.city || BLANK);
                $("#student-state").val(data.state || BLANK);
                $("#student-zip").val(data.zip || BLANK);
                $("#student-phone-main").val(data.mainPhone || BLANK);
                $("#student-phone-cell").val(data.cellPhone || BLANK);

                // Academic information.
                $("#student-college").val(data.college || BLANK);
                $("#student-advisor").val(data.advisor || BLANK);
                $("#student-degree").val(data.degree || BLANK);
                $("#student-gpa").val(data.cumulativeGpa ? data.cumulativeGpa.toFixed(2) : BLANK);
                $("#student-status").val(data.academicStatus || BLANK);

                // Majors and minors.
                Headmaster.loadArrayIntoTable(data.majors, "student-majors", "student-majors-empty", createRowFromMajor);
                Headmaster.loadArrayIntoTable(data.minors, "student-minors", "student-minors-empty", createRowFromString);

                // Status information.
                $("#student-inllc-" + (data.inLivingLearningCommunity ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-inllc-" + (data.inLivingLearningCommunity ? "no" : "yes"))
                    .removeAttr("checked");

                $("#student-transfer-" + (data.transferStudent ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-transfer-" + (data.transferStudent ? "no" : "yes"))
                    .removeAttr("checked");

                $("#student-studyabroad-" + (data.hasStudiedAbroad ? "yes" : "no"))
                    .attr({ checked: "checked" });
                $("#student-studyabroad-" + (data.hasStudiedAbroad ? "no" : "yes"))
                    .removeAttr("checked");

                // Entry information.
                $("#student-entryyear").val(data.entryYear || BLANK);
                $("#student-honorsentrydate").val(data.honorsEntryDate ?
                        Date.parse(data.honorsEntryDate).toString(DATE_FORMAT) : BLANK);
                $("#student-hsgpa").val(data.highSchoolGpa || BLANK);
                $("#student-act").val(data.actScore || BLANK);
                $("#student-sat-verbal").val(data.satVerbalScore || BLANK);
                $("#student-sat-math").val(data.satMathScore || BLANK);
                $("#student-sat-writing").val(data.satWritingScore || BLANK);

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
                                    // View that student if the row is clicked.
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
        Headmaster.toggleElements(false, $("#student-majors"), $("#student-majors-empty"));
        Headmaster.toggleElements(false, $("#student-minors"), $("#student-minors-empty"));
        Headmaster.toggleElements(false, $("#student-attendance"), $("#student-attendance-empty"));
        Headmaster.toggleElements(false, $("#student-grades"), $("#student-grades-empty"));
        Headmaster.toggleElements(false, $("#student-grants"), $("#student-grants-empty"));
    }

    // Set up event handling so that the above function gets called when necessary.
    $('input[type="radio"]').click(updateDependentElements);

    // Button click handling.
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
            campusBox: $("#student-campus-box").val(),
            address: $("#student-address").val(),
            city: $("#student-city").val(),
            state: $("#student-state").val(),
            zip: $("#student-zip").val(),
            mainPhone: $("#student-phone-main").val(),
            cellPhone: $("#student-phone-cell").val(),

            // Academic information.
            college: $("#student-college").val(),
            advisor: $("#student-advisor").val(),
            degree: $("#student-degree").val(),
            cumulativeGpa: $("#student-gpa").val(),
            academicStatus: $("#student-status").val(),

            // Majors and minors (to be gathered later).
            majors: [],
            minors: [],

            // Status information.
            inLivingLearningCommunity: Headmaster.isChecked("student-inllc-yes"),
            transferStudent: Headmaster.isChecked("student-transfer-yes"),
            hasStudiedAbroad: Headmaster.isChecked("student-studyabroad-yes"),

            // Entry information.
            entryYear: $("#student-entryyear").val(),
            honorsEntryDate: Date.parse($("#student-honorsentrydate").val()),
            highSchoolGpa: $("#student-hsgpa").val(),
            actScore: $("#student-act").val(),
            satVerbalScore: $("#student-sat-verbal").val(),
            satMathScore: $("#student-sat-math").val(),
            satWritingScore: $("#student-sat-writing").val(),

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
            studentData, "majors", $("#student-majors > tbody td"),
            function (td) {
                return $(td).text();
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
