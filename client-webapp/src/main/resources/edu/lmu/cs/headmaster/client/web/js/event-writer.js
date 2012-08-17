$(function () {
    // Retrieve the ID that we were given, and load up the event with that ID
    // (if there).
    var eventId = $("#event-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",

        /*
         * Checks the state of the attendees and student search result tables.
         * An empty table is hidden and replaced with its accompanying empty
         * indicator. If it is not empty, it is displayed and the empty
         * indicator is hidden.
         */
        showOrHideStudentTables = function () {
            Headmaster.toggleElements(
                $("#event-attendees > tbody > tr").length,
                $("#event-attendees"),
                $("#event-attendees-empty")
            );

            Headmaster.toggleElements(
                $("#search-results > tbody > tr").length,
                $("#search-results"),
                $("#search-empty")
            );
        },

        /*
         * Creates a tr element representing an attending student.
         */
        createAttendeeTableRow = function (student) {
            var td = $("<td></td>").text(student.firstName + " " + student.lastName),
                tr = $("<tr></tr>");
    
            // Include a remove button.
            $('<i class="icon-remove-sign pull-right"></i>')
                .appendTo(td)
                .click(function () {
                    tr.remove();
                    showOrHideStudentTables();
                });
        
            // Save the actual object as data on that row.
            return tr.data("student", student).append(td).click(function () {
                window.open("../../students/" + student.id, "_blank");
            });
        },

        /*
         * Checks whether the given student already has a row in the attendees
         * table.
         */
        isAttendee = function (student) {
            // We don't use $.each because we might want to break out of the
            // loop.
            var rows = $("#event-attendees > tbody > tr"), i, max;

            // Comparison is by student id only.
            for (i = 0, max = rows.length; i < max; i += 1) {
                if ($(rows[i]).data("student").id === student.id) {
                    return true;
                }
            }

            // If we get here, we did not find the student.
            return false;
        };

    if (eventId) {
        $.getJSON(
            Headmaster.serviceUri("events/" + eventId),
            function (data, textStatus, jqXHR) {
                $("#event-date").val(data.dateTime ?
                        Date.parse(data.dateTime).toString(DATE_FORMAT) : BLANK);
                $("#event-title").val(data.title || BLANK);
                $("#event-description").val(data.description || BLANK);

                // List the attendees.
                Headmaster.loadArrayIntoTable(
                    data.attendees,
                    "event-attendees",
                    "event-attendees-empty",
                    createAttendeeTableRow
                );
            }
        );
    } else {
        // Otherwise, we start empty.
        $("#event-date, #event-title, #event-description").val(BLANK);
        $("#event-attendees").fadeOut();
        $("#event-attendees-empty").fadeIn();
    }

    // Datepicker setup.
    $("#event-date").datepicker();

    // Search field setup.
    Headmaster.setupSearchField(
        $("#search-field"), $("#search-progress"), $("#search-empty"), $("#search-results"),
        Headmaster.serviceUri("students"), "q",
        function (student) {
            return isAttendee(student) ? null :
                $("<tr></tr>")
                    .append($("<td></td>").text(student.firstName + " " + student.lastName))
                    .click(function () {
                        // Add the clicked student to the attendee table...
                        $("#event-attendees > tbody")
                            .append(createAttendeeTableRow(student));
    
                        // ...then remove it from this one.
                        $(this).remove();
                        showOrHideStudentTables();
                    });
        }
    );

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
            description: $("#event-description").val(),

            // To be filled out below.
            attendees: []
        };

        // Gather attendee data.
        Headmaster.loadTableIntoArray(
            eventData, "attendees", $("#event-attendees > tbody > tr"),
            function (tr) {
                return $(tr).data("student");
            }
        );

        // Ditch the id attribute if it is empty.
        if (!eventData.id) {
            delete eventData.id;
        }

        // Convert the date into a string that the service will parse correctly.
        Headmaster.dateToDateString(eventData, "dateTime");

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
