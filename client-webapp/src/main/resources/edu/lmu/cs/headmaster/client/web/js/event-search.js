$(function () {
    var TABLE_FORMAT = "M/d/yyyy";

    Headmaster.setupSearchField(
        $("#search-field"), $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("events"), "q",
        function (event) {
            return $("<tr></tr>")
                .append($("<td></td>").text(Date.parse(event.dateTime).toString(TABLE_FORMAT)))
                .append($("<td></td>").text(event.title))
                .append($("<td></td>").text(event.description))
                .click(function () {
                    location = event.id;
                });
        }
    );
});
