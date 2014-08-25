package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * A CourseWriterPage can serve as a superclass for any client page that defines
 * and writes courses to the web service. This puts the course entry/editing user
 * interface plus course writing code in one place, keeping it consistent across
 * multiple pages (as long as they are subclasses of CourseWriterPage).
 */
public abstract class CourseWriterPage extends ClientPage {

    public CourseWriterPage(final PageParameters pageParameters) {
        super(pageParameters);

        // Relay the requested course ID to the rendered web page.
        add(new Label(
            "course-id",
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
