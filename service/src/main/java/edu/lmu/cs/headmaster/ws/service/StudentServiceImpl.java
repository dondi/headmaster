package edu.lmu.cs.headmaster.ws.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;
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
    public StudentServiceImpl(StudentDao studentDao) {
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
        
        validate(student.getId() == null, 400, STUDENT_OVERSPECIFIED);

        // Dao problems will filter up as exceptions.
        studentDao.createStudent(student);
        return Response.created(URI.create(Long.toString(student.getId()))).build();
    }

    @Override
    public Response createOrUpdateStudent(Long id, Student student) {
        logServiceCall();
        
        // The student IDs should match.
        validate(id.equals(student.getId()), 400, STUDENT_INCONSISTENT);
        
        // Dao problems will filter up as exceptions.
        studentDao.createOrUpdateStudent(student);
        return Response.noContent().build();
    }

    @Override
    public Student getStudentById(Long id) {
        logServiceCall();

        Student student = studentDao.getStudentById(id);
        validate(student != null, 404, STUDENT_NOT_FOUND);
        return student;
    }
    
}
