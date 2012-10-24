package edu.lmu.cs.headmaster.ws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Major;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;

public class StudentDaoTest extends ApplicationContextTest {

    private StudentDao studentDao;

    @Before
    public void getRequiredBeans() {
        studentDao = (StudentDao)applicationContext.getBean("studentDao");
    }

    @Test
    public void testGetStudentById() {
        // Grab a test fixture student.
        Student student = studentDao.getStudentById(1000000L);
        Assert.assertEquals(Long.valueOf(1000000L), student.getId());
        Assert.assertEquals("Berners-Lee", student.getLastName());
        Assert.assertEquals("Tim", student.getFirstName());
        Assert.assertTrue(student.isActive());
        Assert.assertEquals(Integer.valueOf(2016), student.getExpectedGraduationYear());

        // The text fixture data has some empty values.
        Assert.assertNull(student.getMiddleName());
        Assert.assertNull(student.getEntryYear());
        Assert.assertEquals(0, student.getMajors().size());
        Assert.assertEquals(0, student.getMinors().size());
        Assert.assertEquals(0, student.getRecord().getGrades().size());

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
        List<GPA> grades = studentDao.getStudentById(1000002L).getRecord().getGrades();
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
        Assert.assertEquals("Computer Science", student.getMajors().get(0).getDiscipline());
        Assert.assertEquals("Mathematics", student.getMajors().get(1).getDiscipline());
        Assert.assertEquals("Music", student.getMinors().get(0));
    }
    
