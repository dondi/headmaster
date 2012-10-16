package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.dao.util.QueryBuilder;
import edu.lmu.cs.headmaster.ws.domain.Grant;

public class GrantDaoHibernateImpl extends HibernateDaoSupport implements GrantDao {

    // Search patterns
    private static final Pattern ALL_DIGITS = Pattern.compile("\\d+");
    private static final Pattern WORD = Pattern.compile("\\w+");
    
    @Override
    public Grant getGrantById(Long id) {
        return getHibernateTemplate().get(Grant.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Grant> getGrants(String query, Boolean awarded,
            Boolean presented, int skip, int max) {
        return createGrantQuery(query, awarded, presented)
                .build(getSession())
                .setFirstResult(skip)
                .setMaxResults(max)
                .list();
    }

    @Override
    public Grant createGrant(Grant grant) {
        getHibernateTemplate().save(grant);
        return grant;
    }

    @Override
    public void createOrUpdateGrants(Grant grant) {
        getHibernateTemplate().saveOrUpdate(grant);
    }

    /**
     * Returns a base HQL query object (no pagination) for the given parameters
     * for students.
     */
    private QueryBuilder createGrantQuery(String query, Boolean awarded,
            Boolean presented) {
        // The desired return order is id.
        QueryBuilder builder = new QueryBuilder(
            "from ResearchGrant g",
            "order by id"
        );

        if (query != null) {
            Matcher m = WORD.matcher(query);
            if (m.matches()) {
                builder.clause("lower(g.title) like lower(:title)", m.group(1) + "%");
            } else {
                m = ALL_DIGITS.matcher(query);
                if (m.matches()) {
                    builder.clause("g.id = :id", query);
                } else {
                    builder.clause("g.id like :id", query + "%");
                }
            }
        }

        if (awarded != null) {
            builder.clause("g.awarded = :awarded", awarded);
        }

        if (presented != null) {
            builder.clause("g.presented = :presented", presented);
        }

        // All done.
        return builder;
    }

}
