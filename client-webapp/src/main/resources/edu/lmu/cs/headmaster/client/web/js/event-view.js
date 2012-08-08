$(function () {
    // Retrieve the ID that we were given.
    var eventId = $("#event-id").text(),
        DATE_FORMAT = "MMMM d, yyyy";

    // Set up the edit button.
    $("#edit-button").attr({ href: "edit/" + eventId });

    // Load up the event with that ID.
    $.getJSON(
        Headmaster.serviceUri("events/" + eventId),
        function (data, textStatus, jqXHR) {
            $("#event-date").text(Date.parse(data.dateTime).toString(DATE_FORMAT));
            $("#event-title").text(data.title);
            $("#event-description").text(data.description);

            // List the attendees.
            if (!data.attendees || !data.attendees.length) {
                $("#event-attendees-empty").fadeIn();
            }

            $("#event-attendees-progress").fadeOut();
        }
    );
});
