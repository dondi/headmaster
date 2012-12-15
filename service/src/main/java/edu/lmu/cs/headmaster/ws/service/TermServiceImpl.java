package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;

public class TermServiceImpl extends AbstractService implements TermService {

    private StudentDao studentDao;

    public TermServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<String> getMatchingCollegesOrSchools(String query, int skip, int max) {
        return studentDao.getMatchingCollegesOrSchools(query, skip, max);
    }

    @Override
    public List<String> getMatchingDegrees(String query, int skip, int max) {
        return studentDao.getMatchingDegrees(query, skip, max);
    }

    @Override
    public List<String> getMatchingDisciplines(String query, int skip, int max) {
        return studentDao.getMatchingDisciplines(query, skip, max);
    }

}
