
//This is a provisional file to hold the js for adding ney grades
//This Code should later be placed to student-writer.js when it becomes copletely functional




// Button click handling for Grades
    $("#student-grade-add-button").click(function (event) {  
        // Create a new grade from the fields then add it to the grade table.
        var gpa = {  
            term: $("#student-grade-semester").val(),  
            year: $("#student-grade-year").val(),  
            gpa: $("student-grade-gpa").val() 
        };

        // Add a row for that grade to the table.
        //Still need to create a create
        $("#student-grades > tbody").append(createGradeTableRow(gpa));  
        updateDependentElements();

        // Clear the add section.
        $("#student-grade-container > div > input").val("");  
    });



        
        /*
         * Helper function for creating a table row displaying a grade.
         */
        createGradeTableRow = function (gpa) {
            var tr = $("<tr></tr>");

            // Support in-place edits.
            makeGradeTableRowEditable(tr);

            return tr.append($("<td></td>")
                    .append($("<td></td>").text(gpa.term + " " + gpa.year)) // Still to be checked
                    .append($("<td></td>").text(gpa.gpa.toFixed(2)));// Still to be checked
                    .append(createRemoveElement(tr))
                )

                // Save the actual object as data on that row.
                .data("gpa", gpa);
        },




                // Grades.
                Headmaster.loadArrayIntoTable(
                    data.grades, "student-grades", "student-grades-empty", createGradeTableRow
                );



//TO DO

//NEED TO CREATE FUNCTION FOR makeGradeTableRowEditable(tr);

//Check Still to be checked stuff on this file












//This Code should later be placed to student-writer.js when it becomes copletely functional

       
            // ALREADY ON FILE, JUST HERE FOR REFERENCE
            // Grade information.
            Headmaster.loadArrayIntoTable(
                data.grades, "student-grades", "student-grades-empty",
                function (gpa) {
                    return $("<tr></tr>")
                        .append($("<td></td>").text(gpa.term + " " + gpa.year))
                        .append($("<td></td>").text(gpa.gpa.toFixed(2)));
                }
            );

            // ALREADY ON FILE, JUST HERE FOR REFERENCE
            // Majors and minors.
            Headmaster.loadArrayIntoTable(
                data.majors, "student-majors", "student-majors-empty",
                function (major) {
                    return $("<tr></tr>").append($("<td></td>")
                            .text(getMajorAsText(major))
                        );
                }
            );
                                        