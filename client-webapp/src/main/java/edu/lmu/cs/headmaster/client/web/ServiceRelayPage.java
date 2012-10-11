package edu.lmu.cs.headmaster.client.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;

import edu.lmu.cs.headmaster.client.web.util.ServiceRelayUtils;
import edu.lmu.cs.headmaster.client.web.util.ServiceRelayUtils.RequestMethod;

/**
 * ServiceRelayPage passes client web app service calls to the true web service.
 * This allows Headmaster to run its web service and web client on different
 * hosts, without violating the same origin policy.
 */
public class ServiceRelayPage extends ClientPage {

    public ServiceRelayPage(final PageParameters pageParameters) {
        super(pageParameters);
        serviceTail = pageParameters.getString("s");

        getRequestCycle().setRequestTarget(new IRequestTarget() {

            @Override
            public void detach(RequestCycle requestCycle) {
                // Nothing to do here.
            }

            @Override
            public void respond(RequestCycle requestCycle) {
                // Put together the URI.
                String serviceUri = getServiceRoot() + getServiceTail();
                
                // Supply credentials. As a Wicket page derivative we use the
                // property model approach.
                String username = new PropertyModel<String>(ServiceRelayPage.this,
                        "session.currentUsername").getObject();
                String password = new PropertyModel<String>(ServiceRelayPage.this,
                        "session.currentPassword").getObject();

                // Relay any additional parameters.
                List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
                for (String parameterName: pageParameters.keySet()) {
                    if (!"s".equals(parameterName)) {
                        requestParameters.add(new BasicNameValuePair(parameterName,
                            pageParameters.getString(parameterName)));
                    }
                }

                // Build the request based on the method.
                HttpUriRequest request = createRelayRequest(serviceUri, requestParameters,
                        getWebRequestCycle().getWebRequest().getHttpServletRequest(), username);

                getLogger().info("Using service URI: [" + request.getURI() + "]");

                // Issue the request.
                try {
                    requestCycle.getResponse().write(new ByteArrayInputStream(
                        ServiceRelayUtils.sendServiceLayerRequest(request, username, password,
                            new ServiceResponseHandler(getWebRequestCycle().getWebResponse()
                        )
                    )));
                } catch(ClientProtocolException cpexc) {
                    getLogger().error(cpexc.getMessage(), cpexc);
                    getSession().error(cpexc.getMessage());
                    throw new RestartResponseException(ServiceErrorPage.class);
                } catch(IOException ioexc) {
                    getLogger().error(ioexc.getMessage(), ioexc);
                    error(ioexc.getMessage());
                }

            }
        });
    }

    /**
     * Convenience method for getting the service tail: drop a leading slash
     * if necessary (since the service root will have the trailing slash).
     */
    private String getServiceTail() {
        // Simple encoding: ditch spaces.
        return (serviceTail.startsWith("/") ?
                serviceTail.substring(1) : serviceTail).replaceAll(" ", "%20");
    }

    /**
     * Factory method for http requests.
     */
    @SuppressWarnings("unchecked")
    private HttpUriRequest createRelayRequest(String serviceUri,
            List<NameValuePair> requestParameters, HttpServletRequest originalRequest,
            String username) {
        // Build the initial request.
        RequestMethod requestMethod = RequestMethod.valueOf(originalRequest.getMethod());
        HttpUriRequest result = ServiceRelayUtils.createServiceLayerRequest(serviceUri,
                requestParameters, requestMethod, username);

        // For PUT or POST, copy the entity (this consumes the original one). We
        // trust ServiceRelayUtils.createServiceLayerRequest to have created an
        // appropriate request based on the given request method.
        if (requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT) {
            result = copyEntity((HttpEntityEnclosingRequestBase)result, originalRequest);
        };

        // Relay original request headers except for Content-Length.
        Enumeration<String> headerNames = (Enumeration<String>)originalRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!"Content-Length".equalsIgnoreCase(headerName)) {
                Enumeration<String> headerValues = originalRequest.getHeaders(headerName);
                while (headerValues.hasMoreElements()) {
                    String headerValue = headerValues.nextElement();
                    result.setHeader(headerName, headerValue);
                    getLogger().debug("Setting header " + headerName + "=" + headerValue);
                }
            }
        }

        return result;
    }

    /**
     * Helper method for copying the original request's body/entity into the
     * given request. The original request's entity is read entirely into a byte
     * array so we are not dependent on the life cycle of its input stream.
     */
    private HttpEntityEnclosingRequestBase copyEntity(HttpEntityEnclosingRequestBase request,
            HttpServletRequest originalRequest) {
        try {
            request.setEntity(new ByteArrayEntity(EntityUtils.toByteArray(
                    new InputStreamEntity(originalRequest.getInputStream(),
                            originalRequest.getContentLength()))));
        } catch(IOException ioexc) {
            // Shouldn't happen, but we log anyway.
            getLogger().error("Could not copy entity", ioexc);
        }

        return request;
    }
    
    /**
     * Helper class that passes on certain service response headers to the relayed response.
     */
    private class ServiceResponseHandler implements ResponseHandler<byte[]> {

        private WebResponse webResponse;

        public ServiceResponseHandler(WebResponse webResponse) {
            this.webResponse = webResponse;
        }

        public byte[] handleResponse(final HttpResponse response)
            throws HttpResponseException, IOException {
            // Relay most response headers.  The omitted ones either have been known to break Ajax
            // calls for some reason, or are already emitted by Wicket automatically. 
            relayHeadersExcept(response, "server", "transfer-encoding");
            
            // Relay the response status.
            webResponse.getHttpServletResponse().setStatus(
                response.getStatusLine().getStatusCode()
            );

            // Write out the entity.
            HttpEntity httpEntity = response.getEntity();
            return (httpEntity != null) ? EntityUtils.toByteArray(httpEntity) : new byte[0];
        }

        private void relayHeadersExcept(final HttpResponse response, String... headerNames) {
            for (Header header: response.getAllHeaders()) {
                boolean includeHeader = true;
                for (String headerToExclude: headerNames) {
                    if (headerToExclude.equalsIgnoreCase(header.getName())) {
                        includeHeader = false;
                        break;
                    }
                }

                if (includeHeader) {
                    if ("content-length".equalsIgnoreCase(header.getName())) {
                        webResponse.setContentLength(Long.parseLong(header.getValue()));
                    } else {
                        webResponse.setHeader(header.getName(), header.getValue());
                    }
                }
            }
        }

    }

    private String serviceTail;

}
