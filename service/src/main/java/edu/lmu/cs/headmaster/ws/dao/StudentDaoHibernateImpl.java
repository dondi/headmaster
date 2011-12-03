package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.domain.Student;

/**
 * Hibernate implementation of the student dao.
 */
public class StudentDaoHibernateImpl extends HibernateDaoSupport implements StudentDao {

    @Override
    public Student getStudentById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Student> getStudents(String query, int skip, int max) {
        // TODO Auto-generated method stub
        return null;
    }

}
