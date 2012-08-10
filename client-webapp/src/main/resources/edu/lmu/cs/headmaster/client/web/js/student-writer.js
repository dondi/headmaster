$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        UNSPECIFIED = "(unspecified)",
        BLANK = "",
        YES = "Yes",
        NO = "No";

    // If supplied, load up the student with that ID.
    if (studentId) {
        $.getJSON(
            Headmaster.serviceUri("students/" + studentId),
            function (data, textStatus, jqXHR) {
                // TODO
                $("#student-name").text(
                        data.firstName + " " +
                        (data.middleInitial ? data.middleInitial + "." : "") +
                        data.lastName
                );
                $("#student-gradyear").text(data.expectedGraduationYear);

                // Contact information.
                $("#student-email1").text(data.primaryEmail || BLANK);
                $("#student-email2").text(data.secondaryEmail || BLANK);
                $("#student-campus-box").text(data.campusBox || BLANK);
                $("#student-address").text(data.address || BLANK);
                $("#student-city").text(data.city || BLANK);
                $("#student-state").text(data.state || BLANK);
                $("#student-zip").text(data.zip || BLANK);
                $("#student-phone-main").text(data.mainPhone || BLANK);
                $("#student-phone-cell").text(data.cellPhone || BLANK);

                // Academic information.
                $("#student-college").text(data.college || BLANK);
                $("#student-advisor").text(data.advisor || BLANK);
                $("#student-degree").text(data.degree || BLANK);
                $("#student-gpa").text(data.cumulativeGpa || BLANK);
                // TODO majors
                // TODO minors
                $("#student-status").text(data.academicStatus || BLANK);

                // Status information.
                $("#student-inllc").text(data.inLivingLearningCommunity ? YES : NO);
                $("#student-transfer").text(data.transferStudent ? YES : NO);
                $("#student-studyabroad").text(data.hasStudiedAbroad ? YES : NO);

                // Entry information.
                $("#student-entryyear").text(data.entryYear || BLANK);
                $("#student-honorsentrydate").text(data.honorsEntryDate ?
                        Date.parse(data.honorsEntryDate).toString(DATE_FORMAT) : BLANK);
                $("#student-hsgpa").text(data.highSchoolGpa || BLANK);
                $("#student-act").text(data.actScore || BLANK);
                $("#student-sat-verbal").text(data.satVerbalScore || BLANK);
                $("#student-sat-math").text(data.satMathScore || BLANK);
                $("#student-sat-writing").text(data.satWritingScore || BLANK);

                // Notes.
                $("#student-notes").text(data.notes || BLANK);

                // Thesis information.
                $("#student-thesis-title").text(data.thesisTitle || UNSPECIFIED);
                $("#student-thesis-term").text(data.thesisTerm || BLANK);
                $("#student-thesis-year").text(data.thesisYear || data.expectedGraduationYear);
                $("#student-thesis-advisor").text(data.thesisAdvisor || BLANK);
                $("#student-thesis-inmajor").text(data.thesisInMajor ? YES : NO);
                if (!data.thesisInMajor) {
                    $("#student-thesis-course-container").fadeOut();
                }
                $("#student-thesis-course").text(data.thesisCourse || BLANK);
                $("#student-thesis-submitted").text(data.thesisSubmissionDate ? YES : NO);
                if (!data.thesisSubmissionDate) {
                    $("#student-thesis-submissiondate-container").fadeOut();
                }
                $("#student-thesis-submissiondate").text(data.thesisSubmissionDate ?
                        Date.parse(data.thesisSubmissionDate).toString(DATE_FORMAT) : BLANK);
                $("#student-thesis-notes").text(data.thesisNotes || BLANK);
            }
        );
    }

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
