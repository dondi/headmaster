$(function () {
    // Retrieve the ID that we were given, and load up the event with that ID
    // (if there).
    var eventId = $("#event-id").text(),
        DATE_FORMAT = "M/d/yyyy";

    if (eventId) {
        $.getJSON(
            Headmaster.serviceUri("events/" + eventId),
            function (data, textStatus, jqXHR) {
                $("#event-date").val(Date.parse(data.dateTime).toString(DATE_FORMAT));
                $("#event-title").val(data.title);
                $("#event-description").val(data.description);
                
                // List the attendees.
                if (!data.attendees || !data.attendees.length) {
                    $("#event-attendees-empty").fadeIn();
                }
                
                $("#event-attendees-progress").fadeOut();
            }
        );
    }

    // Datepicker setup.
    $("#event-date").datepicker();

    // Button click handling.
    $("#event-cancel").click(function (event) {
        history.go(-1);
        event.preventDefault();
    });

    $("#event-save").click(function (event) {
        // Grab the data from the web page.
        var eventData = {
            id: eventId,
            dateTime: Date.parse($("#event-date").val()),
            title: $("#event-title").val(),
            description: $("#event-description").val()
        };

        // Ditch the id attribute if it is empty.
        if (!eventData.id) {
            delete eventData.id;
        }

        // Convert the date into a string that the service will parse correctly.
        if (eventData.dateTime) {
            eventData.dateTime = eventData.dateTime.toString("yyyy-MM-dd");
        }

        // Ajax call.
        $.ajax({
            type: eventData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("events" + (eventData.id ? "/" + eventData.id : "")),
            data: JSON.stringify(eventData),
            success: function () {
                $("#event-success").fadeIn();

                // If there is no eventId, then we are creating events,
                // in which case we clear the form in case more events
                // are to be created.
                if (!eventId) {
                    $("form input, form textarea").val("");
                } else {
                    location = "../" + eventId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#event-success").fadeOut();
                }, 5000);
            },
            contentType: "application/json",
            dataType: "json"
        });

        event.preventDefault();
    });

});
