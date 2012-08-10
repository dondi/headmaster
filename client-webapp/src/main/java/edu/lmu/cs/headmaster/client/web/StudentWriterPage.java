package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * A StudentWriterPage can serve as a superclass for any client page that defines
 * and writes students to the web service. This puts the student entry/editing user
 * interface plus student writing code in one place, keeping it consistent across
 * multiple pages (as long as they are subclasses of StudentWriterPage).
 */
public abstract class StudentWriterPage extends ClientPage {

    public StudentWriterPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested student ID to the rendered web page.
        add(new Label(
            "student-id",
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
