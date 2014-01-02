package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

/**
 * Tests the course web resource.
 */
public class CourseResourceTest extends ResourceTest {

    @Test
    public void testGetCourseByNonexistentId() {
        ClientResponse clientResponse = wr.path("courses/17").get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
    }

    @Test
    public void testGetCourseById() {
        Course course = wr.path("courses/1000000").get(ClientResponse.class).getEntity(Course.class);
        Assert.assertEquals(Long.valueOf(1000000L), course.getId());
        Assert.assertEquals("CMSI", course.getSubject());
        Assert.assertEquals("370", course.getNumber());
        Assert.assertEquals("01", course.getSection());
        Assert.assertEquals(Term.SPRING, course.getTerm());
        Assert.assertEquals("Interaction Design", course.getTitle());
        Assert.assertEquals(Integer.valueOf(2024), course.getYear());
    }

    @Test
    public void testGetAllCourses() {
        List<Course> fixtureCourses = wr.path("courses").get(new GenericType<List<Course>>(){ });
        Assert.assertEquals(4, fixtureCourses.size());

        // This mainly tests the expected sort order.
        Assert.assertEquals(Long.valueOf(1000002L), fixtureCourses.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000003L), fixtureCourses.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000000L), fixtureCourses.get(2).getId());
        Assert.assertEquals(Long.valueOf(1000001L), fixtureCourses.get(3).getId());
    }

    @Test
    public void testCreateCourse() {
        Course courseToCreate = DomainObjectUtils.createCourseObject("486", "01", "CMSI", Term.FALL, "Databases", 2020);

        ClientResponse response = wr.path("courses").post(ClientResponse.class, courseToCreate);
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void testCreateCourseWithSpecifiedIdProduces400() {
        Course courseToCreate = DomainObjectUtils.createCourseObject("486", "01", "CMSI", Term.FALL, "Databases", 2020);
        courseToCreate.setId(500L);

        ClientResponse response = wr.path("courses").post(ClientResponse.class, courseToCreate);
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdateCourseProduces204() {
        Course courseToUpdate = wr.path("courses/1000000").get(Course.class);

        courseToUpdate.setSubject("MUSC");

        ClientResponse response = wr.path("courses/1000000").put(ClientResponse.class, courseToUpdate);
        Assert.assertEquals(204, response.getStatus());
    }

    @Test
    public void testUpdateCourseInconsistentIdProduces400() {
        Course courseToUpdate = wr.path("courses/1000000").get(Course.class);
        ClientResponse response = wr.path("courses/1000").put(ClientResponse.class, courseToUpdate);
        Assert.assertEquals(400, response.getStatus());
    }

}
