/*
 * Copyright 2014 Grow Bit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.turbogwt.core.http.client;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.turbogwt.core.js.client.Overlays;
import org.turbogwt.core.js.collections.client.JsMap;

/**
 * Default implementation of fluent request.
 *
 * @param <RequestType> Type of data to be sent in the HTTP request body, when appropriate.
 * @param <ResponseType> Type of result from requests, when appropriate.
 *
 * @author Danilo Reinert
 */
public class FluentRequestImpl<RequestType, ResponseType> implements AdvancedFluentRequest<RequestType, ResponseType> {

    private UriBuilder uriBuilder;
    private String uri;
    private JsMap<SingleCallback> mappedCallbacks;
    private AsyncCallback<ResponseType> resultCallback;
    private Serializer<RequestType> requestSerdes;
    private Serdes<ResponseType> responseSerdes;
    private Headers headers;
    private String user;
    private String password;
    private int timeout;

    public FluentRequestImpl(MultipleParamStrategy strategy, Serializer<RequestType> requestSerdes,
                             Serdes<ResponseType> responseSerdes) throws NullPointerException {
        this(requestSerdes, responseSerdes);
        this.requestSerdes = requestSerdes;
        this.uriBuilder.multipleParamStrategy(strategy);
    }

    public FluentRequestImpl(Serializer<RequestType> requestSerdes, Serdes<ResponseType> responseSerdes)
            throws NullPointerException {
        if (requestSerdes == null) throw new NullPointerException("Request serializer cannot be null.");
        if (responseSerdes == null) throw new NullPointerException("Reponse serializer cannot be null.");
        this.responseSerdes = responseSerdes;
        this.uriBuilder = new UriBuilderImpl();
    }

    /**
     * Set the URI scheme.
     *
     * @param scheme the URI scheme. A null value will unset the URI scheme.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if scheme is invalid
     */
    public AdvancedFluentRequest<RequestType, ResponseType> scheme(String scheme) throws IllegalArgumentException {
        uriBuilder.scheme(scheme);
        return this;
    }

    /**
     * Set the URI host.
     *
     * @param host the URI host. A null value will unset the host component of the URI.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if host is invalid.
     */
    public AdvancedFluentRequest<RequestType, ResponseType> host(String host) throws IllegalArgumentException {
        uriBuilder.host(host);
        return this;
    }

    /**
     * Set the URI port.
     *
     * @param port the URI port, a negative value will unset an explicit port.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if port is invalid
     */
    public AdvancedFluentRequest<RequestType, ResponseType> port(int port) throws IllegalArgumentException {
        uriBuilder.port(port);
        return this;
    }

    /**
     * Set the URI path. This method will overwrite any existing path and associated matrix parameters. Existing '/'
     * characters are preserved thus a single value can represent multiple URI path segments.
     *
     * @param path the path. A null value will unset the path component of the URI.
     *
     * @return the updated UriBuilder
     */
    public AdvancedFluentRequest<RequestType, ResponseType> path(String path) {
        uriBuilder.path(path);
        return this;
    }

    /**
     * Append path segments to the existing path. When constructing the final path, a '/' separator will be inserted
     * between the existing path and the first path segment if necessary and each supplied segment will also be
     * separated by '/'. Existing '/' characters are encoded thus a single value can only represent a single URI path
     * segment.
     *
     * @param segments the path segment values
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if segments or any element of segments is null
     */
    public AdvancedFluentRequest<RequestType, ResponseType> segment(String... segments) throws IllegalArgumentException {
        uriBuilder.segment(segments);
        return this;
    }

    /**
     * Append a matrix parameter to the existing set of matrix parameters of the current final segment of the URI path.
     * If multiple values are supplied the parameter will be added once per value. Note that the matrix parameters are
     * tied to a particular path segment; subsequent addition of path segments will not affect their position in the URI
     * path.
     *
     * @param name   the matrix parameter name
     * @param values the matrix parameter value(s), each object will be converted to a {@code String} using its {@code
     *               toString()} method.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if name or values is null
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    public AdvancedFluentRequest<RequestType, ResponseType> matrixParam(String name, Object... values) throws IllegalArgumentException {
        uriBuilder.matrixParam(name, values);
        return this;
    }

    /**
     * Append a query parameter to the existing set of query parameters. If multiple values are supplied the parameter
     * will be added once per value.
     *
     * @param name   the query parameter name
     * @param values the query parameter value(s), each object will be converted to a {@code String} using its {@code
     *               toString()} method.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if name or values is null
     */
    public AdvancedFluentRequest<RequestType, ResponseType> queryParam(String name, Object... values) throws IllegalArgumentException {
        uriBuilder.queryParam(name, values);
        return this;
    }

