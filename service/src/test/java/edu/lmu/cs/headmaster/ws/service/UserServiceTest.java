package edu.lmu.cs.headmaster.ws.service;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.lmu.cs.headmaster.ws.domain.Role;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

/**
 * Tests the user web service.
 */
public class UserServiceTest extends ServiceTest {

    @Test
    public void testGetUserByDifferentUser() {
        // Users may only access themselves.  The test fixtures logs in as "testuser".
        ClientResponse response = ws.path("users/login/admin").get(ClientResponse.class);

        // We should get a 404, so as not to give away an existing user.
        Assert.assertEquals(404, response.getStatus());
        Assert.assertEquals("404 " + UserService.USER_NOT_FOUND, response.getEntity(String.class));
    }

    @Test
    public void testGetUserByNonexistentLogin() {
        // A non-existent user should return nothing. This should never really
        // happen in practice because the user gets logged in first, which will
        // not happen unless they exist. Still, the test fixture lies outside
        // these rules, so we try this here anyway.
        ClientResponse response = ws.path("users/login/testuser").get(ClientResponse.class);

        // This is a classic 404.
        Assert.assertEquals(404, response.getStatus());
        Assert.assertEquals("404 " + UserService.USER_NOT_FOUND, response.getEntity(String.class));
    }

    @Test
    public void testCreateUser() {
        // First, create the user.
        User user = DomainObjectUtils.createUserObject("teacher", "teacher@school.edu", "password",
                    Role.FACULTY, Role.STAFF);

        // Now, save it.  We should get a 201 with a location.
        ClientResponse response = ws.path("users").post(ClientResponse.class, user);
        Assert.assertEquals(201, response.getStatus());

        // Per our database fixture, we know the new user ID (and therefore location) to expect.
        Assert.assertEquals(1, response.getHeaders().get("Location").size());
        Assert.assertEquals(ws.getURI() + "/users/1", response.getHeaders().getFirst("Location"));
    }

    @Test
    public void testCreateOverspecifiedUser() {
        // Create a user with an ID.
        User user = DomainObjectUtils.createUserObject("teacher", "teacher@school.edu", "password",
                Role.FACULTY, Role.STAFF);
        user.setId(78910L);

        // This time, we should not be able to save the user: status 400.
        ClientResponse response = ws.path("users").post(ClientResponse.class, user);

        // We expect error 400, USER_OVERSPECIFIED.
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals(
            "400 " + UserService.USER_OVERSPECIFIED,
            response.getEntity(String.class)
        );
    }

}
