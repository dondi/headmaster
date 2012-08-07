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
            dateTime: Date.parse($("#event-date").val()),
            title: $("#event-title").val(),
            description: $("#event-description").val()
        };

        // Convert the date into a string that the service will parse correctly.
        if (eventData.dateTime) {
            eventData.dateTime = eventData.dateTime.toString("yyyy-MM-dd");
        }

        // Ajax call.
        $.ajax({
            type: "POST",
            url: Headmaster.serviceUri("events"),
            data: JSON.stringify(eventData),
            success: function () {
                // TODO
            },
            contentType: "application/json",
            dataType: "json"
        });

        event.preventDefault();
    });

});
