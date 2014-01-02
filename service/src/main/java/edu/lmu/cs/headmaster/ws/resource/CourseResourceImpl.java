package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.service.CourseService;

@Path("/courses")
public class CourseResourceImpl extends AbstractResource implements CourseResource {

    private CourseService courseService;
    
    // TODO userDao must become userService when that is available.
    public CourseResourceImpl(UserDao userDao, CourseService courseService) {
        super(userDao);
        this.courseService = courseService;
    }

    @Override
    public List<Course> getCourses(String query, int skip, int max) {
        logServiceCall();

        // We allow a null query for courses, for now.
        return courseService.getCourses(preprocessNullableQuery(query, skip, max, 0, 100), skip, max);
    }

    @Override
    public Response createCourse(Course course) {
        logServiceCall();

        validate(course.getId() == null, Response.Status.BAD_REQUEST, COURSE_OVERSPECIFIED);
        course = courseService.createCourse(course);

        return Response.created(java.net.URI.create(Long.toString(course.getId()))).build();
    }

    @Override
    public Response createOrUpdateCourse(Long id, Course course) {
        logServiceCall();

        validate(id.equals(course.getId()), Response.Status.BAD_REQUEST, COURSE_INCONSISTENT);
        courseService.createOrUpdateCourse(course);

        return Response.noContent().build();
    }

    @Override
    public Course getCourseById(Long id) {
        logServiceCall();

        Course course = courseService.getCourseById(id);
        validate(course != null, Response.Status.NOT_FOUND, COURSE_NOT_FOUND);

        return course;
    }

}
