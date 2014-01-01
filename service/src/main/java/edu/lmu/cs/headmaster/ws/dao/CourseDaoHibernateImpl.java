package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.dao.util.QueryBuilder;
import edu.lmu.cs.headmaster.ws.domain.Course;

public class CourseDaoHibernateImpl extends HibernateDaoSupport implements CourseDao {

    @Override
    public Course getCourseById(Long id) {
        return getHibernateTemplate().get(Course.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> getCourses(String query, int skip, int max) {
        return createCourseQuery(query)
                .build(getSession())
                .setFirstResult(skip)
                .setMaxResults(max)
                .list();
    }

    @Override
    public Course createCourse(Course course) {
        getHibernateTemplate().save(course);
        return course;
    }

    @Override
    public void createOrUpdateCourse(Course course) {
        getHibernateTemplate().saveOrUpdate(course);
    }

    /**
     * Returns a base HQL query object (no pagination) for the given parameters
     * for grants.
     */
    private QueryBuilder createCourseQuery(String query) {
        // The desired return order is year, term, subject, number, section
        // (i.e., chronological, then by subject/number/section).
        QueryBuilder builder = new QueryBuilder(
            "select c from Course c",
            "order by year, term, subject, number, section"
        );

        if (query != null) {
            builder.clause("lower(c.subject) like lower(:query) or lower(c.title) like lower(:query) or " +
                "lower(c.number) like lower(:query) or lower(c.section) like lower(:query) or " +
                "lower(c.subject + ' ' + c.number) like lower(:query)", "%" + query + "%");
        }

        // All done.
        return builder;
    }

}
