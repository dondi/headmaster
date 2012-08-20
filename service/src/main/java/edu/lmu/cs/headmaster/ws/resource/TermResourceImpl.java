package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import javax.ws.rs.Path;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;
import edu.lmu.cs.headmaster.ws.dao.UserDao;

/**
 * The sole implementation of the term resource.
 */
@Path("/terms")
public class TermResourceImpl extends AbstractResource implements TermResource {

    private StudentDao studentDao;

    protected TermResourceImpl(UserDao userDao, StudentDao studentDao) {
        super(userDao);
        this.studentDao = studentDao;
    }

    @Override
    public List<String> getMatchingCollegesOrSchools(String query, int skip, int max) {
        logServiceCall();

        return studentDao.getMatchingCollegesOrSchools(
            preprocessQuery(query, skip, max, 0, 50), skip, max
        );
    }

    @Override
    public List<String> getMatchingDegrees(String query, int skip, int max) {
        logServiceCall();

        return studentDao.getMatchingDegrees(
            preprocessQuery(query, skip, max, 0, 50), skip, max
        );
    }

    @Override
    public List<String> getMatchingDisciplines(String query, int skip, int max) {
        logServiceCall();

        return studentDao.getMatchingDisciplines(
            preprocessQuery(query, skip, max, 0, 50), skip, max
        );
    }

}
