package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * An EventWriterPage can serve as a superclass for any client page that defines
 * and writes events to the web service. This puts the event entry/editing user
 * interface plus event writing code in one place, keeping it consistent across
 * multiple pages (as long as they are subclasses of EventWriterPage).
 */
public abstract class EventWriterPage extends ClientPage {

    public EventWriterPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested event ID to the rendered web page.
        add(new Label("event-id", new Model<Long>(pageParameters.getLong("id"))));
   }

}
