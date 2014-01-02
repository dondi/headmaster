package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * The JAX-RS interface for operating on course resources.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CourseResource {
    /**
     * Possible resource error messages.
     */
    String COURSE_OVERSPECIFIED = "course.overspecified";
    String COURSE_INCONSISTENT = "course.inconsistent";
    String COURSE_NOT_FOUND = "course.not.found";
    String COURSE_QUERY_PARAMETERS_MISSING = "course.query.parameters.missing";

    /**
     * Returns courses according to the search parameters
     *
     * @param query
     *            the query
     * @param skip
     *            the number of initial results to skip
     * @param max
     *            the maximum number of results to display
     *
     * @return the (paginated) set of courses matching the query parameters
     */
    @GET
    List<Course> getCourses(@QueryParam("q") String query, @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("100") int max);

    /**
     * Creates a course for which the server will generate the id.
     *
     * @param course
     *            the course object to create. The course must have a null id.
     * @return A response with HTTP 201 on success, or a response with HTTP 400 and message
     *         <code>course.overspecified</code> if the course's id is not null.
     */
    @POST
    Response createCourse(Course course);

    /**
     * Saves the representation of the course with the given id. Inconsistent data should result in HTTP 400,
     * while a successful PUT should return Response.noContent.
     *
     * @param id
     *            the id of the course to save.
     * @return A response with HTTP 204 no content on success, or a response with HTTP 400 and message
     *         <code>course.inconsistent</code> if checked data does not have the save id as requested in the URL.
     */
    @PUT
    @Path("{id}")
    @RolesAllowed({"headmaster", "faculty", "staff"})
    Response createOrUpdateCourse(@PathParam("id") Long id, Course course);

    /**
     * Returns the course with the given id.
     *
     * @param id
     *            the id of the requested course.
     * @return the course with the given id.
     * @throws ServiceException
     *             if there is no course with the given id, causing the framework to generate an HTTP 404.
     */
    @GET
    @Path("{id}")
    Course getCourseById(@PathParam("id") Long id);

}
