package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * The JAX-RS interface for operating on event resources.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface EventResource {

    /**
     * Possible resource error messages.
     */
    String EVENT_OVERSPECIFIED = "event.overspecified";
    String EVENT_INCONSISTENT = "event.inconsistent";
    String EVENT_NOT_FOUND = "event.not.found";
    String EVENT_QUERY_PARAMETERS_BAD = "event.query.parameters.bad";

    /**
     * Returns events according to the search parameters. Two queries are
     * possible: a generic "free text" search (q parameter) and a date range
     * search (from and to parameters).  These queries are mutually exclusive.
     *
     * @param query
     *            a free-text query
     * @param startDate
     *            the start date for a date range query
     * @param stopDate
     *            the stop date for a date range query
     * @param skip
     *            the number of initial results to skip
     * @param max
     *            the maximum number of results to display
     * 
     * @return the (paginated) set of events matching the query parameters
     */
    @GET
    List<Event> getEvents(@QueryParam("q") String query,
            @QueryParam("from") String startDate, @QueryParam("to") String stopDate,
            @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("50") int max);

    /**
     * Creates an event for which the server will generate the id.
     *
     * @param event the event object to create. The event must have a null id.
     * @return A response with HTTP 201 on success, or a response with HTTP 400 and message
     * <code>event.overspecified</code> if the event's id is not null.
     */
    @POST
    Response createEvent(Event event);

    /**
     * Supposed to save the representation of the event with the given id.
     * Inconsistent data should result in HTTP 400, while a successful PUT
     * should return Response.noContent.
     * 
     * @param id the id of the event to save.
     * @return A response with HTTP 204 no content on success, or a response
     *         with HTTP 400 and message <code>event.inconsistent</code> if
     *         checked data does not have the save id as requested in the URL.
     */
    @PUT
    @Path("{id}")
    Response createOrUpdateEvent(@PathParam("id") Long id, Event event);

    /**
     * Returns the event with the given id.
     *
     * @param id the id of the requested event.
     * @return the event with the given id.
     * @throws ServiceException if there is no event with the given id, causing the framework
     * to generate an HTTP 404.
     */
    @GET
    @Path("{id}")
    Event getEventById(@PathParam("id") Long id);

}
