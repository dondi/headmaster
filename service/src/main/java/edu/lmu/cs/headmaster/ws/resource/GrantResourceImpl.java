package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.GrantDao;
import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Grant;

@Path("/grants")
public class GrantResourceImpl extends AbstractResource implements GrantResource {
    
    private GrantDao grantDao;
    
    public GrantResourceImpl(UserDao userDao, GrantDao grantDao) {
        super(userDao);
        this.grantDao = grantDao;
    }

    @Override
    public List<Grant> getGrants(@QueryParam("q") String query,
            @QueryParam("awarded") @DefaultValue("true") Boolean awarded,
            @QueryParam("presented") @DefaultValue("true") Boolean presented,
            @QueryParam("skip") @DefaultValue("0") int skip,
            @QueryParam("max") @DefaultValue("100") int max) {
        logServiceCall();
        
        validate(query != null, Response.Status.BAD_REQUEST, QUERY_REQUIRED);
        
        return grantDao.getGrants(preprocessQuery(query, skip, max, 0, 100), awarded, presented, skip, max);
    }

    @Override
    public Response createGrant(Grant grant) {
        logServiceCall();
        validate(grant.getId() == null, Response.Status.BAD_REQUEST, GRANT_OVERSPECIFIED);
        grantDao.createGrant(grant);
        
        return Response.created(java.net.URI.create(Long.toString(grant.getId()))).build();
    }

    @Override
    public Response createOrUpdateGrant(@PathParam("id") Long id, Grant grant) {
        logServiceCall();
        
        validate(id.equals(grant.getId()), Response.Status.BAD_REQUEST, GRANT_INCONSISTENT);
        
        grantDao.createOrUpdateGrants(grant);
        
        return Response.noContent().build();
    }

    @Override
    public Grant getGrantById(@PathParam("id") Long id) {
        logServiceCall();
        
        Grant grant = grantDao.getGrantById(id);
        validate(grant != null, Response.Status.NOT_FOUND, GRANT_NOT_FOUND);
        
        return grant;
    }

}
