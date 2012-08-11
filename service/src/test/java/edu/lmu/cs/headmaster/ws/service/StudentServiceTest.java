package edu.lmu.cs.headmaster.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.Term;

/**
 * Tests the student web service.
 */
public class StudentServiceTest extends ServiceTest {

    @Test
    public void testGetStudentsNoQuery() {
        ClientResponse response = ws.path("students").get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals(
            "400 " + AbstractService.QUERY_REQUIRED,
            response.getEntity(String.class)
        );
    }

    @Test
    public void testGetStudentById() {
        // Grab a test fixture student.
        Student student = ws.path("students/1000000").get(Student.class);
        Assert.assertEquals(Long.valueOf(1000000L), student.getId());
        Assert.assertEquals("Berners-Lee", student.getLastName());
        Assert.assertEquals("Tim", student.getFirstName());
        Assert.assertEquals(Integer.valueOf(2016), student.getExpectedGraduationYear());

        // The text fixture data has some empty values.
        Assert.assertNull(student.getMiddleInitial());
        Assert.assertNull(student.getEntryYear());

        // Collection data do not come along for the ride.
        Assert.assertEquals(0, student.getGrades().size());
        Assert.assertEquals(0, student.getGrants().size());
        Assert.assertEquals(0, student.getAttendance().size());
    }

    @Test
    public void testGetStudentGradesById() {
        // This test fixture student is supposed to have grades.
        List<GPA> grades = ws.path("students/1000002/grades")
                .get(new GenericType<List<GPA>>(){});
        Assert.assertEquals(2, grades.size());

        // We expect grades to be sorted by year then term.
        Assert.assertEquals(Term.FALL, grades.get(0).getTerm());
        Assert.assertEquals(2015, grades.get(0).getYear());
        Assert.assertEquals(3.8, grades.get(0).getGpa(), 0.0);

        Assert.assertEquals(Term.SPRING, grades.get(1).getTerm());
        Assert.assertEquals(2016, grades.get(1).getYear());
        Assert.assertEquals(3.5, grades.get(1).getGpa(), 0.0);
    }

    @Test
    public void testGetGradesByIdForNonExistentStudent() {
        // When a student does not exist, we should get a 404.
        ClientResponse clientResponse = ws.path("students/2000000/grades")
                .get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
        Assert.assertEquals(
            "404 " + StudentService.STUDENT_NOT_FOUND,
            clientResponse.getEntity(String.class)
        );
    }

    @Test
    public void testGetGradesByIdForStudentWithoutGrades() {
        // When a student does exist but has no grades, we get an empty list
        // back.
        List<GPA> grades = ws.path("students/1000000/grades")
                .get(new GenericType<List<GPA>>(){});
        Assert.assertEquals(0, grades.size());
    }

    @Test
    public void testSetStudentGradesById() {
        // This PUT test verifies the setting of GPA records.
        List<GPA> grades = new ArrayList<GPA>();

        // We leave the correctness of the GET to other unit tests.
        GPA gpa = new GPA();
        gpa.setTerm(Term.FALL);
        gpa.setYear(2013);
        gpa.setGpa(3.5);
        grades.add(gpa);

        gpa = new GPA();
        gpa.setTerm(Term.SPRING);
        gpa.setYear(2014);
        gpa.setGpa(3.75);
        grades.add(gpa);

        // Now, save the grades.  We should get a 204.
        ClientResponse response = ws.path("students/1000000/grades").put(ClientResponse.class, grades);
        Assert.assertEquals(204, response.getStatus());
    }

}
