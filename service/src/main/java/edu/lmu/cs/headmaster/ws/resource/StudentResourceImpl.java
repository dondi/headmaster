package edu.lmu.cs.headmaster.ws.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.StudentRecord;
import edu.lmu.cs.headmaster.ws.service.StudentService;
import edu.lmu.cs.headmaster.ws.types.ClassYear;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * The sole implementation of the student resource.
 */
@Path("/students")
public class StudentResourceImpl extends AbstractResource implements StudentResource {

    private StudentService studentService;

    /**
     * Creates a student resource with the injected dao.
     */
    public StudentResourceImpl(UserDao userDao, StudentService studentService) {
        super(userDao);
        this.studentService = studentService;
    }
    
    /**
     * Returns students according to the search parameters
     * 
     * @param query the query
     * @param skip the number of initial results to skip
     * @param max the maximum number of results to display
     * @return the (paginated) set of students matching the query parameters
     */
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
        // TODO This argument processing is part of the business logic, and not
        //      resource-specific; it should be transferred to the service layer.
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
        
        return studentService.getStudents(
            query != null ? preprocessQuery(query, skip, max, 0, 100) : null,
            active, transferStudent, expectedGraduationYearFrom, expectedGraduationYearTo,
            minCumulativeGpa, maxCumulativeGpa, minTermGpa, maxTermGpa, term, year,
            skip, max
        );
    }

    /**
     * Creates a student for which the server will generate the id.
     *
     * @param student the student object to create. The student must have a null id.
     * @return A response with HTTP 201 on success, or a response with HTTP 400 and message
     * <code>student.overspecified</code> if the student's id is not null.
     */
    @Override
    public Response createStudent(Student student) {
        logServiceCall();
        
        validate(student.getId() == null, Response.Status.BAD_REQUEST, STUDENT_OVERSPECIFIED);

        // Dao problems will filter up as exceptions.
        studentService.createStudent(student);
        return Response.created(URI.create(Long.toString(student.getId()))).build();
    }

    /**
     * Supposed to save the representation of the student with the given id.
     * Inconsistent data should result in HTTP 400, while a successful PUT
     * should return Response.noContent.
     * 
     * @param id the id of the student to save.
     * @return A response with HTTP 204 no content on success, or a response
     *         with HTTP 400 and message <code>student.inconsistent</code> if
     *         checked data does not have the save id as requested in the URL.
     */
    @Override
    public Response createOrUpdateStudent(Long id, Student student) {
        logServiceCall();

        // The student IDs should match.
        validate(id.equals(student.getId()), Response.Status.BAD_REQUEST, STUDENT_INCONSISTENT);

        // Dao problems will filter up as exceptions.
        studentService.createOrUpdateStudent(id, student);
        return Response.noContent().build();
    }

    /**
     * Returns the student with the given id.
     *
     * @param id the id of the requested student.
     * @return the student with the given id.
     * @throws ServiceException if there is no student with the given id, causing the framework
     * to generate an HTTP 404.
     */
    @Override
    public Student getStudentById(Long id) {
        logServiceCall();

        Student student = studentService.getStudentById(id);
        validate(student != null, Response.Status.NOT_FOUND, STUDENT_NOT_FOUND);
        return student;
    }

    /**
     * Returns the events attended by the student with the given id.
     *
     * @param id the id of the requested student.
     * @return the events attended by the student with the given id.
     * @throws ServiceException if there is no student with the given id, causing the framework
     * to generate an HTTP 404.
     */
    @Override
    public List<Event> getStudentAttendanceById(Long id) {
        logServiceCall();

        List<Event> events = studentService.getStudentAttendanceById(id);
        validate(events != null, Response.Status.NOT_FOUND, STUDENT_NOT_FOUND);
        return events;
    }

    /**
     * Returns the student record for the student with the given id.
     */
    @Override
    public StudentRecord getStudentRecordById(Long id) {
        return getStudentById(id).getRecord();
    }

    /**
     * Updates the record for the student with the given id.
     */
    @Override
    public Response updateStudentRecord(Long id, StudentRecord studentRecord) {
        // Dao problems will filter up as exceptions.
        studentService.updateStudentRecord(id, studentRecord);
        return Response.noContent().build();
    }

}
