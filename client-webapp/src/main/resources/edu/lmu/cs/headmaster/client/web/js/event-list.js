$(function () {
    // Load up events for this month, last month, and next month. We use the
    // trick of setting the date to 0 in order to get the last day of the month.

    // TODO Implement date navigation so that the user can scroll through the months.
    // Ideally with a datepicker.
    var today = new Date(),
        thisMonth = today.getMonth(),
        thisYear = today.getFullYear(),
        thisFrom = new Date(thisYear, thisMonth, 1),
        thisTo = new Date(thisYear, thisMonth + 1, 0, 23, 59, 59),
        lastFrom = new Date(thisYear, thisMonth - 1, 1),
        lastTo = new Date(thisYear, thisMonth, 0, 23, 59, 59),
        nextFrom = new Date(thisYear, thisMonth + 1, 1),
        nextTo = new Date(thisYear, thisMonth + 2, 0, 23, 59, 59),
        QUERY_FORMAT = "yyyy-MM-ddThh:mm:ss",
        TABLE_FORMAT = "M/d/yyyy",

        getEvents = function (fromDate, toDate, tableId, progressId) {
            $.getJSON(
                Headmaster.serviceUri("events"),

                {
                    from: fromDate.toString(QUERY_FORMAT),
                    to: toDate.toString(QUERY_FORMAT),
                },

                function (data, textStatus, jqXHR) {
                    // Load up the data.
                    var tbody = $("#" + tableId + " > tbody");
                    $.each(data, function (index, event) {
                        tbody.append($(
                            "<tr><td>" +
                            Date.parse(event.dateTime).toString(TABLE_FORMAT) +
                            "</td><td>" +
                            event.title +
                            "</td></tr>"
                        ));
                    });

                    // Show/hide as needed.
                    $("#" + tableId).fadeIn();
                    $("#" + progressId).fadeOut();
                }
            );
        };

    // Ajax time.
    getEvents(thisFrom, thisTo, "list-this", "list-progress-this");
    getEvents(lastFrom, lastTo, "list-last", "list-progress-last");
    getEvents(nextFrom, nextTo, "list-next", "list-progress-next");
});
