package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * Hibernate-based implementation of the user dao.
 */
public class UserDaoHibernateImpl extends HibernateDaoSupport implements UserDao {

    @Override
    @SuppressWarnings("unchecked")
    public User getUserByLogin(String login) {
        List<User> result = getHibernateTemplate().find(
            "from User u left join fetch u.roles where u.login = ?", login
        );
        return ((result == null) || (result.size() == 0)) ? null : result.get(0);
    }

    @Override
    public User getUserById(Long id) {
        return getHibernateTemplate().get(User.class, id);
    }

    @Override
    public User createUser(User user) {
        // If a challengeRequest exists, change the user's challenge to it.  Otherwise,
        // we let the problem bubble up as an exception (because the challenge property
        // cannot be null).
        user.setChallenge(user.getChallengeRequest());

        getHibernateTemplate().save(user);
        return user;
    }

    @Override
    public User createOrUpdateUser(User user) {
        // If a challengeRequest exists, change the user's password to it.  Otherwise
        // keep the challenge the same.
        String currentChallenge = (String)(getHibernateTemplate()
                .find("select challenge from User where id = ?", user.getId()).get(0));

        user.setChallenge(user.getChallengeRequest() != null ?
                user.getChallengeRequest() : currentChallenge);

        // We must perform a merge instead of saveOrUpdate so that child collections will
        // "sync" correctly, including deletions.
        //
        // This is particularly important if the incoming User object is detached,
        // or created outside of Hibernate (e.g., delivered over the network).
        //
        // A useful blog entry for using saveOrUpdate vs. merge:
        //     http://www.stevideter.com/2008/12/07/saveorupdate-versus-merge-in-hibernate/
        //
        // In particular, look at Comment 10 for some discussion of why we should generally
        // default to saveOrUpdate until we realize that we need the extra work done by merge.
        return getHibernateTemplate().merge(user);
    }

}
