package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.types.Term;

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
    List<Student> getStudents(String query, Boolean active, Boolean transferStudent,
            Integer expectedGraduationYearFrom, Integer expectedGraduationYearTo,
            Double minCumulativeGpa, Double maxCumulativeGpa,
            Double minTermGpa, Double maxTermGpa, 
            Term term, Integer year, int skip, int max);

    /**
     * Returns the events attended by the student with the given id.
     */
    List<Event> getStudentAttendanceById(Long id);

    /**
     * Returns the currently-saved values for college or school that match the
     * given query term, skipping the first <code>skip</code> results and
     * returning at most <code>max</code> results. Results are returned in
     * alphabetical order.
     */
    List<String> getMatchingCollegesOrSchools(String query, int skip, int max);

    /**
     * Returns the currently-saved values for degree that match the given query
     * term, skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results. Results are returned in alphabetical order.
     */
    List<String> getMatchingDegrees(String query, int skip, int max);

    /**
     * Returns the currently-saved values for discipline (whether stored as a
     * major or minor) that match the given query term, skipping the first
     * <code>skip</code> results and returning at most <code>max</code> results.
     * Results are returned in alphabetical order.
     */
    List<String> getMatchingDisciplines(String query, int skip, int max);

    /**
     * Saves the given student, which should have a null id.
     */
    Student createStudent(Student student);

    /**
     * Updates or saves the given student, which should have a non-null id.
     */
    void createOrUpdateStudent(Student student);

}
