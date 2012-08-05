package edu.lmu.cs.headmaster.client.web;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.codehaus.jackson.map.ObjectMapper;
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
        // We authenticate by accessing the user's roles.
        DefaultHttpClient httpClient = new DefaultHttpClient();

        String serviceUri = ((Headmaster)getApplication()).getServiceRoot() +
            "users/login/" + username;
        getLogger().info("Authenticating with serviceUri: [" + serviceUri + "]");

        // Supply credentials.
        httpClient.getCredentialsProvider().setCredentials(
            new AuthScope(null, -1), // This is strictly between the web app and the service, so
                                     // no need to get any more specific.
            new UsernamePasswordCredentials(username, password)
        );

        // Create the request.  We want JSON.
        HttpGet httpGet = new HttpGet(serviceUri);
        httpGet.setHeader("Accept", "application/json");

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            String response = httpClient.execute(httpGet, responseHandler);
            
            // The response is JSON; parse it.
            getLogger().debug("got response " + response);
            ServiceUser serviceUser = new ObjectMapper().readValue(response, ServiceUser.class);
            getLogger().debug("found user " + serviceUser.login);
            getLogger().debug("has roles " + serviceUser.roles);
            currentRoles = new Roles();

            // Include a catch-all for anyone with credentials.
            currentRoles.add(Roles.USER);
            for (ServiceRole role: serviceUser.roles) {
                currentRoles.add(role.role);
                getLogger().debug("User [" + username + "] has role: [" + role + "]");
            }

            // Authentication worked.
            return true;
        } catch(ClientProtocolException cpexc) {
            getLogger().error(cpexc.getMessage(), cpexc);
            return false;
        } catch(IOException ioexc) {
            getLogger().error(ioexc.getMessage(), ioexc);
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
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

    /**
     * Helper classes for parsing user data.  For internal use only.
     */
    static class ServiceUser {
        public long id;
        public boolean active;
        public String email;
        public String login;
        public Collection<ServiceRole> roles;
    }

    static class ServiceRole {
        public long id;
        public String role;
    }

    private Roles currentRoles;

}
