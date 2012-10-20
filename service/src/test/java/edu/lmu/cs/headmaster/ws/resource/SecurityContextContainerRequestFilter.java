package edu.lmu.cs.headmaster.ws.resource;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.apache.http.auth.BasicUserPrincipal;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * A test-only container request filter for inserting a test security context into
 * a request.
 */
public class SecurityContextContainerRequestFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        request.setSecurityContext(createSecurityContext());
        return request;
    }

    /**
     * Helper, override-able method for creating a security context.
     */
    protected SecurityContext createSecurityContext() {
        return createSimpleSecurityContext("testuser", true, true, null);
    }

    /**
     * Parameterized helper method for different kinds of [simple] security
     * contexts.
     */
    protected SecurityContext createSimpleSecurityContext(final String username,
            final boolean userInRole, final boolean secure, final String authenticationScheme) {
        return new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return new BasicUserPrincipal(username);
            }

            @Override
            public boolean isUserInRole(String role) {
                // This user has every role.
                return userInRole;
            }

            @Override
            public boolean isSecure() {
                return secure;
            }

            @Override
            public String getAuthenticationScheme() {
                return authenticationScheme;
            }
            
        };
    }
}
