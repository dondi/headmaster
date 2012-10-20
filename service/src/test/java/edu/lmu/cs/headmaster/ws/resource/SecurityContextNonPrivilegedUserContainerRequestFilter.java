package edu.lmu.cs.headmaster.ws.resource;

import javax.ws.rs.core.SecurityContext;

/**
 * A test-only container request filter for inserting a test security context into
 * a request.  This version simulates a non-privileged user.
 */
public class SecurityContextNonPrivilegedUserContainerRequestFilter
        extends SecurityContextContainerRequestFilter {

    @Override
    protected SecurityContext createSecurityContext() {
        return createSimpleSecurityContext("testuser", false, true, null);
    }

}
