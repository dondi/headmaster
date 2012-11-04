package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Grant;

/**
 * Simple dao for grant domain objects.
 */
public interface GrantDao {

    /**
     * Returns the grant with the given id, or null if no such grant exists.
     */
    Grant getGrantById(Long id);

    /**
     * Returns a paginated set of grants that match the required query term,
     * skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Grant> getGrants(String query, String awarded, Boolean presented,
            int skip, int max);

    /**
     * Saves the given grant, which should have a null id.
     */
    Grant createGrant(Grant grant);

    /**
     * Updates or saves the given grant, which should have a non-null id.
     */
    void createOrUpdateGrant(Grant grant);

}
