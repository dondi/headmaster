package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * The JAX-RS service for accessing "terms" in the database---i.e.,
 * currently-stored values for certain fields.
 */
@Produces({MediaType.APPLICATION_JSON})
public interface TermService {

    /**
     * Returns the currently-saved values for college or school that match the
     * given query term.  Results are returned in alphabetical order.
     * 
     * @param query the query
     * @param skip the number of initial results to skip
     * @param max the maximum number of results to display
     * 
     * @return the (paginated) set of terms matching the query parameters;
     *         400 query.required if q is not supplied
     */
    @GET
    @Path("colleges-or-schools")
    List<String> getMatchingCollegesOrSchools(@QueryParam("q") String query,
            @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("50") int max);

    /**
     * Returns the currently-saved values for degree that match the given query
     * term.  Results are returned in alphabetical order.
     */
    @GET
    @Path("degrees")
    List<String> getMatchingDegrees(@QueryParam("q") String query,
            @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("50") int max);

    /**
     * Returns the currently-saved values for discipline (whether stored as a
     * major or minor) that match the given query term.  Results are returned
     * in alphabetical order.
     */
    @GET
    @Path("disciplines")
    List<String> getMatchingDisciplines(@QueryParam("q") String query,
            @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("50") int max);

}
