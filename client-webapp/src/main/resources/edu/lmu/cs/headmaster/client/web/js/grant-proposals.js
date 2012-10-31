var TABLE_FORMAT = "M/d/yyyy",
    getGrants = function (tableId) {
        Headmaster.loadJsonArrayIntoTable(
            Headmaster.serviceUri("grants"),
            tableId + "-progress",
            tableId,
            tableId + "-empty",

            function (grant) {
                return $("<tr></tr>")
                    .append($("<td></td>").text(grant.title))
                    .append($("<td></td>").text(grant.description))
                    .append($("<td></td>").text(
                         Date.parse(grant.submissionDate).toString(TABLE_FORMAT)))
                    .append($("<td></td>").text(grant.type))
                    .append($("<td></td>").text(grant.facultyMentor))
                    //this is going to be a problem
                    .append($("<td></td>").text(grant.students))
                    .append($("<td></td>").text(grant.notes))
                    .click(function () {
                        location = grant.id;
                    });
            },

            {
                awarded: false,
                presented: false,
            }
        );
    };
    getGrants("list-this");
