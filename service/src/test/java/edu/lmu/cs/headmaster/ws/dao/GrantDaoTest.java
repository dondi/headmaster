package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Grant;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

public class GrantDaoTest extends ApplicationContextTest {

    private GrantDao grantDao;

    @Before
    public void getRequiredBeans() {
        grantDao = (GrantDao) applicationContext.getBean("grantDao");
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

        // There should only be one grant there. We'll check just the ID.
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

    @Test
    public void testCreateGrant() {
        // Create an id-less grant.
        Grant grantToCreate = DomainObjectUtils.createGrantObject(25000, "Reading", "Rainbow");

        grantDao.createGrant(grantToCreate);

        // The created grant should now have an ID of 1 because there is
        // nothing else in the text fixture.
        Assert.assertEquals(Long.valueOf(1L), grantToCreate.getId());

        // Reload the grant that was just created.
        Grant createdGrant = grantDao.getGrantById(1L);
        assertSimpleEquality(grantToCreate, createdGrant);
    }

    // insert into researchgrant(id, amount, facultymentor, title) values(1000000, 10000, 'Leonard Kleinrock', 'The
    // Worldwide Web');
    /*
     * Grant getGrantById(Long id); List<Grant> getGrants(String query, Boolean awarded, Boolean presented, int skip,
     * int max); Grant createGrant(Grant grant); void createOrUpdateGrants(Grant grant);
     */

    @Test
    public void testCreateAndUpdateGrant() {
        // Create an id-less grant.
        Grant grantToCreate = DomainObjectUtils.createGrantObject(25000, "Reading", "Rainbow");
        Grant grantToReplaceWith = DomainObjectUtils.createGrantObject(12500, "Watching TV", "Monotone");

        grantDao.createGrant(grantToCreate);

        // Keep the ID of the created grant to make sure it does not change when updated.
        Long createdGrantId = grantToCreate.getId();
        grantToReplaceWith.setId(createdGrantId);

        // Reload the grant that was just created with a new grant with the same ID.
        grantDao.createOrUpdateGrants(grantToReplaceWith);
        Grant createdGrant = grantDao.getGrantById(createdGrantId);

        assertSimpleEquality(grantToReplaceWith, createdGrant);
        Assert.assertNotSame(createdGrant.getFacultyMentor(), grantToCreate.getFacultyMentor());
        Assert.assertNotSame(createdGrant.getDescription(), grantToCreate.getDescription());
    }

    /**
     * Helper function for asserting the equality of two grants.
     */
    private void assertSimpleEquality(Grant grant1, Grant grant2) {
        Assert.assertEquals(grant1.getId(), grant2.getId());
        Assert.assertEquals(grant1.getAmount(), grant2.getAmount());
        Assert.assertEquals(grant1.getTitle(), grant2.getTitle());
        Assert.assertEquals(grant1.getFacultyMentor(), grant2.getFacultyMentor());
    }
}
