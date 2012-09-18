$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "MMMM d, yyyy",
        UNSPECIFIED = "(unspecified)",
        BLANK = "",
        YES = "Yes",
        NO = "No",
        UNKNOWN = "?",

        /*
         * Helper function for building email address links.
         */
        getEmailElement = function (email) {
            return email ?
                    $('<a class="email"></a>').attr({ href: "mailto:" + email }).text(email) :
                    null;
        },

        /*
         * Helper function that provides the standardized text representation
         * for a major.
         */
        getMajorAsText = function (major) {
            return (major.degree ? major.degree + " " : BLANK) +
                    (major.discipline || BLANK) +
                    (major.collegeOrSchool ? ", " + major.collegeOrSchool : BLANK);
        };

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + studentId });

    // Set up other interactive components.
    $(".collapse").collapse();

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
                        location = "../events/" + event.id;
                    });
            }
        );

        // We only load once.
        $(this).unbind("show");
    });

    // TODO
    $("#student-grants-container").on("show", function () {
        console.log("load grants");
    });

    // Load up the student with that ID.
    $.getJSON(
        Headmaster.serviceUri("students/" + studentId),
        function (data, textStatus, jqXHR) {
            $("#student-name").text(
                data.firstName + " " +
                (data.middleName ? data.middleName + " " : "") +
                data.lastName
            );
            $("#student-gradyear").text(data.expectedGraduationYear);
            $("#student-active")
                .addClass(data.active ? "badge-success" : "badge-important")
                .text(data.active ? "Active" : "Inactive");

            // Contact information.
            $("#student-email1").append(getEmailElement(data.primaryEmail));
            $("#student-email2").append(getEmailElement(data.secondaryEmail));
            $("#student-campus-box").text(data.campusBox || BLANK);
            $("#student-address").text(data.address || BLANK);
            $("#student-city").text(data.city || BLANK);
            $("#student-state").text(data.state || BLANK);
            $("#student-zip").text(data.zip || BLANK);
            $("#student-phone-main").text(data.mainPhone || BLANK);
            $("#student-phone-cell").text(data.cellPhone || BLANK);

            // Academic information.
            $("#student-advisor").text(data.advisor || BLANK);

            // Majors and minors.
            Headmaster.loadArrayIntoTable(
                data.majors, "student-majors", "student-majors-empty",
                function (major) {
                    return $("<tr></tr>").append($("<td></td>")
                            .text(getMajorAsText(major))
                        );
                }
            );

            Headmaster.loadArrayIntoTable(
                data.minors, "student-minors", "student-minors-empty",
                function (string) {
                    return $("<tr></tr>").append($("<td></td>")
                            .text(string));
                }
            );

            // Status information.
            $("#student-compact").text(data.compactSigned ? YES : NO);
            $("#student-inllc").text(data.inLivingLearningCommunity ? YES : NO);
            $("#student-transfer").text(data.transferStudent ? YES : NO);
            $("#student-residencycode").text(data.residencyCode);
            $("#student-studyabroad").text(data.hasStudiedAbroad ? YES : NO);

            // Demographics information.
            $("#student-sex").text(data.sex);
            $("#student-raceorethnicity").text(data.raceOrEthnicity);

            // Entry information.
            $("#student-entryyear").text(data.entryYear || BLANK);
            $("#student-honorsentrydate").text(data.honorsEntryDate ?
                    Date.parse(data.honorsEntryDate).toString(DATE_FORMAT) : BLANK);
            $("#student-scholarship").text(data.scholarship || BLANK);

            // Notes.
            $("#student-notes").text(data.notes || UNSPECIFIED);

            // Thesis information.
            $("#student-thesis-title").text(data.thesisTitle || UNSPECIFIED);
            $("#student-thesis-term").text(data.thesisTerm || BLANK);
            $("#student-thesis-year").text(data.thesisYear || data.expectedGraduationYear);
            $("#student-thesis-advisor").text(data.thesisAdvisor || BLANK);
            $("#student-thesis-inmajor").text(data.thesisInMajor ? YES :
                    (data.thesisInMajor === false ? NO : UNKNOWN));
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
            $("#student-thesis-notes").text(data.thesisNotes || UNSPECIFIED);
        }
    );

    // Load up the student's privileged information. Here, we attempt to load
    // the student record object. If we get FORBIDDEN, we conclude that the
    // current user is not allowed to see the student record and thus hide the
    // view elements for them. This can be done asynchronously because they
    // affect a separate set of elements.
    $.getJSON(
        Headmaster.serviceUri("students/" + studentId + "/record"),
        function (data, textStatus, jqXHR) {
            $("#student-schoolid").text(data.schoolId || BLANK);
            $("#student-hsgpa").text(data.highSchoolGpa || BLANK);
            $("#student-act").text(data.actScore || BLANK);
            $("#student-sat-verbal").text(data.satVerbalScore || BLANK);
            $("#student-sat-math").text(data.satMathScore || BLANK);
            $("#student-sat-writing").text(data.satWritingScore || BLANK);
            $("#student-gpa").text(data.cumulativeGpa ? data.cumulativeGpa.toFixed(2) : BLANK);
            $("#student-status").text(data.academicStatus || BLANK);

            // Grade information.
            Headmaster.loadArrayIntoTable(
                data.grades, "student-grades", "student-grades-empty",
                function (gpa) {
                    return $("<tr></tr>")
                        .append($("<td></td>").text(gpa.term + " " + gpa.year))
                        .append($("<td></td>").text(gpa.gpa.toFixed(2)));
                }
            );
        }
    ).error(function (jqXHR, textStatus, errorThrown) {
        // On error, hide the expected fields.
        // TODO The only "routine" error should be receipt of FORBIDDEN. If
        // anything else is encountered, it would be good to inform the user
        // that something else is amiss.
        $("#student-schoolid").parent().parent().hide();
        $("#student-hsgpa").parent().parent().hide();
        $("#student-act").parent().parent().hide();
        $("#student-sat-verbal").parent().parent().hide();
        $("#student-sat-math").parent().parent().hide();
        $("#student-sat-writing").parent().parent().hide();
        $("#student-gpa").parent().parent().hide();
        $("#student-status").parent().parent().hide();
        $("#student-grades-container").parent().hide();
    });

});
