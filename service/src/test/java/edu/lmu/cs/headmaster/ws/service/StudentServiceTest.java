package edu.lmu.cs.headmaster.ws.service;

import org.junit.Assert;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * Tests the student web service.
 */
public class StudentServiceTest extends ServiceTest {

    private StudentService studentService = context.getBean(StudentServiceImpl.class);

    @Test
    public void testGetStudentsNoQuery() {
        try {
            // No need to store results for this one.
            studentService.getStudents(null, 0, 100);

            // If we get here, the test has failed.
            Assert.fail("null query should have thrown an exception, but didn't");
        } catch (ServiceException serviceException) {
            // We expect error 400, QUERY_REQUIRED.
            Assert.assertEquals(400, serviceException.getResponse().getStatus());
            Assert.assertEquals("400 " + AbstractService.QUERY_REQUIRED,
                    serviceException.getResponse().getEntity().toString());
        }
    }
}
