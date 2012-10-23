package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

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
        Grant grant = wr.path("grants/1000000").get(ClientResponse.class).getEntity(Grant.class);
        Assert.assertEquals(Long.valueOf(1000000L), grant.getId());
        Assert.assertEquals(Integer.valueOf(10000), grant.getAmount());
        Assert.assertEquals("Leonard Kleinrock", grant.getFacultyMentor());
        Assert.assertEquals("The Worldwide Web", grant.getTitle());
    }
    
    @Test(expected = UniformInterfaceException.class)
    public void testGetGrantsWithoutQueryThrowsException() {
        wr.path("grants").get(List.class);
    }
    
    @Test
    public void testGetGrantsQueryByName() {
        
        List<Grant> grants = wr.path("grants")
                .queryParam("q", "leonard")
                .get(new GenericType<List<Grant>>(){});
        
        //List<Grant> grants = wr.path("grants?q=leonard").get(ClientResponse.class).getEntity(List.class);
        Assert.assertEquals(grants.size(), 1);
    }
}
