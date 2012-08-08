$(function () {
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
            id: $("#event-id").text(),
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

                // If there is no event-id, then we are creating events,
                // in which case we clear the form in case more events
                // are to be created.
                if (!$("#event-id").text()) {
                    $("form input, form textarea").val("");
                }

                // Dismiss the alert after a fixed delay.
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
