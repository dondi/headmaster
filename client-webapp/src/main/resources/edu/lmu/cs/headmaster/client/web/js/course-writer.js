$(function () {
    // Retrieve the ID that we were given.
    var courseId = $("#course-id").text(),

        // Indicates whether the current user may edit the course.
        canEditCourse= false,

        /*
         * Helper function for updating elements that depend on the value of
         * another element.
         */
        updateDependentElements = function () {
        },

        /*
         * Helper function for creating a row removal element for the given
         * table row (tr).
         */
        createRemoveElement = function (tr) {
            return $("<i></i>")
                .addClass("icon-remove-sign pull-right")
                .click(function () {
                    tr.remove();
                    updateDependentElements();
                });
        },

        /*
         * Helper function for creating the standard in-place editing controls.
         */
        addEditableRowIcons = function (container, confirmFunction, restoreFunction) {
            container.append(
                $("<button></button>").addClass("btn btn-danger")
                    .append($("<i></i>").addClass("icon-ban-circle icon-white"))
                    .click(function (event) {
                        // No harm no foul---just restore the row.
                        restoreFunction(event);
                    })
            ).append(
                $("<button></button>").addClass("btn btn-success")
                    .append($("<i></i>").addClass("icon-ok icon-white"))
                    .click(function (event) {
                        // Finalize the edit.
                        confirmFunction();
                        restoreFunction(event);
                    })
            );
        },

        /*
         * Sets up the given (presumed input) element as a typeahead for the
         * given uri.
         */
        setUpTypeahead = function (input, uri) {
            input.typeahead({
                source: function (query, process) {
                    $.getJSON(
                        Headmaster.serviceUri(uri),
                        { q: query },
                        process
                    );
                }
            });
        };

    // If supplied, load up the course with that ID.
    if (courseId) {
        $.getJSON(
            Headmaster.serviceUri("courses/" + courseId),
            function (data, textStatus, jqXHR) {
                // Student name and graduation year.
                $("#course-subject").val(data.subject || BLANK);
                $("#course-number").val(data.number || BLANK);
                $("#course-section").val(data.section || BLANK);
                $("#course-term").val(course.term || BLANK);
                $("#course-year").val(data.year);
                $("#course-title").val(data.title || BLANK);

                // TODO Load objectives (which in turn have outcomes)
                // TODO Load assignments
                // TODO Load students

                // Now that values have been set, put dependent user interface
                // elements in the correct initial state.
                updateDependentElements();
            }
        );
    } else {
        // When there is no id, we update the dependent elements and clear out the lists.
        // Update dependent elements in the case that there is no id.
        updateDependentElements();
        $(".progress").fadeOut();
    }

    $("#course-cancel").click(function (event) {
        history.go(-1);
        event.preventDefault();
    });

    $("#course-save").click(function (event) {
        // Grab the data from the web page.
        var courseData = {
                id: courseId,
                subject: $("#course-subject").val(),
                number: $("#course-number").val(),
                section: $("#course-section").val(),
                term: $("#course-term").val(),
                year: $("#course-year").val(),
                title: $("#course-title").val(),

                // TODO objectives: [],
                // TODO assignments: [],
                // TODO students: []
            };

        // Ditch the id attribute if it is empty.
        if (!courseData.id) {
            delete courseData.id;
        }

        // Ajax call(s).
        $.ajax({
            type: courseData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("courses" + (courseData.id ? "/" + courseData.id : "")),
            data: JSON.stringify(courseData),
            contentType: "application/json",
            dataType: "json",

            success: function (data, textStatus, jqXHR) {
                var resultId = courseId || jqXHR.getResponseHeader("Location").split("/").pop();

                // Provide visible UI feedback.
                $("#course-success").fadeIn();

                // If there is no courseId, then we are creating courses,
                // in which case we clear the form in case more courses
                // are to be created.
                if (!courseId) {
                    $("input, textarea").val("");
                    $("select").val("FALL");
                    updateDependentElements();
                    // Added content ends here.
                    
                    $("#course-new-link")
                        .attr({ href: resultId })
                        .fadeIn();
                } else {
                    location = "../" + courseId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#course-new-link, #course-success").fadeOut();
                }, 5000);
            }
        });

        event.preventDefault();
    });

    // Set up other interactive components.
    $(".collapse").collapse();
});
