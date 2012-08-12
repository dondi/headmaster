$(function () {
    // Retrieve the ID that we were given.
    var eventId = $("#event-id").text(),
        DATE_FORMAT = "MMMM d, yyyy",
        BLANK = "";

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + eventId });

    // Load up the event with that ID.
    $.getJSON(
        Headmaster.serviceUri("events/" + eventId),
        function (data, textStatus, jqXHR) {
            $("#event-date").text(data.dateTime ?
                    Date.parse(data.dateTime).toString(DATE_FORMAT) : BLANK);
            $("#event-title").text(data.title);
            $("#event-description").text(data.description);

            // List the attendees.
            Headmaster.loadArrayIntoTable(
                data.attendees, "event-attendees", "event-attendees-empty",
                function (student) {
                    return $(
                        "<tr><td>" +
                        student.firstName + " " +
                        student.lastName +
                        "</td></tr>"
                    ).click(function () {
                        // View that student if the row is clicked.
                        location = "../students/" + student.id;
                    });
                }
            );
        }
    );
});
