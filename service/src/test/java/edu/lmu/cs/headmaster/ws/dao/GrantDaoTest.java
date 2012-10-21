package edu.lmu.cs.headmaster.ws.dao;

//import java.util.List;

import junit.framework.Assert;

//import org.joda.time.DateTime;
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
}
