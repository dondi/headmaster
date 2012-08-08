$(function () {
    // Retrieve the ID that we were given, and load up the event with that ID.
    var eventId = $("#event-id").text(),
        DATE_FORMAT = "MMMM d, yyyy";

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
