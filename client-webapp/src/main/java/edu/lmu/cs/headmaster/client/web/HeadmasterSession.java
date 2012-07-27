package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session class for the Headmaster client.
 */
public class HeadmasterSession extends AuthenticatedWebSession {

    public HeadmasterSession(Request request) {
        super(request);
        clearUserData();
    }

    @Override
    public boolean authenticate(String username, String password) {
        // Stub pending implementation of service.
        currentRoles = new Roles();
        currentRoles.add(Roles.USER);
        return true;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles() {
        return isSignedIn() ? currentRoles : null;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#signOut()
     */
    @Override
    public void signOut() {
        super.signOut();
        clearUserData();
    }

    /**
     * Convenience method for grabbing the logger.  This is not stored as an
     * instance variable because Wicket serializes web page classes and this
     * logger is not serializable.
     */
    protected Logger getLogger() {
        return LoggerFactory.getLogger(HeadmasterSession.class);
    }

    /**
     * Convenience method for clearing out current user information.
     */
    private void clearUserData() {
        currentRoles = null;
    }

    private Roles currentRoles;

}
