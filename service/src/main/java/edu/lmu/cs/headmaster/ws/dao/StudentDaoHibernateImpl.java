package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.dao.util.QueryBuilder;
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
    public List<Student> getStudents(String query, Boolean active,
            Integer expectedGraduationYearFrom, Integer expectedGraduationYearTo,
            int skip, int max) {
        return createStudentQuery(query, active,
                expectedGraduationYearFrom, expectedGraduationYearTo)
            .build(getSession())
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
    @SuppressWarnings("unchecked")
    public List<String> getMatchingCollegesOrSchools(String query) {
        return (List<String>)getHibernateTemplate()
            .find(
                "select distinct m.collegeOrSchool from Major m where lower(m.collegeOrSchool) like lower(?) order by m.collegeOrSchool",
                "%" + query + "%"
            );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getMatchingDegrees(String query) {
        return (List<String>)getHibernateTemplate()
            .find(
                "select distinct m.degree from Major m where lower(m.degree) like lower(?) order by m.degree",
                "%" + query + "%"
            );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getMatchingDisciplines(String query) {
        // HQL does not do unions, so we drop to SQL here.
        final String wildcard = "%" + query + "%";
        return (List<String>)getSession()
            .createSQLQuery("select distinct discipline from major where lower(discipline) like lower(:query1) union select distinct minors as discipline from student_minors where lower(minors) like lower(:query2) order by discipline")
            .setString("query1", wildcard)
            .setString("query2", wildcard)
            .list();
    }

    @Override
    public Student createStudent(Student student) {
        getHibernateTemplate().save(student);
        return student;
    }

    @Override
    public void createOrUpdateStudent(Student student) {
        // We do a merge to ensure orphan deletion.
        getHibernateTemplate().merge(student);
    }

    /**
     * Returns a base HQL query object (no pagination) for the given parameters
     * for students.
     */
    private QueryBuilder createStudentQuery(String query, Boolean active,
            Integer expectedGraduationYearFrom, Integer expectedGraduationYearTo) {
        // The desired return order is lastName, firstName.
        QueryBuilder builder = new QueryBuilder(
            "from Student s",
            "order by lower(s.lastName), lower(s.firstName)"
        );

        if (query != null) {
            Matcher m = WORD_COMMA_WORD.matcher(query);
            if (m.matches()) {
                builder.clause("lower(s.lastName) like lower(:lastName)", m.group(1) + "%");
                builder.clause("lower(s.firstName) like lower(:firstName)", m.group(2) + "%");
            } else {
                m = ALL_DIGITS.matcher(query);
                if (m.matches()) {
                    builder.clause("s.schoolId = :schoolId", query);
                } else {
                    builder.clause("lower(s.lastName) like lower(:lastName)", query + "%");
                }
            }
        }

        if (active != null) {
            builder.clause("s.active = :active", active);
        }

        if (expectedGraduationYearFrom != null) {
            builder.clause("s.expectedGraduationYear >= :gradYearFrom", expectedGraduationYearFrom);
        }

        if (expectedGraduationYearTo != null) {
            builder.clause("s.expectedGraduationYear <= :gradYearTo", expectedGraduationYearTo);
        }

        // All done.
        return builder;
    }

}
