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

/**
 * Tests the user web service.
 */
public class UserServiceTest extends ServiceTest {

    private UserService userService = context.getBean(UserServiceImpl.class);

    @Test
    public void testCreateUser() {
        // First, create the user.
        User user = new User();
        user.setLogin("teacher");
        user.setEmail("teacher@school.edu");
        user.setChallengeRequest("password");

        List<UserRole> roles = new ArrayList<UserRole>();

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(Role.FACULTY);
        roles.add(userRole);

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(Role.STAFF);
        roles.add(userRole);
        
        user.setRoles(roles);

        // Now, save it.  We should get a 201 with a location.
        Response response = userService.createUser(user);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }

}
