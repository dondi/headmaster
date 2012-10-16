package edu.lmu.cs.headmaster.ws.resource;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.resource.UserResource;
import edu.lmu.cs.headmaster.ws.types.Role;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

/**
 * Tests the user web resource.
 */
public class UserResourceTest extends ResourceTest {

    @Test
    public void testGetUserByDifferentUser() {
        // Users may only access themselves.  The test fixtures logs in as "testuser".
        ClientResponse response = wr.path("users/login/admin").get(ClientResponse.class);

        // We should get a 404, so as not to give away an existing user.
        Assert.assertEquals(404, response.getStatus());
        Assert.assertEquals("404 " + UserResource.USER_NOT_FOUND, response.getEntity(String.class));
    }

    @Test
    public void testGetUserByNonexistentLogin() {
        // A non-existent user should return nothing. This should never really
        // happen in practice because the user gets logged in first, which will
        // not happen unless they exist. Still, the test fixture lies outside
        // these rules, so we try this here anyway.
        ClientResponse response = wr.path("users/login/testuser").get(ClientResponse.class);

        // This is a classic 404.
        Assert.assertEquals(404, response.getStatus());
        Assert.assertEquals("404 " + UserResource.USER_NOT_FOUND, response.getEntity(String.class));
    }

    @Test
    public void testGetUserByLogin() {
        // The test fixture does not contain the testuser, so we need to create
        // it first (we assume here that creation works properly; verifying
        // *that* is the job of other unit tests).
        User user = DomainObjectUtils.createUserObject("testuser", "testuser@headmaster.test",
                "testpassword", Role.STUDENT);
        wr.path("users").post(user);

        // Now we test.
        User responseUser = wr.path("users/login/testuser")
                .get(ClientResponse.class)
                .getEntity(User.class);
        Assert.assertEquals(user.getLogin(), responseUser.getLogin());
        Assert.assertEquals(user.getEmail(), responseUser.getEmail());
        Assert.assertEquals(user.isActive(), responseUser.isActive());
        Assert.assertEquals(1, responseUser.getRoles().size());
        Assert.assertEquals(
            user.getRoles().get(0).getRole(),
            responseUser.getRoles().get(0).getRole()
        );

        // Per our database fixture, we know the user ID to expect.
        Assert.assertEquals(Long.valueOf(1L), responseUser.getId());

        // The exception: challenge should not ride along.
        Assert.assertNull(responseUser.getChallenge());
    }

    @Test
    public void testCreateUser() {
        // First, create the user.
        User user = DomainObjectUtils.createUserObject("teacher", "teacher@school.edu", "password",
                    Role.FACULTY, Role.STAFF);

        // Now, save it.  We should get a 201 with a location.
        ClientResponse response = wr.path("users").post(ClientResponse.class, user);
        Assert.assertEquals(201, response.getStatus());

        // Per our database fixture, we know the new user ID (and therefore location) to expect.
        Assert.assertEquals(1, response.getHeaders().get("Location").size());
        Assert.assertEquals(wr.getURI() + "/users/1", response.getHeaders().getFirst("Location"));
    }

    @Test
    public void testCreateOverspecifiedUser() {
        // Create a user with an ID.
        User user = DomainObjectUtils.createUserObject("teacher", "teacher@school.edu", "password",
                Role.FACULTY, Role.STAFF);
        user.setId(78910L);

        // This time, we should not be able to save the user: status 400.
        ClientResponse response = wr.path("users").post(ClientResponse.class, user);

        // We expect error 400, USER_OVERSPECIFIED.
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals(
            "400 " + UserResource.USER_OVERSPECIFIED,
            response.getEntity(String.class)
        );
    }

}
