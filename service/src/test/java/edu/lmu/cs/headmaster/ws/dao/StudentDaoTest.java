package edu.lmu.cs.headmaster.ws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.Term;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;

public class StudentDaoTest extends ApplicationContextTest {

    private StudentDao studentDao = (StudentDao)applicationContext.getBean("studentDao");

    @Test
    public void testGetStudentById() {
        // Grab a test fixture student.
        Student student = studentDao.getStudentById(1000000L);
        Assert.assertEquals(Long.valueOf(1000000L), student.getId());
        Assert.assertEquals("Berners-Lee", student.getLastName());
        Assert.assertEquals("Tim", student.getFirstName());
        Assert.assertEquals(Integer.valueOf(2016), student.getExpectedGraduationYear());

        // The text fixture data has some empty values.
        Assert.assertNull(student.getMiddleInitial());
        Assert.assertNull(student.getEntryYear());
        Assert.assertEquals(0, student.getMajors().size());
        Assert.assertEquals(0, student.getMinors().size());
        Assert.assertEquals(0, student.getGrades().size());

        // Grant and event data do not come along for the ride.
        try {
            student.getGrants().size();
            // If this doesn't bork, something is wrong.
            Assert.fail("getGrants should not succeed, but did.");
        } catch(LazyInitializationException lazyInitializationException) {
            // This is what should happen; carry on.
        }

        try {
            student.getAttendance().size();
            // If this doesn't bork, something is wrong.
            Assert.fail("getAttendance should not succeed, but did.");
        } catch(LazyInitializationException lazyInitializationException) {
            // This is what should happen; carry on.
        }
    }

    @Test
    public void testGetStudentByIdGrades() {
        // One of the test fixture students has grades.
        List<GPA> grades = studentDao.getStudentById(1000002L).getGrades();
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
    public void testGetStudentByIdMajorsAndMinors() {
        // One of the test fixture students has majors and minors.
        Student student = studentDao.getStudentById(1000002L);
        Assert.assertEquals(2, student.getMajors().size());
        Assert.assertEquals(1, student.getMinors().size());

        // Majors and minors are manually ordered.
        Assert.assertEquals("Computer Science", student.getMajors().get(0));
        Assert.assertEquals("Mathematics", student.getMajors().get(1));
        Assert.assertEquals("Music", student.getMinors().get(0));
    }

    @Test
    public void testGetStudentsByLastName() {
        // When without commas and not all-digits, the student query is hits on
        // "last name starts with query," case insensitive.
        List<Student> students = studentDao.getStudents("cer", 0, 10);
        Assert.assertEquals(1, students.size());
        Assert.assertEquals(Long.valueOf(1000001L), students.get(0).getId());

        students = studentDao.getStudents("k", 0, 10);
        Assert.assertEquals(2, students.size());
 
        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000004L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000002L), students.get(1).getId());
    }

    // TODO Many more ways to test getStudents...

    @Test
    public void testGetStudentAttendanceById() {
        // Verify that the text fixture event attendance comes out correctly.
        for (long l = 1000000L; l < 1000003L; l++) {
            List<Event> events = studentDao.getStudentAttendanceById(l);
            Assert.assertEquals(1, events.size());
            Assert.assertEquals(Long.valueOf(1000000L), events.get(0).getId());
            Assert.assertEquals("Summit", events.get(0).getTitle());
            Assert.assertEquals("The big one", events.get(0).getDescription());
        }
    }

    @Test
    public void testGetStudentAttendanceByIdForStudentWithNoEvents() {
        // We get a non-null but empty list for a student without events.
        List<Event> events = studentDao.getStudentAttendanceById(1000003L);
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testGetStudentAttendanceByIdForNonexistentStudent() {
        // We expect null attendance when the student does not exist.
        Assert.assertNull(studentDao.getStudentAttendanceById(2000000L));
    }

    @Test
    public void testCreateOrUpdateStudentGrades() {
        // Build the grade list.
        List<GPA> grades = new ArrayList<GPA>();

        // We leave the correctness of the GET to other unit tests.
        GPA gpa = new GPA();
        gpa.setTerm(Term.SPRING);
        gpa.setYear(2017);
        gpa.setGpa(2.75);
        grades.add(gpa);

        gpa = new GPA();
        gpa.setTerm(Term.SUMMER);
        gpa.setYear(2015);
        gpa.setGpa(2.0);
        grades.add(gpa);

        Student student = studentDao.getStudentById(1000000L);
        student.setGrades(grades);
        studentDao.createOrUpdateStudent(student);

        // We check that the grades were indeed saved.
        grades = studentDao.getStudentById(1000000L).getGrades();
        Assert.assertEquals(2, grades.size());
        Assert.assertEquals(Term.SUMMER, grades.get(0).getTerm());
        Assert.assertEquals(2015, grades.get(0).getYear());
        Assert.assertEquals(2.0, grades.get(0).getGpa(), 0.0);

        Assert.assertEquals(Term.SPRING, grades.get(1).getTerm());
        Assert.assertEquals(2017, grades.get(1).getYear());
        Assert.assertEquals(2.75, grades.get(1).getGpa(), 0.0);
    }

    @Test
    public void testCreateOrUpdateStudentAddMajorsAndMinors() {
        // Grab a test fixture student.
        Student student = studentDao.getStudentById(1000001L);

        // Add a major and some minors.
        student.getMajors().add("Biology");
        student.getMinors().add("Physics");
        student.getMinors().add("Chemistry");

        studentDao.createOrUpdateStudent(student);

        // Now re-grab and check.
        student = studentDao.getStudentById(1000001L);
        Assert.assertEquals(1, student.getMajors().size());
        Assert.assertEquals(2, student.getMinors().size());

        Assert.assertEquals("Biology", student.getMajors().get(0));
        Assert.assertEquals("Physics", student.getMinors().get(0));
        Assert.assertEquals("Chemistry", student.getMinors().get(1));
    }

}
