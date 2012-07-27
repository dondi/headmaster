package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;

/**
 * Corresponding Java class for the main menu page.
 */
@AuthorizeInstantiation(Roles.USER)
public class MainMenuPage extends WebPage {

    public MainMenuPage(final PageParameters pageParameters) {
        super(pageParameters);
    }

}
