package edu.lmu.cs.headmaster.ws.service;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.GPA;
import edu.lmu.cs.headmaster.ws.domain.Role;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.Term;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.domain.UserRole;

/**
 * A provider which ensures that JSON content is generated using the <em>natural</em> style,
 * rather than the default <em>mapped</em> style.
 */
@Provider
public class NaturalJsonContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext context;

    private final Class<?>[] types = {

        // Domain classes requiring JSON serialization.
        Event.class,
        GPA.class,
        Role.class,
        Student.class,
        Term.class,
        User.class,
        UserRole.class
    };

    private final List<Class<?>> typeList = Arrays.asList(types);

    /**
     * Creates the context to use the natural JSON style.
     */
    public NaturalJsonContextResolver() throws Exception {
        context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        return (typeList.contains(objectType)) ? context : null;
    }
}
