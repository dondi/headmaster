package edu.lmu.cs.headmaster.ws.dao;

//import java.util.List;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Grant;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;
//import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

public class GrantDaoTest extends ApplicationContextTest {

    private GrantDao grantDao;

    @Before
    public void getRequiredBeans() {
        grantDao = (GrantDao)applicationContext.getBean("grantDao");
    }

    @Test
    public void testGetGrantById() {
        // Grab the known event in the fixture.
        Grant grant = grantDao.getGrantById(1000000L);

        Assert.assertEquals(Long.valueOf(1000000L), grant.getId());
        Assert.assertEquals(10000, grant.getAmount().intValue());
        Assert.assertEquals("Leonard Kleinrock", grant.getFacultyMentor());
        Assert.assertEquals("The Worldwide Web", grant.getTitle());
    }

    @Test
    public void testGetGrantsByFacultyMentor() {
        // Use a text query that can be found in the grant faculty mentor.
        // We intentionally mix case to validate the case insensitivity.
        List<Grant> grants = grantDao.getGrants("LeOnarD", null, null, 0, 5);

        // There should only be one grant there.  We'll check just the ID.
        Assert.assertEquals(1, grants.size());
        Assert.assertEquals(Long.valueOf(1000000L), grants.get(0).getId());
    }

    @Test
    public void testGetGrantsByTitle() {
        List<Grant> grants = grantDao.getGrants("The world", null, null, 0, 5);
        Assert.assertEquals(1, grants.size());
        Assert.assertEquals(Long.valueOf(1000000L), grants.get(0).getId());
    }

    @Test
    public void testGetGrantsByInvalidQuery() {
        List<Grant> grants = grantDao.getGrants("blarg", null, null, 0, 5);
        Assert.assertEquals(0, grants.size());
    }

}
