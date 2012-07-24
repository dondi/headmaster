package edu.lmu.cs.headmaster.ws.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Role;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.domain.UserRole;
import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * Tests the user web service.
 */
public class UserServiceTest extends ServiceTest {

    private UserService userService = context.getBean(UserServiceImpl.class);

    @Test
    public void testCreateUser() {
        // First, create the user.
        User user = createUserObject("teacher", "teacher@school.edu", "password",
                Role.FACULTY, Role.STAFF);

        // Now, save it.  We should get a 201 with a location.
        Response response = userService.createUser(user);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateOverspecifiedUser() {
        // Create a user with an ID.
        User user = createUserObject("teacher", "teacher@school.edu", "password",
                Role.FACULTY, Role.STAFF);
        user.setId(78910L);

        // This time, we should not be able to save the user: status 400.
        try {
            // No need to store the response for this one.
            userService.createUser(user);

            // If we get here, something is wrong.
            Assert.fail("overspecified user should have thrown an exception, but didn't");
        } catch (ServiceException serviceException) {
            // We expect error 400, USER_OVERSPECIFIED.
            Assert.assertEquals(400, serviceException.getResponse().getStatus());
            Assert.assertEquals("400 " + UserService.USER_OVERSPECIFIED,
                    serviceException.getResponse().getEntity().toString());
        }
    }

    /**
     * Helper factory method for creating new user objects.
     */
    private User createUserObject(String login, String email, String challenge, Role... roles) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setChallengeRequest(challenge);

        List<UserRole> userRoles = new ArrayList<UserRole>();
        for (Role role: roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoles.add(userRole);
        }
        
        user.setRoles(userRoles);
        return user;
    }
}
