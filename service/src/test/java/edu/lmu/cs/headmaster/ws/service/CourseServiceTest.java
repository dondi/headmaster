package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

public class CourseServiceTest extends ApplicationContextTest {

    private CourseService courseService;
    
    @Before
    public void setUp() {
        courseService = (CourseService)applicationContext.getBean("courseService");
    }

    @Test
    public void testGetCourseById() {
        Assert.assertEquals(Long.valueOf(1000002L), courseService.getCourseById(1000002L).getId());
        Assert.assertEquals(Long.valueOf(1000000L), courseService.getCourseById(1000000L).getId());
        Assert.assertEquals(Long.valueOf(1000001L), courseService.getCourseById(1000001L).getId());
    }

    @Test
    public void testGetAllCourses() {
        List<Course> fixtureCourses = courseService.getCourses(null, 0, 100);
        Assert.assertEquals(4, fixtureCourses.size());
        
        // This mainly tests the expected sort order.
        Assert.assertEquals(Long.valueOf(1000002L), fixtureCourses.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000003L), fixtureCourses.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000000L), fixtureCourses.get(2).getId());
        Assert.assertEquals(Long.valueOf(1000001L), fixtureCourses.get(3).getId());
    }

    @Test
    public void testCreateBasicCourse() {
        // Create an id-less course.
        Course courseToCreate = DomainObjectUtils.createCourseObject("486", "01", "CMSI", Term.FALL, "Databases", 2020);

        // Save the course.
        courseService.createCourse(courseToCreate);

        // The created course should now have an ID. Because we know what is in
        // the test fixture, we know what ID to expect.
        Assert.assertEquals(Long.valueOf(1L), courseToCreate.getId());

        // Reload the event that was just created.
        Course createdCourse = courseService.getCourseById(1L);
        DomainObjectUtils.assertSimpleEquality(courseToCreate, createdCourse);
        Assert.assertEquals(0, createdCourse.getObjectives().size());
        Assert.assertEquals(0, createdCourse.getAssignments().size());
        Assert.assertEquals(0, createdCourse.getStudents().size());
    }

    @Test
    public void testCreateOrUpdateCourse() {
        // Grab a course from the fixture.
        Course course = courseService.getCourseById(1000001L);
        course.setSubject("MUSC");
        courseService.createOrUpdateCourse(course);
        
        Course updatedCourse = courseService.getCourseById(1000001L);
        DomainObjectUtils.assertSimpleEquality(course, updatedCourse);
    }

}
