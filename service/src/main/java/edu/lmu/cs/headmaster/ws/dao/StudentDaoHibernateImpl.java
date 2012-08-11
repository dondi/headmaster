package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;

/**
 * Hibernate implementation of the student dao.
 */
public class StudentDaoHibernateImpl extends HibernateDaoSupport implements StudentDao {

    // Search patterns
    private static final Pattern ALL_DIGITS = Pattern.compile("\\d+");
    private static final Pattern WORD_COMMA_WORD = Pattern.compile("(\\w+)\\s*,\\s*(\\w*)");

    @Override
    public Student getStudentById(Long id) {
        return getHibernateTemplate().get(Student.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> getStudents(String query, int skip, int max) {
        return createBaseStudentQuery(query)
                .setFirstResult(skip)
                .setMaxResults(max)
                .list();
    }

    @Override
    public List<Event> getStudentAttendanceById(Long id) {
        Student student = (Student)getSession()
                .createQuery("from Student s left join fetch s.attendance where s.id = :id")
                .setParameter("id", id)
                .uniqueResult();

        return (student == null) ? null : student.getAttendance();
    }

    @Override
    public Student createStudent(Student student) {
        getHibernateTemplate().save(student);
        return student;
    }

    @Override
    public void createOrUpdateStudent(Student student) {
        getHibernateTemplate().saveOrUpdate(student);
    }

    /**
     * Returns a base HQL query object (no pagination) for the free-form text search value
     * for students.
     */
    private Query createBaseStudentQuery(String query) {
        // The desired return order.
        final String orderBy = " order by lower(s.lastName), lower(s.firstName)";

        Matcher m = WORD_COMMA_WORD.matcher(query);
        if (m.matches()) {
            return getLastNameFirstNameQuery(m.group(1), m.group(2), orderBy);
        }

        m = ALL_DIGITS.matcher(query);
        if (m.matches()) {
            return getDigitStringQuery(query, orderBy);
        }

        // The query is not in any specific form, so do the default
        return getDefaultQuery(query, orderBy);
    }

    /**
     * Returns the query for finding students given leading characters for the last name, leading
     * characters for the first name, and an ordering criteria.
     */
    private Query getLastNameFirstNameQuery(String last, String first, String orderBy) {
        return getSession()
                .createQuery("from Student s where lower(s.lastName) like lower(?) and " +
                        "lower(s.firstName) like lower(?) " +
                        orderBy)
                .setParameter(0, last + "%")
                .setParameter(1, first + "%");
    }

    /**
     * Returns a query for finding student given a digit string.  The digit string will always
     * be searched as a school id.
     */
    private Query getDigitStringQuery(String query, String orderBy) {
        return getSession()
                .createQuery("from Student s where s.schoolId = ? " + orderBy)
                .setParameter(0, query);
    }

    /**
     * Return a query for finding students by a prefix of last name only; this is the "default"
     * query to run when the input doesn't have any particular known format.
     */
    private Query getDefaultQuery(String query, String orderBy) {
        return getSession()
                .createQuery("from Student s where lower(s.lastName) like lower(?) " + orderBy)
                .setParameter(0, query + "%");
    }
}
