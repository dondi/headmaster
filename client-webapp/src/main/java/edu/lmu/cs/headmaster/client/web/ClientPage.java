package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientPage is the superclass for all logged-in Headmaster views.
 */
@AuthorizeInstantiation(Roles.USER)
public abstract class ClientPage extends WebPage {

    public ClientPage(final PageParameters pageParameters) {
        super(pageParameters);
    }

    /**
     * Relay call to the application. Mainly a shortcut, so wedon't have to code
     * out ((Headmaster)getApplication()).getServiceRoot() all the time.
     */
    protected String getServiceRoot() {
        return ((Headmaster)getApplication()).getServiceRoot();
    }

    /**
     * Convenience method for grabbing the logger.  This is not stored as an
     * instance variable because Wicket serializes web page classes and this
     * logger is not serializable.
     */
    protected Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

}
