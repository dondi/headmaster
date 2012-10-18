package edu.lmu.cs.headmaster.ws.resource;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.AppDescriptor;

public class UnauthorizedUserResourceTest extends ResourceTest {

    @Override
    protected AppDescriptor configure() {
        return configure("edu.lmu.cs.headmaster.ws.resource.SecurityContextUnauthorizedUserContainerRequestFilter");
    }
    
    @Test
    public void getStudentsByCumulativeGpaNotAsAdminThrowsException() {
        ClientResponse response = wr.path("students")
                .queryParam("cumulativeGpaFrom", "3.0")
                .queryParam("cumulativeGpaTo", "4.0")
                .get(ClientResponse.class);
                
        Assert.assertEquals(403, response.getStatus());
    }

}
