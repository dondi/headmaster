package edu.lmu.cs.headmaster.ws.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;


public class StudentServiceTest extends ApplicationContextTest {
    
    private StudentService studentService;
    
    @Before
    public void setUp() {
        studentService = (StudentService)applicationContext.getBean("studentService");
    }
    
    @Test
    public void isDaoInjected() {
        Student student = studentService.getStudentById(1000000L);
        Assert.assertEquals(Long.valueOf(1000000L), student.getId());
        Assert.assertEquals("Berners-Lee", student.getLastName());
        Assert.assertEquals("Tim", student.getFirstName());
        Assert.assertTrue(student.isActive());
        Assert.assertEquals(Integer.valueOf(2016), student.getExpectedGraduationYear());
    }

}
