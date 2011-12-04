package edu.lmu.cs.headmaster.ws.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.lmu.cs.headmaster.ws.util.ServiceException;

/**
 * A base class for the services, supplying error keys, a logger, a validation method, and fields
 * for JAX-RS context objects.
 */
public class AbstractService {

    // Error keys.  No service returns user-displayable text, since that is the responsibility
    // of a client.  Instead, the services provide mnemonic error keys.  We've purposely
    // refrained from using a properties file (they're not key-value pairs), a separate enum
    // (we'd need long references or static imports), or injection (they're not variables,
    // but constants).
    protected static final String SKIP_TOO_SMALL = "skip.too.small";
    protected static final String MAX_TOO_LARGE = "max.too.large";
    protected static final String QUERY_REQUIRED = "query.required";
    protected static final String MALFORMED_ARGUMENT_DATE = "argument.date.malformed";
    protected static final String MISSING_ARGUMENT_DATE = "argument.date.missing";
    protected static final String UNSUPPORTED_ENCODING = "encoding.not.supported";
    
    protected static final String STUDENT_OVERSPECIFIED = "student.overspecified";
    protected static final String STUDENT_INCONSISTENT = "student.inconsistent";
    protected static final String STUDENT_NOT_FOUND = "student.not.found";

    protected Logger logger = Logger.getLogger(getClass());

    // URI information automatically injected by JAX-RS framework on each service call.
    @Context
    protected UriInfo uriInfo;

    // Security context automatically injected by JAX-RS framework on each service call.
    @Context
    protected SecurityContext securityContext;

    // A dao for security checking.
    // TODO not yet ready
//    protected UserDao userDao;

    // Every service needs a user dao.
    // TODO not yet ready
//    protected AbstractService(UserDao userDao) {
//        this.userDao = userDao;
//    }

    /**
     * Checks that a condition is true and throws a <code>ServiceException</code> with the given
     * integer HTTP response code if it is not.  Example:
     * <pre>
     *     validate(student != null, 404, NO_STUDENT);
     * </pre>
     */
    protected void validate(boolean condition, int httpStatus, String errorKey) {
        if (!condition) {
            throw new ServiceException(httpStatus, errorKey);
        }
    }

    /**
     * Convenience method that takes a <code>Response.Status</code> instead of an int.
     */
    protected void validate(boolean condition, Response.Status httpStatus, String errorKey) {
        validate(condition, httpStatus.getStatusCode(), errorKey);
    }

    /**
     * Utility method for checking a paginated request's parameters for validity.
     */
    protected void validatePagination(int skip, int max, int minimumSkip, int maximumSkip) {
        validate(skip >= minimumSkip, 400, SKIP_TOO_SMALL);
        validate(max <= maximumSkip, 400, MAX_TOO_LARGE);
    }

    /**
     * Utility method for checking an input interval's validity.
     */
    protected Interval validateInterval(String startDate, String endDate) {
        validate(startDate != null, 400, MISSING_ARGUMENT_DATE);
        validate(endDate != null, 400, MISSING_ARGUMENT_DATE);

        try {
            return new Interval(new DateTime(URLDecoder.decode(startDate, "UTF-8")),
                new DateTime(URLDecoder.decode(endDate, "UTF-8")));
        } catch(IllegalArgumentException iae) {
            throw new ServiceException(400, MALFORMED_ARGUMENT_DATE);
        } catch(UnsupportedEncodingException uee) {
            throw new ServiceException(500, UNSUPPORTED_ENCODING);
        }
    }

    /**
     * Logs the currently accessed uri.  While this data can also be found in the web server's
     * logs, it can be useful in providing some context for debugging when reading the regular
     * application logs.
     */
    protected void logServiceCall() {
        if (logger.isDebugEnabled()) {
            logger.debug("Invoking " + uriInfo.getAbsolutePath());
        }
    }

    /**
     * Returns a list of all the current user's roles.  Since a user should have at least one
     * role, this method will throw an exception if it detects the current user has no roles.
     */
    // TODO Not yet ready
//    protected List<UserRoleData> getRolesForCurrentUser() {
//
//        // First get all the patient roles.
//        String loginName = securityContext.getUserPrincipal().getName();
//        List<UserRoleData> roles = userDao.getRoleDataByLoginName(loginName);
//
//        if (roles == null || roles.isEmpty()) {
//            logger.error("A user has logged in without any roles");
//            throw new ServiceException(500, INVALID_USER);
//        }
//
//        logger.debug(loginName + " has " + roles.size() + " roles.");
//
//        return roles;
//    }

    /**
     * Checks if the principal user in the security context has admin or superuser user roles.
     */
    // TODO Not yet ready
//    protected void validateAdminCredentials() {
//        logger.debug("Checking for admin credentials");
//        for (UserRoleData roleData : getRolesForCurrentUser()) {
//            if (roleData.isManager() || roleData.isSuperuser()) {
//                return;
//            }
//        }
//
//        // If we get here, we have a surefire rejection.
//        validate(false, Response.Status.FORBIDDEN, USER_FORBIDDEN);
//    }

    /**
     * Preprocesses a query for a URI by trimming, urldecoding, and validating that the skip and
     * max parameters make sense.
     *
     * @return the processed query.
     * @throws a ServiceException resulting in a HTTP 400 if the query parameter is missing or a
     *     ServiceException resulting in an HTTP 500 if the JVM for any reason doesn't understand
     *     the encoding scheme used to decoding the URL.
     */
    protected String preprocessQuery(String q, int skip, int max, int minimumSkip, int maximumSkip) {
        try {
            validate(q != null, 400, QUERY_REQUIRED);
            String query = StringUtils.trimToNull(URLDecoder.decode(q, "UTF-8"));
            validatePagination(skip, max, minimumSkip, maximumSkip);
            return query;
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(500, UNSUPPORTED_ENCODING);
        }
    }

    /**
     * Returns a datetime object for a string in a null-safe way.
     *
     * @return null if the input is null, or else the datetime object for the string produced
     *     by Joda Time.
     * @throws ServiceException to generate an HTTP 400 with the message for a malformed date
     *     argument if Joda Time cannot convert the input string.
     */
    protected DateTime toDateTime(String dateString) {
        try {
            return dateString == null ? null : new DateTime(dateString);
        } catch (IllegalArgumentException iae) {
            throw new ServiceException(400, MALFORMED_ARGUMENT_DATE);
        }
    }
}
