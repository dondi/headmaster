package edu.lmu.cs.headmaster.ws.resource;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.lmu.cs.headmaster.ws.domain.Grant;

/**
 * Tests the user web resource.
 */
public class GrantResourceTest extends ResourceTest {

    @Test
    public void testGetGrantByNonexistentId() {
        ClientResponse clientResponse = wr.path("grants/17").get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
    }
    
    @Test
    public void testGetGrantById() {
        /*Grant grant = wr.path("grants/1000000").get(Grant.class);
        Assert.assertEquals(Long.valueOf(1000000L), grant.getId());
        Assert.assertEquals(Integer.valueOf(10000), grant.getAmount());
        Assert.assertEquals("Leonard Kleinrock", grant.getFacultyMentor());
        Assert.assertEquals("The Worldwide Web", grant.getTitle());*/
        System.out.println("\n\n\n\n\n\n\n\n" + "yo!" + "\n\n\n\n\n\n\n\n");
    }

}
