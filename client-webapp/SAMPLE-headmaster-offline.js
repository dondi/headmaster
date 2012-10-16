/*
 * Sample file that sets up "offline" overrides for the Headmaster object, when opening .html
 * files directly (i.e., outside a web app container).  This code should *not* be used in the
 * deployed version; for that, the client page superclass supplies the Headmaster object for
 * pages served up by a web app container.
 * 
 * See src/main/resources/edu/lmu/cs/headmaster/client/web/ClientPage.html for instructions on
 * what to do with this file.
 */
window.Headmaster = $.extend(window.Headmaster || {}, {

    serviceUri: function (serviceCall) {
        // Modify the URL prefix below so that it is whatever host/root path
        // that is running the web service.
        return "http://localhost:8080/headmaster/" + serviceCall;
    }

});