    /**
     * Set the URI fragment.
     *
     * @param fragment the URI fragment. A null value will remove any existing fragment.
     *
     * @return the updated UriBuilder
     */
    public AdvancedFluentRequest<RequestType, ResponseType> fragment(String fragment) {
        uriBuilder.fragment(fragment);
        return this;
    }

    /*
    **
     * Se the responseSerializer for handling request/response body.
     *
     * @param responseSerializer the responseSerializer of ResponseType.
     *
    public FluentRequest responseSerializer(Serializer<ResponseType> responseSerializer) {
        this.responseSerializer = responseSerializer;
        return this;
    }
    */

    /**
     * Sets a request header with the given name and value. If a header with the
     * specified name has already been set then the new value overwrites the
     * current value.
     *
     * @param header the name of the header
     * @param value the value of the header
     *
     * @throws NullPointerException if header or value are null
     * @throws IllegalArgumentException if header or value are the empty string
     */
    public AdvancedFluentRequest<RequestType, ResponseType> header(String header, String value) {
        if (headers == null) headers = Headers.create();
        headers.set(header, value);
        return this;
    }

    /**
     * Directly set URI for request.
     * It will override any uri in construction.
     *
     * @param uri The URI for requesting.
     */
    protected AdvancedFluentRequest<RequestType, ResponseType> setUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Sets the user name that will be used in the request URL.
     *
     * @param user user name to use
     *
     * @throws IllegalArgumentException if the user is empty
     * @throws NullPointerException if the user is null
     */
    public AdvancedFluentRequest<RequestType, ResponseType> user(String user) {
        this.user = user;
        return this;
    }

    /**
     * Sets the password to use in the request URL. This is ignored if there is no
     * user specified.
     *
     * @param password password to use in the request URL
     *
     * @throws IllegalArgumentException if the password is empty
     * @throws NullPointerException if the password is null
     */
    public AdvancedFluentRequest<RequestType, ResponseType> password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Sets the number of milliseconds to wait for a request to complete. Should
     * the request timeout, the
     * {@link com.google.gwt.http.client.RequestCallback#onError(Request, Throwable)}
     * method will be called on the callback instance given to the
     * {@link com.google.gwt.http.client.RequestBuilder#sendRequest(String, RequestCallback)}
     * method. The callback method will receive an instance of the
     * {@link com.google.gwt.http.client.RequestTimeoutException} class as its
     * {@link Throwable} argument.
     *
     * @param timeoutMillis number of milliseconds to wait before canceling the
     *          request, a value of zero disables timeouts
     *
     * @throws IllegalArgumentException if the timeout value is negative
     */
    public AdvancedFluentRequest<RequestType, ResponseType> timeout(int timeoutMillis) {
        this.timeout = timeoutMillis;
        return this;
    }

    /**
     * Set a callback to handle specific HTTP status code response.
     * The informed code can represent a group of codes, e.g. 4 will handle any code in [400,499].
     * Similarly, 20 will handle any code in [200,209].
     *
     * @param statusCode    the unit, dozen or hundred expected on response's status code.
     * @param callback      the callback to handle informed code
     */
    public AdvancedFluentRequest<RequestType, ResponseType> on(int statusCode, SingleCallback callback) {
        if (mappedCallbacks == null) mappedCallbacks = JsMap.create();
        mappedCallbacks.set(String.valueOf(statusCode), callback);
        return this;
    }

    @Override
    public Request get(AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.GET, null, callback);
    }

    @Override
    public Request get() {
        return send(RequestBuilder.GET, null, null);
    }

    @Override
    public Request post(RequestType data, AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.POST, data, callback);
    }

