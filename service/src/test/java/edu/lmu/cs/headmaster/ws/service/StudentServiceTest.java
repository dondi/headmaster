package edu.lmu.cs.headmaster.ws.service;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

/**
 * Tests the student web service.
 */
public class StudentServiceTest extends ServiceTest {

    @Test
    public void testGetStudentsNoQuery() {
        ClientResponse response = ws.path("students").get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals(
            "400 " + AbstractService.QUERY_REQUIRED,
            response.getEntity(String.class)
        );
    }

    // TODO So many more to do...
}
