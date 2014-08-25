package edu.lmu.cs.headmaster.client.web;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.javascript.DefaultJavascriptCompressor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.target.coding.MixedParamUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Application object for the web application.
 */
public class Headmaster extends AuthenticatedWebApplication {

    private String serviceRoot;
    private boolean wicketDeployment;

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getWebSessionClass()
     */
    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return HeadmasterSession.class;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getSignInPageClass()
     */
    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return MainMenuPage.class;
    }

    /**
     * Returns the SpringComponentInjector to use.  Separated out so that it can
     * be overridden for unit tests.
     */
    protected SpringComponentInjector createSpringComponentInjector() {
        return new SpringComponentInjector(this);
    }

    /**
     * Convenience call for grabbing the service root.  At the very least,
     * it ensures that it ends with a slash.
     */
    public String getServiceRoot() {
        return StringUtils.isNotBlank(serviceRoot) ?
            (serviceRoot.endsWith("/") ? serviceRoot : serviceRoot + "/") :
            "http://localhost:8080/"; // Common case: service is on the same host as web app.
    }

    /**
     * For Spring injection.
     */
    public void setServiceRoot(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#getConfigurationType()
     */
    @Override
    public String getConfigurationType() {
        // This is DEVELOPMENT or DEPLOYMENT.
        return wicketDeployment ? DEPLOYMENT : DEVELOPMENT;
    }

    /**
     * For Spring injection.
     */
    public void setWicketDeployment(Boolean wicketDeployment) {
        this.wicketDeployment = wicketDeployment;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        addComponentInstantiationListener(createSpringComponentInjector());
        
        // Some settings depend on deployment type...
        getDebugSettings().setDevelopmentUtilitiesEnabled(DEVELOPMENT.equals(getConfigurationType()));
        if (DEPLOYMENT.equals(getConfigurationType())) {
            getResourceSettings().setJavascriptCompressor(new DefaultJavascriptCompressor());
        }

        // ...while others are unconditional.
        getMarkupSettings().setStripWicketTags(true);
    
        // URL customization.
        mountBookmarkablePage("login", LoginPage.class);
        mountBookmarkablePage("logout", LogoutPage.class);
        mountBookmarkablePage("relay", ServiceRelayPage.class);

        mountBookmarkablePage("events/list", EventListPage.class);
        mountBookmarkablePage("events/new", EventCreationPage.class);
        mountBookmarkablePage("events/search", EventSearchPage.class);

        mountBookmarkablePage("students/new", StudentCreationPage.class);
        mountBookmarkablePage("students/search", StudentSearchPage.class);

        mountBookmarkablePage("students/freshmen", FreshmenPage.class);
        mountBookmarkablePage("students/sophomores", SophomoresPage.class);
        mountBookmarkablePage("students/juniors", JuniorsPage.class);
        mountBookmarkablePage("students/seniors", SeniorsPage.class);

        mountBookmarkablePage("courses/new", CourseCreationPage.class);
        mountBookmarkablePage("courses/search", CourseSearchPage.class);

        mountBookmarkablePage("grants/list", ProposalListPage.class);
        
        // More URL customization, now for URLs with parameters.
        // Special handling: individual referral page is parameterized.
        MixedParamUrlCodingStrategy studentViewUrls = new MixedParamUrlCodingStrategy(
            "students",
            StudentViewPage.class,
            new String[] { "id" }
        );
        mount(studentViewUrls);

        MixedParamUrlCodingStrategy studentEditorUrls = new MixedParamUrlCodingStrategy(
            "students/edit",
            StudentEditorPage.class,
            new String[] { "id" }
        );
        mount(studentEditorUrls);

        MixedParamUrlCodingStrategy eventViewUrls = new MixedParamUrlCodingStrategy(
            "events",
            EventViewPage.class,
            new String[] { "id" }
        );
        mount(eventViewUrls);

        MixedParamUrlCodingStrategy eventEditorUrls = new MixedParamUrlCodingStrategy(
            "events/edit",
            EventEditorPage.class,
            new String[] { "id" }
        );
        mount(eventEditorUrls);
    }

}