    @Test
    public void testGetStudentByGpaWithOnlyMaximum() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                null, 3.0, null, null, null, null, 0, 10);
        Assert.assertEquals(2, students.size());
        Assert.assertEquals(Long.valueOf(1000005), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000006), students.get(1).getId());
    }
    
    @Test
    public void testGetStudentByGpaWithOnlyMinimum() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                3.0, null, null, null, null, null, 0, 10);
        Assert.assertEquals(3, students.size());
        Assert.assertEquals(Long.valueOf(1000007), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000008), students.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000009), students.get(2).getId());
    }

    @Test
    public void testGetStudentByGpaBothMinimumAndMaximum() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                3.39, 3.91, null, null, null, null, 0, 10);
        Assert.assertEquals(2, students.size());
        Assert.assertEquals(Long.valueOf(1000008), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000009), students.get(1).getId());
    }

    @Test
    public void testGetStudentByTermGpaMinimumOnly() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                null, null, 3.0, null, Term.FALL, 2012, 0, 10);
        Assert.assertEquals(3, students.size());
        Assert.assertEquals("Turd", students.get(0).getFirstName());
        Assert.assertEquals("Trevor", students.get(1).getFirstName());
        Assert.assertEquals("Nestor", students.get(2).getFirstName());
    }
    
    @Test
    public void testGetStudentByTermGpaMaximumOnly() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                null, null, null, 3.9, Term.FALL, 2012, 0, 10);
        Assert.assertEquals(2, students.size());
        Assert.assertEquals("Turd", students.get(0).getFirstName());
        Assert.assertEquals("Trevor", students.get(1).getFirstName());
    }
    
    @Test 
    public void testGetStudentsByTermGpaMaxAndMin() {
        List<Student> students = studentDao.getStudents(null, null, null, null, null,
                null, null, 3.7, 3.7, Term.FALL, 2012, 0, 10);
        Assert.assertEquals(1, students.size());
        Assert.assertEquals("McBean", students.get(0).getLastName());
    }
    
    @Test
    public void testGetStudentsByTermGpaAndTransfer() {
        List<Student> students = studentDao.getStudents(null, null, true, null, null,
                null, null, 3.7, 4.0, Term.FALL, 2012, 0, 10);
        Assert.assertEquals(2, students.size());
        Assert.assertEquals("Ferguson", students.get(0).getLastName());
        Assert.assertEquals("McBean", students.get(1).getLastName());
        
        students = studentDao.getStudents(null, null, true, null, null,
                null, null, 4.0, 4.0, Term.FALL, 2012, 0, 10);
        Assert.assertEquals(0, students.size());
        
    }
    
    @Test
    public void testGetStudentsByCumulativeGpaAndTransfer() {
        List<Student> students = studentDao.getStudents(null, null, true, null, null,
                3.5, 4.0, null, null, null, null, 0, 10);
       Assert.assertEquals(2, students.size());
       Assert.assertEquals(Long.valueOf(1000008), students.get(0).getId());
       Assert.assertEquals(Long.valueOf(1000009), students.get(1).getId());
       
       students = studentDao.getStudents(null, null, true, null, null,
               4.0, null, null, null, null, null, 0, 10);
       Assert.assertEquals(0, students.size());
       
    }
    
    @Test
    public void testGetStudentsByLastName() {
        // When without commas and not all-digits, the student query is hits on
        // "last name starts with query," case insensitive.
        List<Student> students = studentDao.getStudents("cer", null, null, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(1, students.size());
        Assert.assertEquals(Long.valueOf(1000001L), students.get(0).getId());

        students = studentDao.getStudents("k", null, null, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(2, students.size());
 
        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000004L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000002L), students.get(1).getId());
    }

    @Test
    public void testGetActiveStudents() {
        List<Student> students = studentDao.getStudents(null, Boolean.TRUE, null, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(8, students.size());

        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000000L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000005L), students.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000004L), students.get(2).getId());
        Assert.assertEquals(Long.valueOf(1000002L), students.get(3).getId());
        Assert.assertEquals(Long.valueOf(1000006L), students.get(4).getId());
        Assert.assertEquals(Long.valueOf(1000007L), students.get(5).getId());
        Assert.assertEquals(Long.valueOf(1000008L), students.get(6).getId());
        Assert.assertEquals(Long.valueOf(1000009L), students.get(7).getId());
    }

    @Test
    public void testGetInactiveStudents() {
        List<Student> students = studentDao.getStudents(null, Boolean.FALSE, null, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(2, students.size());

        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000001L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000003L), students.get(1).getId());
    }

    @Test
    public void testGetTransferStudents() {
        List<Student> students = studentDao.getStudents(null, null, Boolean.TRUE, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(7, students.size());

        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000000L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000005L), students.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000004L), students.get(2).getId());
        Assert.assertEquals(Long.valueOf(1000002L), students.get(3).getId());
        Assert.assertEquals(Long.valueOf(1000006L), students.get(4).getId());
        Assert.assertEquals(Long.valueOf(1000008L), students.get(5).getId());
        Assert.assertEquals(Long.valueOf(1000009L), students.get(6).getId());
    }
    
    @Test
    public void testGetNotTransferStudents() {
        List<Student> students = studentDao.getStudents(null, null, Boolean.FALSE, null, null,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(3, students.size());

        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000001L), students.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000007L), students.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000003L), students.get(2).getId());
    }
    
    @Test
    public void testGetStudentsBySpecificExpectedGraduationYear() {
        List<Student> students = studentDao.getStudents(null, null, null, 2015, 2015,
                null, null, null, null, null, null, 0, 10);
        Assert.assertEquals(2, students.size());

        // Search results are sorted by last name, first name.
        Assert.assertEquals(Long.valueOf(1000001L), students.get(0).getId());
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
    public void testGetMatchingCollegesOrSchools() {
        // "en" should match both science and engineering.
        List<String> results = studentDao.getMatchingCollegesOrSchools("en", 0, 50);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("Engineering", results.get(0));
        Assert.assertEquals("Science", results.get(1));

        // "sc" should match only science.
        results = studentDao.getMatchingCollegesOrSchools("sc", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Science", results.get(0));

        // "eer" should match only engineering.
        results = studentDao.getMatchingCollegesOrSchools("eer", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Engineering", results.get(0));

        // "zatoichi" should match nothing.
        results = studentDao.getMatchingCollegesOrSchools("zatoichi", 0, 50);
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testGetMatchingDegrees() {
        // "b" should match both BA and BS.
        List<String> results = studentDao.getMatchingDegrees("b", 0, 50);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("BA", results.get(0));
        Assert.assertEquals("BS", results.get(1));

        // "s" should match only BS.
        results = studentDao.getMatchingDegrees("s", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("BS", results.get(0));

        // "a" should match only BA.
        results = studentDao.getMatchingDegrees("a", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("BA", results.get(0));

        // "bazinga" should match nothing.
        results = studentDao.getMatchingDegrees("bazinga", 0, 50);
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testGetMatchingDisciplines() {
        // "ic" should match both Mathematics and Music.
        List<String> results = studentDao.getMatchingDisciplines("ic", 0, 50);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("Mathematics", results.get(0));
        Assert.assertEquals("Music", results.get(1));

        // "comp" should match only Computer Science.
        results = studentDao.getMatchingDisciplines("comp", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Computer Science", results.get(0));

        // "us" should match only Music.
        results = studentDao.getMatchingDisciplines("us", 0, 50);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Music", results.get(0));

        // "cucaracha" should match nothing.
        results = studentDao.getMatchingDisciplines("cucaracha", 0, 50);
        Assert.assertEquals(0, results.size());
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
        student.getRecord().setGrades(grades);
        studentDao.createOrUpdateStudent(student);

        // We check that the grades were indeed saved.
        grades = studentDao.getStudentById(1000000L).getRecord().getGrades();
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
        Major major = new Major();
        major.setCollegeOrSchool("Science");
        major.setDegree("BS");
        major.setDiscipline("Biology");

        student.getMajors().add(major);
        student.getMinors().add("Physics");
        student.getMinors().add("Chemistry");

        studentDao.createOrUpdateStudent(student);

        // Now re-grab and check.
        student = studentDao.getStudentById(1000001L);
        Assert.assertEquals(1, student.getMajors().size());
        Assert.assertEquals(2, student.getMinors().size());

        Assert.assertEquals("Biology", student.getMajors().get(0).getDiscipline());
        Assert.assertEquals("Physics", student.getMinors().get(0));
        Assert.assertEquals("Chemistry", student.getMinors().get(1));
    }

}
