$(function () {
    var searchField = $("#search-field");

    // Set up search field event handling.
    Headmaster.setupSearchField(
        searchField, $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("students"), "q",
        function (student) {
            return $("<tr></tr>")
                .append($("<td></td>").text(student.firstName + " " + student.lastName))
                .append($("<td></td>").append(
                        student.primaryEmail ? Headmaster.getEmailElement(student.primaryEmail) : null
                    ).append(
                        student.primaryEmail && student.secondaryEmail ? $("<br/>") : null
                    ).append(
                        student.secondaryEmail ? Headmaster.getEmailElement(student.secondaryEmail) : null
                    )
                )
                .append($("<td></td>").append(Headmaster.loadArrayIntoUnorderedList(student.majors, "collegeOrSchool")))
                .append($("<td></td>").append(Headmaster.loadArrayIntoUnorderedList(student.majors, "discipline")))
                .click(function () {
                    location = student.id;
                });
        }
    );

    // If there is already a value in the search field, trigger a search.
    if (searchField.val()) {
        searchField.trigger("input");
    }
});
