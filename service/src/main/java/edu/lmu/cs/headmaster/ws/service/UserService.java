package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * JAX-RS user service.
 */
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface UserService {

    /**
     * Possible service error messages.
     */
    String USER_INCONSISTENT = "user.inconsistent";

    /**
     * Returns all known users.  Only users with the HEADMASTER role
     * should be allowed to call this URI.
     * @return the known users
     */
    @GET
    List<User> getUsers();

    /**
     * Creates a new user.
     */
    @POST
    Response createUser(User user);

    /**
     * Creates or updates a user using the given id.
     */
    @PUT
    @Path("{id}")
    Response createOrUpdateUser(@PathParam("id") Integer id, User user);

}
