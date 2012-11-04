package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.Grant;
import edu.lmu.cs.headmaster.ws.service.GrantService;
import edu.lmu.cs.headmaster.ws.types.GrantStatus;
import edu.lmu.cs.headmaster.ws.util.ServiceException;

@Path("/grants")
public class GrantResourceImpl extends AbstractResource implements GrantResource {

    private GrantService grantService;

    // TODO userDao must become userService when that is available.
    public GrantResourceImpl(UserDao userDao, GrantService grantService) {
        super(userDao);
        this.grantService = grantService;
    }

    @Override
    public List<Grant> getGrants(String query, String grantAwarded, Boolean grantPresented, int skip, int max) {
        logServiceCall();

        validate((query != null || grantAwarded != null || grantPresented != null), Response.Status.BAD_REQUEST, GRANT_QUERY_PARAMETERS_MISSING);
        // TODO: Ask Dr. Dondi about this; this is kind of hoakey
        if(grantAwarded != null) {
            try {
                GrantStatus awarded = GrantStatus.valueOf(grantAwarded);
            } catch (Exception e) {
                throw new ServiceException(Response.Status.BAD_REQUEST, GRANT_AWARDED_STATUS_INCORRECT);
            }
        }
        return grantService.getGrants(preprocessNullableQuery(query, skip, max, 0, 100), grantAwarded,
                grantPresented, skip, max);
    }

    @Override
    public Response createGrant(Grant grant) {
        logServiceCall();

        validate(grant.getId() == null, Response.Status.BAD_REQUEST, GRANT_OVERSPECIFIED);
        grant = grantService.createGrant(grant);

        return Response.created(java.net.URI.create(Long.toString(grant.getId()))).build();
    }

    @Override
    public Response createOrUpdateGrant(Long id, Grant grant) {
        logServiceCall();

        validate(id.equals(grant.getId()), Response.Status.BAD_REQUEST, GRANT_INCONSISTENT);
        grantService.createOrUpdateGrant(grant);

        return Response.noContent().build();
    }

    @Override
    public Grant getGrantById(Long id) {
        logServiceCall();

        Grant grant = grantService.getGrantById(id);
        validate(grant != null, Response.Status.NOT_FOUND, GRANT_NOT_FOUND);

        return grant;
    }

}
