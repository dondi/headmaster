package edu.lmu.cs.headmaster.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.domain.Event;
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
        Assert.assertEquals(0, student.getGrades().size());

        // Grant and event data do not come along for the ride.
        Assert.assertEquals(0, student.getGrants().size());
        Assert.assertEquals(0, student.getAttendance().size());
    }

    @Test
    public void testGetStudentAttendanceById() {
        // Verify that the text fixture event attendance comes out correctly.
        for (long l = 1000000L; l < 1000003L; l++) {
            List<Event> events = ws.path("students/" + l + "/attendance")
                    .get(new GenericType<List<Event>>(){});
            Assert.assertEquals(1, events.size());
            Assert.assertEquals(Long.valueOf(1000000L), events.get(0).getId());
            Assert.assertEquals("Summit", events.get(0).getTitle());
            Assert.assertEquals("The big one", events.get(0).getDescription());
        }
    }

    @Test
    public void testGetStudentAttendanceByIdForStudentWithNoEvents() {
        // We get a non-null but empty list for a student without events.
        List<Event> events = ws.path("students/1000003/attendance")
                .get(new GenericType<List<Event>>(){});
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testGetStudentAttendanceByIdForNonexistentStudent() {
        // We expect a 404 when the student does not exist.
        ClientResponse clientResponse = ws.path("students/2000000/attendance")
                .get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
        Assert.assertEquals("404 " + StudentService.STUDENT_NOT_FOUND,
                clientResponse.getEntity(String.class));
    }

    @Test
    public void testGetStudentByIdGrades() {
        // This test fixture student is supposed to have grades.
        List<GPA> grades = ws.path("students/1000002").get(Student.class).getGrades();
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
    public void testCreateOrUpdateStudentGrades() {
        // This PUT test verifies the full setting of GPA records.
        List<GPA> grades = new ArrayList<GPA>();

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

        // We leave the correctness of the GET to other unit tests.
        Student student = ws.path("students/1000000").get(Student.class);
        student.setGrades(grades);

        // Now, save the student.  We should get a 204.
        ClientResponse response = ws.path("students/1000000")
                .put(ClientResponse.class, student);
        Assert.assertEquals(204, response.getStatus());

        // We check that the grades were indeed saved.
        grades = ws.path("students/1000000").get(Student.class).getGrades();
        Assert.assertEquals(2, grades.size());
        Assert.assertEquals(Term.FALL, grades.get(0).getTerm());
        Assert.assertEquals(2013, grades.get(0).getYear());
        Assert.assertEquals(3.5, grades.get(0).getGpa(), 0.0);

        Assert.assertEquals(Term.SPRING, grades.get(1).getTerm());
        Assert.assertEquals(2014, grades.get(1).getYear());
        Assert.assertEquals(3.75, grades.get(1).getGpa(), 0.0);
    }

}
