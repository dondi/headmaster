package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Course;

/**
 * Simple dao for course domain objects.
 */
public interface CourseDao {

    /**
     * Returns the course with the given id, or null if no such course exists.
     */
    Course getCourseById(Long id);

    /**
     * Returns a paginated set of courses that match the required query term,
     * skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Course> getCourses(String query, int skip, int year);

    /**
     * Saves the given course, which should have a null id.
     */
    Course createCourse(Course course);

    /**
     * Updates or saves the given course, which should have a non-null id.
     */
    void createOrUpdateCourse(Course course);

}
