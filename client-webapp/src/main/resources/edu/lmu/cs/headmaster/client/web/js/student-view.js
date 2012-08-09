$(function () {
    // Retrieve the ID that we were given.
    var studentId = $("#student-id").text(),
        DATE_FORMAT = "MMMM d, yyyy";

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + studentId });

    // Set up other interactive components.
    $(".collapse").collapse();
    // TODO set up listeners so that as an accordion opens, the right Ajax call is made
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
        }
    );
});
