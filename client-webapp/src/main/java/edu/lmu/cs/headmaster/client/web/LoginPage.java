package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebResponse;

public class LoginPage extends WebPage {

    /**
     * @param parameters
     */
    public LoginPage(final PageParameters parameters) {
        add(new LoginPanel("loginPanel"));
    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        
        // Supply an explicit header indicating signed-in state.
        response.setHeader("X-Headmaster-Sign-In-Required", "yes");
    }

}
