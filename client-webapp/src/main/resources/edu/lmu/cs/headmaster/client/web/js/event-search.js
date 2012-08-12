$(function () {
    var TABLE_FORMAT = "M/d/yyyy";

    Headmaster.setupSearchField(
        $("#search-field"), $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("events"), "q",
        function (event) {
            return $(
                    "<tr><td>" +
                    Date.parse(event.dateTime).toString(TABLE_FORMAT) +
                    "</td><td>" +
                    event.title +
                    "</td><td>" +
                    event.description +
                    "</td></tr>"
                ).click(function () {
                    location = event.id;
                });
        }
    );
});
