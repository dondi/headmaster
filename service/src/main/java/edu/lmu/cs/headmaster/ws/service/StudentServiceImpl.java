package edu.lmu.cs.headmaster.ws.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;
import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Student;

/**
 * The sole implementation of the student service.
 */
@Path("/students")
public class StudentServiceImpl extends AbstractService implements StudentService {

    private StudentDao studentDao;
    
    /**
     * Creates a student service with the injected dao.
     */
    public StudentServiceImpl(UserDao userDao, StudentDao studentDao) {
        super(userDao);
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> getStudents(String query, int skip, int max) {
        logServiceCall();
        return studentDao.getStudents(preprocessQuery(query, skip, max, 0, 50), skip, max);
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
    public List<GPA> getStudentGradesById(Long id) {
        logServiceCall();
        return null;
    }

}
