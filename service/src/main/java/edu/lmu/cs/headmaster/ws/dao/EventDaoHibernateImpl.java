package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.domain.Event;

/**
 * Hibernate implementation of the event dao.
 */
public class EventDaoHibernateImpl extends HibernateDaoSupport implements EventDao {

    @Override
    public Event getEventById(Long id) {
        return getHibernateTemplate().get(Event.class, id);
    }

    @Override
    public List<Event> getEvents(String query, int skip, int max) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> getEventsByDate(DateTime startDate, DateTime stopDate, int skip, int max) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Event createEvent(Event event) {
        getHibernateTemplate().save(event);
        return event;
    }

    @Override
    public void createOrUpdateEvent(Event event) {
        getHibernateTemplate().saveOrUpdate(event);
    }

}
