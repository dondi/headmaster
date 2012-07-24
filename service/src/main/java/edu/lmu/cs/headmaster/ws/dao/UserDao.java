package edu.lmu.cs.headmaster.ws.dao;

import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * Dao interface for user domain objects.
 */
public interface UserDao {

    /**
     * Creates a new service user.
     */
    public User createUser(User user);
    
    /**
     * Creates a new service user or updates an existing one.
     */
    public User createOrUpdateUser(User user);

}
