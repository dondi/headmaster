package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.domain.Grant;

public interface GrantService {
    
    String GRANT_OVERSPECIFIED = "grant.overspecified";
    String GRANT_INCONSISTENT = "grant.inconsistent";
    String GRANT_NOT_FOUND = "grant.not.found";

    public List<Grant> getGrants(String query, Boolean awarded, Boolean presented, int skip, int max);

    public Response createGrant(Grant grant);

    public Response createOrUpdateGrant(Long id, Grant grant);

    public Grant getGrantById(Long id);

}
