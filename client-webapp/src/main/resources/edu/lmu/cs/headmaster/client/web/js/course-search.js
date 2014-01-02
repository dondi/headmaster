$(function () {
    var searchField = $("#search-field");

    // Set up search field event handling.
    Headmaster.setupSearchField(
        searchField, $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("courses"), "q",
        function (course) {
            return $("<tr></tr>")
                .append($("<td></td>").text(course.subject))
                .append($("<td></td>").text(course.number))
                .append($("<td></td>").text(course.section))
                .append($("<td></td>").text(course.title))
                .append($("<td></td>").text(course.term))
                .append($("<td></td>").text(course.year))
                .click(function () {
                    location = course.id;
                });
        }
    );

    // If there is already a value in the search field, trigger a search.
    if (searchField.val()) {
        searchField.trigger("input");
    }
});
