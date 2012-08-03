package edu.lmu.cs.headmaster.ws.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base class for dao unit tests. Its main purpose is to create and hold the
 * Spring test context that instantiates all of the daos to be tested.
 */
public abstract class DaoTest {

    protected ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("testContext.xml");

}
