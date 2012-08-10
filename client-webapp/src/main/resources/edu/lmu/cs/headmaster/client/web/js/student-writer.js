$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",
        YES = "Yes",
        NO = "No",

        // Helper function for updating elements that depend on the value of
        // another element.
        updateDependentElements = function () {
            var inMajor = $("#student-thesis-inmajor-yes").attr("checked"),
                thesisSubmitted = $("#student-thesis-submitted-yes").attr("checked");

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

    // If supplied, load up the student with that ID.
    if (studentId) {
        $.getJSON(
            Headmaster.serviceUri("students/" + studentId),
            function (data, textStatus, jqXHR) {
                // Student name and graduation year.
                $("#student-firstname").val(data.firstName || BLANK);
                $("#student-middlename").val(data.middleInitial || BLANK);
                $("#student-lastname").val(data.lastName || BLANK);
                $("#student-gradyear").val(data.expectedGraduationYear);

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
                $("#student-gpa").val(data.cumulativeGpa || BLANK);
                // TODO majors
                // TODO minors
                $("#student-status").val(data.academicStatus || BLANK);

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
            }
        );
    }

    // Put dependent user interface elements in the correct initial state.
    updateDependentElements();

    // Set up event handling so that the above function gets called when necessary.
    $('input[type="radio"]').click(updateDependentElements);

    // Datepicker setup.
    $("#student-honorsentrydate, #student-thesis-submissiondate").datepicker();

    // Button click handling.
    $("#student-cancel").click(function (event) {
        history.go(-1);
        event.preventDefault();
    });

    $("#student-save").click(function (event) {
        // TODO
        // Grab the data from the web page.
        var studentData = {
            id: studentId
        };

        // Ditch the id attribute if it is empty.
        if (!studentData.id) {
            delete studentData.id;
        }

        // Convert the dates into strings that the service will parse correctly.
        // TODO

        // Ajax call.
        $.ajax({
            type: studentData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("students" + (studentData.id ? "/" + studentData.id : "")),
            data: JSON.stringify(studentData),
            success: function () {
                $("#student-success").fadeIn();

                // If there is no studentId, then we are creating students,
                // in which case we clear the form in case more students
                // are to be created.
                if (!studentId) {
                    $("input, textarea").val("");
                } else {
                    location = "../" + studentId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#student-success").fadeOut();
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
