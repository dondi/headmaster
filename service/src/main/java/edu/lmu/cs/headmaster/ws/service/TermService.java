package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

public interface TermService {

    /**
     * Returns the currently-saved values for college or school that match the
     * given query term.  Results are returned in alphabetical order.
     */
	List<String> getMatchingCollegesOrSchools(String query, int skip, int max);

    /**
     * Returns the currently-saved values for degree that match the given query
     * term.  Results are returned in alphabetical order.
     */
	List<String> getMatchingDegrees(String query, int skip, int max);

    /**
     * Returns the currently-saved values for discipline (whether stored as a
     * major or minor) that match the given query term.  Results are returned
     * in alphabetical order.
     */
	List<String> getMatchingDisciplines(String query, int skip, int max);
}
