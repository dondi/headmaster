package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;

/**
 * Simple dao for student domain objects.
 */
public interface StudentDao {

    /**
     * Returns the student with the given id, or null if no such student exists.
     */
    Student getStudentById(Long id);

    /**
     * Returns a paginated set of students that match the required query term,
     * skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Student> getStudents(String query, int skip, int max);

    /**
     * Returns the events attended by the student with the given id.
     */
    List<Event> getStudentAttendance(Long id);

    /**
     * Saves the given student, which should have a null id.
     */
    Student createStudent(Student student);

    /**
     * Updates or saves the given student, which should have a non-null id.
     */
    void createOrUpdateStudent(Student student);

}
