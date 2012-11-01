package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * A GrantWriterPage can serve as a superclass for any client page that defines
 * and writes grants to the web service. This puts the grant entry/editing user
 * interface plus grant writing code in one place, keeping it consistent across
 * multiple pages (as long as they are subclasses of GrantWriterPage).
 */
public abstract class GrantWriterPage extends ClientPage {

    public GrantWriterPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested student ID to the rendered web page.
        add(new Label(
            "grant-id",
            pageParameters.containsKey("id") ?
                new Model<Long>(pageParameters.getLong("id")) :
                new Model<String>() {
                    public String getObject() {
                        return "";
                    }
                }
        ));
    }

}