    @Override
    public Request post(AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.POST, null, callback);
    }

    @Override
    public Request post() {
        return send(RequestBuilder.POST, null, null);
    }

    @Override
    public Request put(RequestType data, AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.PUT, data, callback);
    }

    @Override
    public Request put(AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.PUT, null, callback);
    }

    @Override
    public Request put() {
        return send(RequestBuilder.PUT, null, null);
    }

    @Override
    public Request delete(AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.DELETE, null, callback);
    }

    @Override
    public Request delete() {
        return send(RequestBuilder.DELETE, null, null);
    }

    @Override
    public Request head(AsyncCallback<ResponseType> callback) {
        return send(RequestBuilder.HEAD, null, callback);
    }

    @Override
    public Request head() {
        return send(RequestBuilder.HEAD, null, null);
    }

    /**
     * Build request and send it.
     * If the request could not be sent then the returned {@link Request} is null and resultCallback#onError is called.
     *
     * @param data              The data to be serialized and sent into the request body.
     * @param resultCallback    The user callback.
     */
    private Request send(RequestBuilder.Method method, RequestType data, AsyncCallback<ResponseType> resultCallback) {
        this.resultCallback = resultCallback;

        // Prepare callback for following request builder
        RequestCallback callback = new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                final String code = String.valueOf(response.getStatusCode());

                // Mapped response status
                if (mappedCallbacks != null) {
                    JsArrayString codes = Overlays.getOwnPropertyNames(mappedCallbacks, true);
                    codes = reverse(codes);
                    for (int i = 0; i < codes.length(); i++) {
                        String mappedCode = codes.get(i);
                        if (code.startsWith(mappedCode)) {
                            SingleCallback singleCallback = mappedCallbacks.get(mappedCode);
                            singleCallback.onResponseReceived(request, response);
                            return;
                        }
                    }
                }

                // Successful response
                if (code.startsWith("2")) {
                    ResponseType result = null;
                    final String body = response.getText();
                    if (body != null && !body.isEmpty()) {
                        // Serializer init was verified on construction
                        Headers responseHeaders = Headers.create();
                        final Header[] responseHeaderArray = response.getHeaders();
                        for (Header header : responseHeaderArray) {
                            responseHeaders.set(header.getName(), header.getValue());
                        }
                        result = responseSerdes.deserialize(body, responseHeaders);
                    }
                    getResultCallback().onSuccess(result);
                    return;
                }

                // Unsuccessful response
                getResultCallback().onFailure(new UnsuccessfulResponseException(request, response));
            }

            @Override
            public void onError(Request request, Throwable exception) {
                if (hasResultCallback()) getResultCallback().onFailure(exception);
            }
        };

        if (uri == null) uri = uriBuilder.build();
        RequestBuilder requestBuilder = new RequestBuilder(method, uri);
        uri = null; // Avoid caching problems
        requestBuilder.setCallback(callback);

        // Handle request parameters
        if (timeout > 0) requestBuilder.setTimeoutMillis(timeout);
        if (user != null) requestBuilder.setUser(user);
        if (password != null) requestBuilder.setPassword(password);
        if (user != null && password != null) requestBuilder.setIncludeCredentials(true);
        if (headers != null) {
            JsArrayString names = Overlays.getOwnPropertyNames(headers);
            for (int i = 0; i < names.length(); i++) {
                String header = names.get(i);
                requestBuilder.setHeader(header, headers.get(header));
            }
        }

        // Handle request body
        String body = null;
        if (data != null) {
            // Serializer init was verified on construction
            body = requestSerdes.serialize(data, Overlays.deepCopy(headers));
        }
        requestBuilder.setRequestData(body);

        try {
            return requestBuilder.send();
        } catch (RequestException e) {
            getResultCallback().onFailure(e);
        }

        return null;
    }

    private boolean hasResultCallback() {
        return resultCallback != null;
    }

    private AsyncCallback<ResponseType> getResultCallback() {
        return resultCallback;
    }

    private static native JsArrayString reverse(JsArrayString array) /*-{
        return array.reverse();
    }-*/;
}
