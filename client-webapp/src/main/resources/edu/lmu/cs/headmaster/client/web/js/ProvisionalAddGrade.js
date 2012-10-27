//This is a provisional file to hold the js for adding ney grades
//This Code should later be placed to student-writer.js when it becomes copletely functional

// Button click handling for Grades
    $("#student-grade-add-button").click(function (event) {  
        // Create a new grade from the fields then add it to the grade table.
        var gpa = {  
            term: $("#student-grade-semester").val(),  
            year: $("#student-grade-year").val(),  
            numeric-value: $("student-grade-value").val() 
        };

        // Add a row for that grade to the table.
        //Still need to create a create
        $("#student-grades > tbody").append(createGradeTableRow(grade));  
        updateDependentElements();

        // Clear the add section.
        $("#stundent-grade-container > div > input").val("");  
    });
                                        