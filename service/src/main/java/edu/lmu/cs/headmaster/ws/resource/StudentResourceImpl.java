package edu.lmu.cs.headmaster.ws.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;
import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.StudentRecord;
import edu.lmu.cs.headmaster.ws.types.ClassYear;
import edu.lmu.cs.headmaster.ws.types.Term;

/**
 * The sole implementation of the student resource.
 */
@Path("/students")
public class StudentResourceImpl extends AbstractResource implements StudentResource {

    private StudentDao studentDao;
    
    /**
     * Creates a student resource with the injected dao.
     */
    public StudentResourceImpl(UserDao userDao, StudentDao studentDao) {
        super(userDao);
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> getStudents(String query, Boolean active,
            Boolean transferStudent, ClassYear classYear,
            Integer expectedGraduationYearFrom, Integer expectedGraduationYearTo,
            Double minCumulativeGpa, Double maxCumulativeGpa,
            Double minTermGpa, Double maxTermGpa, 
            Term term, Integer year, int skip, int max) {
        logServiceCall();

        // classYear is mutually exclusive with (expectedGraduationYearFrom,
        // expectedGraduationYearTo).
        validate(
            (classYear == null && expectedGraduationYearFrom == null &&
                            expectedGraduationYearTo == null) ||
                    (classYear != null && expectedGraduationYearFrom == null &&
                            expectedGraduationYearTo == null) ||
                    (classYear == null && (expectedGraduationYearFrom != null ||
                                expectedGraduationYearTo != null)),
            Response.Status.BAD_REQUEST, ARGUMENT_CONFLICT
        );
        
        // make sure cumulative gpa and term gpa queries are mutually exclusive
        boolean gpaTermOrYearAreProvided = term != null || year != null;
        boolean gpaTermAndYearAreProvided = term != null && year != null;
        boolean isCumulativeGpaQuery = (minCumulativeGpa != null || maxCumulativeGpa!= null);
        boolean isTermGpaQuery = (minTermGpa != null || maxTermGpa != null);
                
        if (isCumulativeGpaQuery || isTermGpaQuery) {
            // Validate we have admin privileges
            validatePrivilegedUserCredentials();
            
            // Validate we don't have both, they are mutually exclusive.
            validate(
                (isCumulativeGpaQuery != isTermGpaQuery),
                Response.Status.BAD_REQUEST, ARGUMENT_CONFLICT
            );
            
            // Validate we don't have parameters from the two types of GPA queries mixing with each other
            validate(!(isCumulativeGpaQuery && gpaTermOrYearAreProvided), 
                    Response.Status.BAD_REQUEST, ARGUMENT_CONFLICT);
            // Validate we don't have incomplete term gpa query
            validate(!(isTermGpaQuery && !gpaTermAndYearAreProvided), 
                    Response.Status.BAD_REQUEST, ARGUMENT_CONFLICT);
        } else {
            // Validate we don't only have gpaTerm or gpaYear for querying term gpa
            validate(!gpaTermOrYearAreProvided, Response.Status.BAD_REQUEST, ARGUMENT_CONFLICT);
        }
        
        // At least one of query, classYear, expectedGraduationYearFrom, 
        // expectedGraduationYearTo, or either of the GPA queries must be set.
        validate(query != null || classYear != null || expectedGraduationYearFrom != null ||
                expectedGraduationYearTo != null || transferStudent != null ||
                isCumulativeGpaQuery || isTermGpaQuery,
                Response.Status.BAD_REQUEST, QUERY_REQUIRED);

        // The classYear parameter produces an expected graduation year.
        if (classYear != null) {
            expectedGraduationYearFrom = expectedGraduationYearTo =
                    classYear.getExpectedGraduationYear(new DateTime());
        }
        
        return studentDao.getStudents(
            query != null ? preprocessQuery(query, skip, max, 0, 100) : null,
            active, transferStudent, expectedGraduationYearFrom, expectedGraduationYearTo,
            minCumulativeGpa, maxCumulativeGpa, minTermGpa, maxTermGpa, term, year,
            skip, max
        );
    }

    @Override
    public Response createStudent(Student student) {
        logServiceCall();
        
        validate(student.getId() == null, Response.Status.BAD_REQUEST, STUDENT_OVERSPECIFIED);

        // Dao problems will filter up as exceptions.
        studentDao.createStudent(student);
        return Response.created(URI.create(Long.toString(student.getId()))).build();
    }

    @Override
    public Response createOrUpdateStudent(Long id, Student student) {
        logServiceCall();

        // The student IDs should match.
        validate(id.equals(student.getId()), Response.Status.BAD_REQUEST, STUDENT_INCONSISTENT);

        // The student record does not travel with the incoming student, so we set it here.
        Student currentStudent = studentDao.getStudentById(id);
        if (currentStudent != null) {
            student.setRecord(currentStudent.getRecord());
        }

        // Dao problems will filter up as exceptions.
        studentDao.createOrUpdateStudent(student);
        return Response.noContent().build();
    }

    @Override
    public Student getStudentById(Long id) {
        logServiceCall();

        Student student = studentDao.getStudentById(id);
        validate(student != null, Response.Status.NOT_FOUND, STUDENT_NOT_FOUND);
        return student;
    }

    @Override
    public List<Event> getStudentAttendanceById(Long id) {
        logServiceCall();

        List<Event> events = studentDao.getStudentAttendanceById(id);
        validate(events != null, Response.Status.NOT_FOUND, STUDENT_NOT_FOUND);
        return events;
    }

    @Override
    public StudentRecord getStudentRecordById(Long id) {
        return getStudentById(id).getRecord();
    }

    @Override
    public Response updateStudentRecord(Long id, StudentRecord studentRecord) {
        // Retrieve the full student, assign this record, then save.
        Student student = getStudentById(id);
        student.setRecord(studentRecord);

        // Dao problems will filter up as exceptions.
        studentDao.createOrUpdateStudent(student);
        return Response.noContent().build();
    }

}
