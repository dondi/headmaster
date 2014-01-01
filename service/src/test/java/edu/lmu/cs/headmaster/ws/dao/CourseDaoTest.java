package edu.lmu.cs.headmaster.ws.dao;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;

public class CourseDaoTest extends ApplicationContextTest {

    private CourseDao courseDao;

    @Before
    public void getRequiredBeans() {
        courseDao = (CourseDao)applicationContext.getBean("courseDao");
    }

    @Test
    public void testGetCourseById() {
        Assert.assertEquals(Long.valueOf(1000002L), courseDao.getCourseById(1000002L).getId());
        Assert.assertEquals(Long.valueOf(1000000L), courseDao.getCourseById(1000000L).getId());
        Assert.assertEquals(Long.valueOf(1000001L), courseDao.getCourseById(1000001L).getId());
    }

    @Test
    public void testCreateBasicCourse() {
        // Create an id-less course.
        Course courseToCreate = new Course();
        courseToCreate.setNumber("486");
        courseToCreate.setSection("01");
        courseToCreate.setSubject("CMSI");
        courseToCreate.setTerm(Term.FALL);
        courseToCreate.setTitle("Databases");
        courseToCreate.setYear(2020);

        // Save the course.
        courseDao.createCourse(courseToCreate);

        // The created course should now have an ID. Because we know what is in
        // the test fixture, we know what ID to expect.
        Assert.assertEquals(Long.valueOf(1L), courseToCreate.getId());

        // Reload the event that was just created.
        Course createdCourse = courseDao.getCourseById(1L);
        assertSimpleEquality(courseToCreate, createdCourse);
        Assert.assertEquals(0, createdCourse.getObjectives().size());
        Assert.assertEquals(0, createdCourse.getAssignments().size());
        Assert.assertEquals(0, createdCourse.getStudents().size());
    }

    @Test
    public void testCreateOrUpdateCourse() {
        // Grab a course from the fixture.
        Course course = courseDao.getCourseById(1000001L);
        course.setSubject("MUSC");
        courseDao.createOrUpdateCourse(course);
        
        Course updatedCourse = courseDao.getCourseById(1000001L);
        assertSimpleEquality(course, updatedCourse);
    }

    /**
     * Helper function for asserting the equality of two courses.
     */
    private void assertSimpleEquality(Course course1, Course course2) {
        Assert.assertEquals(course1.getId(), course2.getId());
        Assert.assertEquals(course1.getNumber(), course2.getNumber());
        Assert.assertEquals(course1.getSection(), course2.getSection());
        Assert.assertEquals(course1.getSubject(), course2.getSubject());
        Assert.assertEquals(course1.getTerm(), course2.getTerm());
        Assert.assertEquals(course1.getTitle(), course2.getTitle());
        Assert.assertEquals(course1.getYear(), course2.getYear());
    }
}
