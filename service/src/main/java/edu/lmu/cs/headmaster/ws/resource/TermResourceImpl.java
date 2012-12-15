package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.Path;

import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.service.TermService;

/**
 * The sole implementation of the term resource.
 */
@Path("/terms")
public class TermResourceImpl extends AbstractResource implements TermResource {

    private TermService termService;

    // TODO userDao must become userService when that is available.
    protected TermResourceImpl(UserDao userDao, TermService termService) {
        super(userDao);
        this.termService = termService;
    }

    @Override
    public List<String> getMatchingCollegesOrSchools(String query, int skip, int max) {
        logServiceCall();

        return termService.getMatchingCollegesOrSchools(preprocessQuery(query, skip, max, 0, 50), skip, max);
    }

    @Override
    public List<String> getMatchingDegrees(String query, int skip, int max) {
        logServiceCall();

        return termService.getMatchingDegrees(
            preprocessQuery(query, skip, max, 0, 50), skip, max
        );
    }

    @Override
    public List<String> getMatchingDisciplines(String query, int skip, int max) {
        logServiceCall();

        return termService.getMatchingDisciplines(
            preprocessQuery(query, skip, max, 0, 50), skip, max
        );
    }

}
