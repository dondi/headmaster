package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.domain.Event;

/**
 * Simple dao for event domain objects.
 */
public interface EventDao {

    /**
     * Returns the event with the given id, or null if no such event exists.
     */
    Event getEventById(Long id);

    /**
     * Returns a paginated set of events that match the required query term,
     * skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Event> getEvents(String query, int skip, int max);

    /**
     * Returns a paginated set of events within the given date range, skipping
     * the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Event> getEventsByDate(DateTime startDate, DateTime stopDate, int skip, int max);

    /**
     * Saves the given event, which should have a null id.
     */
    public Event createEvent(Event event);

    /**
     * Updates or saves the given event, which should have a non-null id.
     */
    public void createOrUpdateEvent(Event event);

}
