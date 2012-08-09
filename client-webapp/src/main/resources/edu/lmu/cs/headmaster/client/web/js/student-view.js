$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "MMMM d, yyyy",
        UNSPECIFIED = "(unspecified)",
        BLANK = "",
        YES = "Yes",
        NO = "No";

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + studentId });

    // Set up other interactive components.
    $(".collapse").collapse();
    // TODO set up listeners so that as an accordion opens, the right Ajax call is made
    //      (except for thesis, which is known right away)
    $("#student-attendance-container").on("show", function () {
        console.log("load attendance");
    });
    $("#student-grades-container").on("show", function () {
        console.log("load grades");
    });
    $("#student-grants-container").on("show", function () {
        console.log("load grants");
    });
    $("#student-thesis-container").on("show", function () {
        console.log("load thesis");
    });

    // Load up the event with that ID.
    $.getJSON(
        Headmaster.serviceUri("students/" + studentId),
        function (data, textStatus, jqXHR) {
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
});
