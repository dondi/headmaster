package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.resource.AbstractResource;

/**
 * Tests the term web resource.
 */
public class TermResourceTest extends ResourceTest {

    @Test
    public void testGetMatchingCollegesOrSchoolsNoQuery() {
        ClientResponse clientResponse = wr.path("terms/colleges-or-schools")
                .get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.QUERY_REQUIRED,
            clientResponse.getEntity(String.class)
        );
    }

    @Test
    public void testGetMatchingDegreesNoQuery() {
        ClientResponse clientResponse = wr.path("terms/degrees")
                .get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.QUERY_REQUIRED,
            clientResponse.getEntity(String.class)
        );
    }

    @Test
    public void testGetMatchingDisciplinesNoQuery() {
        ClientResponse clientResponse = wr.path("terms/disciplines")
                .get(ClientResponse.class);

        // We expect error 400, QUERY_REQUIRED.
        Assert.assertEquals(400, clientResponse.getStatus());
        Assert.assertEquals(
            "400 " + AbstractResource.QUERY_REQUIRED,
            clientResponse.getEntity(String.class)
        );
    }

    @Test
    public void testGetMatchingCollegesOrSchools() {
        // "en" should match both science and engineering.
        List<String> results = wr.path("terms/colleges-or-schools")
        		.queryParam("q", "en")
        		.get(new GenericType<List<String>>() {});

        Assert.assertEquals(2, results.size());
        Assert.assertEquals("Engineering", results.get(0));
        Assert.assertEquals("Science", results.get(1));

        // "sc" should match only science.
        results = wr.path("terms/colleges-or-schools")
                .queryParam("q", "sc")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Science", results.get(0));

        // "eer" should match only engineering.
        results = wr.path("terms/colleges-or-schools")
                .queryParam("q", "eer")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Engineering", results.get(0));

        // "zatoichi" should match nothing.
        results = wr.path("terms/colleges-or-schools")
                .queryParam("q", "zatoichi")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testGetMatchingDegrees() {
        // "b" should match both BA and BS.
        List<String> results = wr.path("terms/degrees")
                .queryParam("q", "b")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(2, results.size());
        Assert.assertEquals("BA", results.get(0));
        Assert.assertEquals("BS", results.get(1));

        // "s" should match only BS.
        results = wr.path("terms/degrees")
                .queryParam("q", "s")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("BS", results.get(0));

        // "a" should match only BA.
        results = wr.path("terms/degrees")
                .queryParam("q", "a")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("BA", results.get(0));

        // "bazinga" should match nothing.
        results = wr.path("terms/degrees")
                .queryParam("q", "bazinga")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testGetMatchingDisciplines() {
        // "ic" should match both Mathematics and Music.
        List<String> results = wr.path("terms/disciplines")
                .queryParam("q", "ic")
                .get(new GenericType<List<String>>() {});

        Assert.assertEquals(2, results.size());
        Assert.assertEquals("Mathematics", results.get(0));
        Assert.assertEquals("Music", results.get(1));

        // "comp" should match only Computer Science.
        results = wr.path("terms/disciplines")
                .queryParam("q", "comp")
                .get(new GenericType<List<String>>() {});
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Computer Science", results.get(0));

        // "us" should match only Music.
        results = wr.path("terms/disciplines")
                .queryParam("q", "us")
                .get(new GenericType<List<String>>() {});
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Music", results.get(0));

        // "cucaracha" should match nothing.
        results = wr.path("terms/disciplines")
                .queryParam("q", "cucaracha")
                .get(new GenericType<List<String>>() {});
        Assert.assertEquals(0, results.size());
    }

}
