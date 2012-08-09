package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class StudentViewPage extends ClientPage {

    public StudentViewPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested student ID to the rendered web page.
        add(new Label("student-id", new Model<Long>(pageParameters.getLong("id"))));
    }

}
