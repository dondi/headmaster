package edu.lmu.cs.headmaster.ws.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Role;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

public class UserDaoTest extends ApplicationContextTest {

    private UserDao userDao;

    @Before
    public void getRequiredBeans() {
        userDao = (UserDao)applicationContext.getBean("userDao");
    }

    @Test
    public void testGetUserByLogin() {
        // The test fixture loads an "admin" user.
        User user = userDao.getUserByLogin("admin");

        Assert.assertEquals(Long.valueOf(1000000L), user.getId());
        Assert.assertEquals("admin", user.getLogin());
        Assert.assertEquals("password", user.getChallenge());
        Assert.assertEquals("admin@headmaster.test", user.getEmail());
        Assert.assertEquals(Boolean.TRUE, user.isActive());
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertEquals(user, user.getRoles().get(0).getUser());
        Assert.assertEquals(Role.HEADMASTER, user.getRoles().get(0).getRole());
    }

    @Test
    public void testGetUserByLoginNoRoles() {
        // The test fixture loads a user that does not have any roles.
        User user = userDao.getUserByLogin("noroles");

        Assert.assertEquals(Long.valueOf(1000001L), user.getId());
        Assert.assertEquals("noroles", user.getLogin());
        Assert.assertEquals("password-noroles", user.getChallenge());
        Assert.assertEquals("noroles@headmaster.test", user.getEmail());
        Assert.assertEquals(Boolean.TRUE, user.isActive());
        Assert.assertEquals(0, user.getRoles().size());
    }

    @Test
    public void testCreateUser() {
        // First, create the user.
        User userToCreate = DomainObjectUtils.createUserObject("teacher", "teacher@school.edu",
                "password", Role.FACULTY, Role.STAFF);

        // Now, save it.
        userDao.createUser(userToCreate);

        // Reload the user that was just created. Because we know what is in the
        // test fixture, we know what ID to expect.
        User createdUser = userDao.getUserById(1L);
        Assert.assertEquals(Long.valueOf(1L), createdUser.getId());
        Assert.assertEquals(userToCreate.getLogin(), createdUser.getLogin());
        Assert.assertEquals(userToCreate.getEmail(), createdUser.getEmail());
        Assert.assertEquals(2, createdUser.getRoles().size());

        // We do not make assumptions on which role comes first.
        if (createdUser.getRoles().get(0).getRole() == Role.FACULTY) {
            Assert.assertEquals(Role.FACULTY, createdUser.getRoles().get(0).getRole());
            Assert.assertEquals(Role.STAFF, createdUser.getRoles().get(1).getRole());
        } else {
            Assert.assertEquals(Role.STAFF, createdUser.getRoles().get(0).getRole());
            Assert.assertEquals(Role.FACULTY, createdUser.getRoles().get(1).getRole());
        }
    }

}
