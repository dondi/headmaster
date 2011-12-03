package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

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
     * Returns a paginated set of students that match the required query term, skipping the first
     * <code>skip</code> results and returning at most <code>max</code> results. The default value
     * of <code>skip</code> is 0 and the default value of <code>max</code> is 50.
     */
    List<Student> getStudents(String query, int skip, int max);

}
