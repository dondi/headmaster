package edu.lmu.cs.headmaster.ws.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

/**
 * Base class for all other service test classes to extend. It defines a shared
 * Spring application context for all service unit tests to use.
 */
public abstract class ServiceTest extends JerseyTest {

    protected static ApplicationContext context;
    
    static
    {
        System.setProperty("jersey.test.port", "4040");
        context = new ClassPathXmlApplicationContext("testContext.xml");
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("edu.lmu.cs.headmaster.ws.service")
               .contextPath("headmaster-web-service-0.1-SNAPSHOT").build();
    }

}
