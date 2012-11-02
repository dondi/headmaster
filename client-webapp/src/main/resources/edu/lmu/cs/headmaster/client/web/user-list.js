$(function () {
    // Retrieve the User ID that we were given.
    var userId = $("#student-id").text(),
        DATE_FORMAT = "M/d/yyyy",
        BLANK = "",
        YES = "Yes",
        NO = "No",
   
    // If supplied, load up the student with that ID.
    if (userId) {
        $.getJSON(
            Headmaster.serviceUri("users/" + userId),
            function (data, textStatus, jqXHR) {
                // Student name and graduation year.
                $("#user-firstname").val(data.firstName || BLANK);
                $("#user-email").val(data.email || BLANK);
                $("#user-username").val(data.username || BLANK);
                $("#user-password").val(data.password || BLANK);                
                $("#user-lastname").val(data.lastName || BLANK);

                    // We only load once.
                    $(this).unbind("show");
                });
            }
        );

        
    } else {
        
    $("#user-cancel").click(function (event) {
        history.go(-1);
        event.preventDefault();
    });

    $("#user-save").click(function (event) {
        // Grab the data from the web page.
        var userData = {
                id: userId,

                // Student name and graduation year.
                firstName: $("#user-firstname").val(),                
                lastName: $("#user-lastname").val(),
                email: $("#user-email").val(),
                username: $("#user-username").val(),
                password: $("#user-password").val(),
                

        

        // Ditch the id attribute if it is empty.
        if (!userData.id) {
            delete userData.id;
        }

       

        // Ajax call(s).  We do the student record put synchronously in case this is a new
        // student (in which case we want to be certain that the addition succeeded before
        // proceeding with the student record portion).
        $.ajax({
            type: userData.id ? "PUT" : "POST",
            url: Headmaster.serviceUri("users" + (userData.id ? "/" + userData.id : "")),
            data: JSON.stringify(userData),
            contentType: "application/json",
            dataType: "json",

            success: function (data, textStatus, jqXHR) {
                var resultId = userId || jqXHR.getResponseHeader("Location").split("/").pop();

                // Now for the student record, if applicable.
               
                // Provide visible UI feedback.
                $("#student-success").fadeIn();

                // If there is no studentId, then we are creating students,
                // in which case we clear the form in case more students
                // are to be created.
                if (!userId) {
                    $("input, textarea").val("");
                    $("#user-new-link")
                        .attr({ href: resultId })
                        .fadeIn();
                } else {
                    location = "../" + userId;
                }

                // Dismiss the alert after a fixed delay (not needed for edits).
                setTimeout(function () {
                    $("#user-new-link, #user-success").fadeOut();
                }, 5000);
            }
        });

        event.preventDefault();
    });

    // Set up other interactive components.
    $(".collapse").collapse();
});
