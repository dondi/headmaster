package edu.lmu.cs.headmaster.ws.dao;

import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * Dao interface for user domain objects.
 */
public interface UserDao {

    /**
     * Returns the user with the given login name (password not included).
     */
    User getUserByLogin(String login);

    /**
     * Returns the user with the given id.
     */
    User getUserById(Long id);

    /**
     * Creates a new service user.
     */
    User createUser(User user);
    
    /**
     * Creates a new service user or updates an existing one.
     */
    User createOrUpdateUser(User user);

}
