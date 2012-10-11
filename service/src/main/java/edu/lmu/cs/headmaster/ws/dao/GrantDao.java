package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Grant;

/**
 * Simple dao for grant domain objects.
 */
public interface GrantDao {

    /**
     * Returns the student with the given id, or null if no such student exists.
     */
    Grant getGrantById(Long id);

    /**
     * Returns a paginated set of grants that match the required query term,
     * skipping the first <code>skip</code> results and returning at most
     * <code>max</code> results.
     */
    List<Grant> getGrants(String query, Boolean awarded, Boolean presented,
            int skip, int max);

    /**
     * Saves the given student, which should have a null id.
     */
    Grant createGrant(Grant grant);

    /**
     * Updates or saves the given student, which should have a non-null id.
     */
    void createOrUpdateGrants(Grant grant);

}
