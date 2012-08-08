$(function () {
    var TABLE_FORMAT = "M/d/yyyy",
        jSearchResults = $("#search-results"),
        jSearchEmpty = $("#search-empty"),
        jSearchProgress = $("#search-progress"),
        jSearchField = $("#search-field"),
        tbody = jSearchResults.find("tbody"),

        // To avoid flooding the server with partial searches, we place the
        // search call in a timeout.
        pendingSearch;

    // It all gets triggered by input events.
    jSearchField.bind("input", function (event) {
        // If there is a pending search, kill it.
        if (pendingSearch) {
            clearTimeout(pendingSearch);
            pendingSearch = null;
        }

        // An empty search field just clears things.
        if (!jSearchField.val()) {
            jSearchProgress.fadeOut();
            jSearchEmpty.fadeOut();
            jSearchResults.fadeOut();
            return;
        }

        // Wait a little before triggering JSON.
        pendingSearch = setTimeout(function () {
            // First, display the progress bar.
            jSearchProgress.fadeIn();
            
            // Make the call.
            $.getJSON(
                Headmaster.serviceUri("events"),
                { q: jSearchField.val() },
                function (data, textStatus, jqXHR) {
                    tbody.empty();
                    if (data.length) {
                        $.each(data, function (index, event) {
                            tbody.append(
                                $(
                                    "<tr><td>" +
                                    Date.parse(event.dateTime).toString(TABLE_FORMAT) +
                                    "</td><td>" +
                                    event.title +
                                    "</td><td>" +
                                    event.description +
                                    "</td></tr>"
                                ).click(function () {
                                    location = event.id;
                                })
                            );
                        });
                        jSearchEmpty.fadeOut(function () {
                            jSearchResults.fadeIn();
                        });
                    } else {
                        jSearchResults.fadeOut(function () {
                            jSearchEmpty.fadeIn();
                        });
                    }
                    jSearchProgress.fadeOut();
                }
            );
        }, 250);
    });
});
