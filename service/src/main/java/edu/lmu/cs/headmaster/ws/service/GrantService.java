package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Grant;

public interface GrantService {

    List<Grant> getGrants(String query, Boolean awarded, Boolean presented, int skip, int max);

    Grant createGrant(Grant grant);

    void createOrUpdateGrant(Grant grant);

    Grant getGrantById(Long id);

}
