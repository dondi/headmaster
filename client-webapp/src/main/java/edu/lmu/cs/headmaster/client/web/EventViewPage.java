package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class EventViewPage extends ClientPage {

    public EventViewPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested event ID to the rendered web page.
        add(new Label("event-id", new Model<Long>(pageParameters.getLong("id"))));
    }

}
