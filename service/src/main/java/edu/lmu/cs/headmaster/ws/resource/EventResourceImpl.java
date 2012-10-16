package edu.lmu.cs.headmaster.ws.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.joda.time.Interval;

import edu.lmu.cs.headmaster.ws.dao.EventDao;
import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Event;

/**
 * The sole implementation of the event resource.
 */
@Path("/events")
public class EventResourceImpl extends AbstractResource implements EventResource {

    private EventDao eventDao;

    /**
     * Creates an event resource with the injected daos.
     */
    public EventResourceImpl(UserDao userDao, EventDao eventDao) {
        super(userDao);
        this.eventDao = eventDao;
    }

    @Override
    public List<Event> getEvents(String query, String startDate, String stopDate, int skip,
            int max) {
        logServiceCall();

        // First, check the parameters for the right query.
        boolean textQuery = (query != null) && (startDate == null) && (stopDate == null);
        boolean dateQuery = (query == null) && (startDate != null) && (stopDate != null); 
        validate(textQuery || dateQuery, Response.Status.BAD_REQUEST, EVENT_QUERY_PARAMETERS_BAD);

        // Issue the query according to the determined type.
        if (textQuery) {
            return eventDao.getEvents(query, skip, max);
        } else { // Sure to be dateQuery at this point.
            Interval interval = validateInterval(startDate, stopDate);
            return eventDao.getEventsByDate(interval.getStart(), interval.getEnd(), skip, max);
        }
    }

    @Override
    public Response createEvent(Event event) {
        logServiceCall();
        validate(event.getId() == null, Response.Status.BAD_REQUEST, EVENT_OVERSPECIFIED);
        eventDao.createEvent(event);
        return Response.created(URI.create(Long.toString(event.getId()))).build();
    }

    @Override
    public Response createOrUpdateEvent(Long id, Event event) {
        logServiceCall();
        validate(id.equals(event.getId()), Response.Status.BAD_REQUEST, EVENT_INCONSISTENT);
        eventDao.createOrUpdateEvent(event);
        return Response.noContent().build();
    }

    @Override
    public Event getEventById(Long id) {
        logServiceCall();

        Event event = eventDao.getEventById(id);
        validate(event != null, Response.Status.NOT_FOUND, EVENT_NOT_FOUND);
        return event;
    }

}
