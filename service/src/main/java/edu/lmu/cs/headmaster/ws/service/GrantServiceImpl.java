package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.dao.GrantDao;
import edu.lmu.cs.headmaster.ws.domain.Grant;

public class GrantServiceImpl extends AbstractService implements GrantService {

    private GrantDao grantDao;

    public GrantServiceImpl(GrantDao grantDao) {
        this.grantDao = grantDao;
    }

    @Override
    public List<Grant> getGrants(String query, Boolean awarded, Boolean presented, int skip, int max) {
        getLogger().debug("getGrants");
        return grantDao.getGrants(query, awarded, presented, skip, max);
    }
    
    @Override
    public Grant createGrant(Grant grant) {
        getLogger().debug("createGrant");
        return grantDao.createGrant(grant);
    }

    @Override
    public void createOrUpdateGrant(Grant grant) {
        getLogger().debug("createOrUpdateGrant");
        grantDao.createOrUpdateGrant(grant);
    }

    @Override
    public Grant getGrantById(Long id) {
        getLogger().debug("getGrantById");
        return grantDao.getGrantById(id);
    }

}
