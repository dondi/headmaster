package edu.lmu.cs.headmaster.ws.resource;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.StudentRecord;
import edu.lmu.cs.headmaster.ws.types.Term;

/**
 * Tests the student web resource.
 */
public class StudentResourceTest extends ResourceTest {

    @Test
    public void testGetStudentsNoQuery() {
        ClientResponse clientResponse = wr.path("students").get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.QUERY_REQUIRED,
            clientResponse.getEntity(String.class)
        );
    }

    @Test
    public void testGetStudentsClassYearMutualExclusion() {
        // The GET /students parameter "class" is mutually exclusive with
        // "classFrom" and "classTo."
        ClientResponse clientResponse = wr.path("students")
                .queryParam("class", "FRESHMAN")
                .queryParam("classFrom", "2012")
                .get(ClientResponse.class);

        // We expect error 400, ARGUMENT_CONFLICT.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.ARGUMENT_CONFLICT,
            clientResponse.getEntity(String.class)
        );

        clientResponse = wr.path("students")
                .queryParam("class", "FRESHMAN")
                .queryParam("classTo", "2012")
                .get(ClientResponse.class);

        // Still error 400, ARGUMENT_CONFLICT.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.ARGUMENT_CONFLICT,
            clientResponse.getEntity(String.class)
        );

        clientResponse = wr.path("students")
                .queryParam("class", "FRESHMAN")
                .queryParam("classFrom", "2012")
                .queryParam("classTo", "2016")
                .get(ClientResponse.class);

        // Still more error 400, ARGUMENT_CONFLICT.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.ARGUMENT_CONFLICT,
            clientResponse.getEntity(String.class)
        );
    }

    // TODO Many more to do here...

    @Test
    public void testGetStudentById() {
        // Grab a test fixture student.
        Student student = wr.path("students/1000000").get(Student.class);
        Assert.assertEquals(Long.valueOf(1000000L), student.getId());
        Assert.assertEquals("Berners-Lee", student.getLastName());
        Assert.assertEquals("Tim", student.getFirstName());
        Assert.assertEquals(Integer.valueOf(2016), student.getExpectedGraduationYear());
        
        // The text fixture data has some empty values.
        Assert.assertNull(student.getMiddleName());
        Assert.assertNull(student.getEntryYear());
        Assert.assertEquals(0, student.getRecord().getGrades().size());

        // Grant and event data do not come along for the ride.
        Assert.assertEquals(0, student.getGrants().size());
        Assert.assertEquals(0, student.getAttendance().size());
    }
    
    @Test
    public void testTransferStudent(){
    	// Grab five test fixture students.
    	Student stud0 = wr.path("students/1000000").get(Student.class);
    	Student stud1 = wr.path("students/1000001").get(Student.class);
    	Student stud2 = wr.path("students/1000002").get(Student.class);
    	Student stud3 = wr.path("students/1000003").get(Student.class);
    	Student stud4 = wr.path("students/1000004").get(Student.class);
    	Assert.assertEquals(true, stud0.isTransferStudent());
    	Assert.assertEquals(false, stud1.isTransferStudent());
    	Assert.assertEquals(true, stud2.isTransferStudent());
    	Assert.assertEquals(false, stud3.isTransferStudent());
    	Assert.assertEquals(true, stud4.isTransferStudent());
    	
    }

    @Test
    public void testGetStudentAttendanceById() {
        // Verify that the text fixture event attendance comes out correctly.
        for (long l = 1000000L; l < 1000003L; l++) {
            List<Event> events = wr.path("students/" + l + "/attendance")
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
        List<Event> events = wr.path("students/1000003/attendance")
                .get(new GenericType<List<Event>>(){});
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testGetStudentAttendanceByIdForNonexistentStudent() {
        // We expect a 404 when the student does not exist.
        ClientResponse clientResponse = wr.path("students/2000000/attendance")
                .get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
        Assert.assertEquals("404 " + StudentResource.STUDENT_NOT_FOUND,
                clientResponse.getEntity(String.class));
    }

    @Test
    public void testGetStudentByIdGrades() {
        // This test fixture student has grades in the database, but because we
        // are asking for this via the students URL, those data are not included
        // in transit.
        Assert.assertTrue(wr.path("students/1000002")
                .get(Student.class).getRecord().getGrades().isEmpty());
    }

    @Test
    public void testGetStudentRecordByIdGrades() {
        List<GPA> grades = wr.path("students/1000002/record").get(StudentRecord.class).getGrades();
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
        StudentRecord studentRecord = wr.path("students/1000000/record").get(StudentRecord.class);
        studentRecord.setGrades(grades);

        // Now, save the student record.  We should get a 204.
        ClientResponse response = wr.path("students/1000000/record")
                .put(ClientResponse.class, studentRecord);
        Assert.assertEquals(204, response.getStatus());

        // We check that the grades were indeed saved.
        grades = wr.path("students/1000000/record").get(StudentRecord.class).getGrades();
        Assert.assertEquals(2, grades.size());
        Assert.assertEquals(Term.FALL, grades.get(0).getTerm());
        Assert.assertEquals(2013, grades.get(0).getYear());
        Assert.assertEquals(3.5, grades.get(0).getGpa(), 0.0);

        Assert.assertEquals(Term.SPRING, grades.get(1).getTerm());
        Assert.assertEquals(2014, grades.get(1).getYear());
        Assert.assertEquals(3.75, grades.get(1).getGpa(), 0.0);
    }

}
