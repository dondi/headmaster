package edu.lmu.cs.headmaster.ws.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base class for Spring bean unit tests, such as daos and service
 * implementations. Its main purpose is to create and hold the Spring test
 * context that instantiates all of the beans that may be tested.
 */
public abstract class ApplicationContextTest {

    protected ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("testContext.xml");

}
