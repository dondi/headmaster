package edu.lmu.cs.headmaster.client.web.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wicket.model.PropertyModel;

import edu.lmu.cs.headmaster.client.web.ClientPage;

/**
 * ServiceRelayUtils contains utility methods for communicating with the
 * configured service layer for this web application.
 */
public class ServiceRelayUtils {

    /**
     * The request methods supported by the utility methods in this class. The
     * enum encapsulates the code required for initializing an HttpUriRequest.
     */
    public static enum RequestMethod {
        GET {
            public HttpUriRequest getRequest(String uri) {
               return new HttpGet(uri);
            }
        },
        
        PUT {
            public HttpUriRequest getRequest(String uri) {
                return new HttpPut(uri);
             }
        },

        POST {
            public HttpUriRequest getRequest(String uri) {
                return new HttpPost(uri);
             } 
        },
        
        DELETE {    
            public HttpUriRequest getRequest(String uri) {
                return new HttpDelete(uri);
            }
        };
        
        public abstract HttpUriRequest getRequest(String uri);
    }

    /**
     * Creates a GET request object for the service layer, for a service URI
     * that does not have parameters nor need username substitution. This is
     * pretty much just a convenience method for the version below it.
     */
    public static HttpUriRequest createServiceLayerRequest(String serviceUri) {
        return createServiceLayerRequest(serviceUri, null, null, null);
    }

    /**
     * Creates a request object for the service layer, using the given parameters,
     * method, and username (which substitutes the {username} placeholder).
     * 
     * This method is actually little more than a generic HTTP request creation
     * method since the serviceUri is expected to be fully specified. It mainly
     * packages operations that would normally be repeated for a single service
     * call into a single method, such as appending the parameter list, doing
     * some string substitutions, etc.
     */
    public static HttpUriRequest createServiceLayerRequest(String serviceUri,
            List<NameValuePair> requestParameters, RequestMethod method, String username) {
        // Perform substitutions.
        StringBuilder uriBuilder = new
                StringBuilder(StringUtils.isBlank(username) ? serviceUri :
                        serviceUri.replaceAll("\\{username\\}", username));

        // Build the parameter list into the URI.
        uriBuilder.append(((requestParameters != null) && !requestParameters.isEmpty()) ?
                "?" + URLEncodedUtils.format(requestParameters, "UTF-8") : "");

        // All done.
        return ((method != null) ? method : RequestMethod.GET).getRequest(uriBuilder.toString());
    }

    /**
     * Sends the given request to the service layer, with the given ClientPage
     * as context. Convenience version.
     */
    public static String sendServiceLayerRequest(HttpUriRequest request, ClientPage page)
            throws ClientProtocolException, IOException {
        // The page supplies the credentials.
        return sendServiceLayerRequest(request,
                new PropertyModel<String>(page, "session.currentUsername").getObject(),
                new PropertyModel<String>(page, "session.currentPassword").getObject());
    }

    /**
     * Sends the given request to the service layer, and returns the response as
     * a string. Convenience version that does not require custom response
     * handling.
     */
    public static String sendServiceLayerRequest(HttpUriRequest request, String username,
            String password) throws ClientProtocolException, IOException {
        return sendServiceLayerRequest(request, username, password, new BasicResponseHandler());
    }

    /**
     * Sends the given request to the service layer, and returns the response as a string.
     */
    public static <T> T sendServiceLayerRequest(HttpUriRequest request, String username,
            String password, ResponseHandler<T> responseHandler) throws
                    ClientProtocolException, IOException {
        // Create and set up the HTTP client.
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
            new AuthScope(null, -1), // This is strictly between the web app and the
                                     // service, so no need to get any more specific.
            new UsernamePasswordCredentials(username, password)
        );

        // Issue the request, using the custom response handler if supplied.
        try {
            return httpClient.execute(request, responseHandler);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}
