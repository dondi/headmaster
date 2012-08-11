$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "MMMM d, yyyy",
        UNSPECIFIED = "(unspecified)",
        BLANK = "",
        YES = "Yes",
        NO = "No",

        /*
         * Helper function for loading an array into a web page table. The
         * createRow parameter is a function (object) which is expected to
         * return a jQuery element for the table row that corresponds to that
         * object.
         */
        loadArrayIntoTable = function (arrayToLoad, tableId, emptyId, createRow) {
            var table = $("#" + tableId),
                empty = $("#" + emptyId),
                tbody = table.find("tbody");

            if (arrayToLoad && arrayToLoad.length) {
                $.each(arrayToLoad, function (index, item) {
                    tbody.append(createRow(item));
                });
                table.fadeIn();
                empty.fadeOut();
            } else {
                table.fadeOut();
                empty.fadeIn();
            }
        };

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + studentId });

    // Set up other interactive components.
    $(".collapse").collapse();

    // TODO set up listeners so that as an accordion opens, the right Ajax call is made
    //      (except for grades and thesis, which are known right away)
    $("#student-attendance-container").on("show", function () {
        console.log("load attendance");
    });

    $("#student-grants-container").on("show", function () {
        console.log("load grants");
    });

    // Load up the student with that ID.
    $.getJSON(
        Headmaster.serviceUri("students/" + studentId),
        function (data, textStatus, jqXHR) {
            var createRowFromString = function (string) {
                return $("<tr><td>" + string + "</td></tr>");
            };

            $("#student-name").text(
                data.firstName + " " +
                (data.middleInitial ? data.middleInitial + ". " : "") +
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
            $("#student-gpa").text(data.cumulativeGpa ? data.cumulativeGpa.toFixed(2) : BLANK);
            $("#student-status").text(data.academicStatus || BLANK);

            // Majors and minors.
            loadArrayIntoTable(data.majors, "student-majors", "student-majors-empty", createRowFromString);
            loadArrayIntoTable(data.minors, "student-minors", "student-minors-empty", createRowFromString);

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

            // Grade information.
            loadArrayIntoTable(
                data.grades, "student-grades", "student-grades-empty",
                function (gpa) {
                    return $(
                        "<tr><td>" +
                        gpa.term + " " + gpa.year +
                        "</td><td>" +
                        gpa.gpa.toFixed(2) +
                        "</td></tr>"
                    );
                }
            );

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
