package edu.lmu.cs.headmaster.client.web;

import org.apache.wicket.authentication.panel.SignInPanel;

/**
 * LoginPanel customizes the default Wicket sign-in panel markup.
 */
public class LoginPanel extends SignInPanel {

    public LoginPanel(final String id) {
        super(id);
    }

    public LoginPanel(final String id, final boolean includeRememberMe) {
        super(id, includeRememberMe);
    }

}
