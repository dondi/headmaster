package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;

/**
 * A StudentListPage can serve as a superclass for any client page that displays
 * a list of students queried from the web service. The page provides an element
 * for which a jQuery data property named "query" is expected to be set. This
 * JavaScript object will be used directly as the parameters for a GET /students
 * Ajax call.
 */
public abstract class StudentListPage extends ClientPage {

    public StudentListPage(final PageParameters pageParameters) {
        super(pageParameters);
    }

}
