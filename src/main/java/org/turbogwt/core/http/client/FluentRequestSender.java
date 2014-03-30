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

/**
 * This class provides fluent style request building and sending capabilities.
 *
 * @param <RequestType> Type of data to be sent in the HTTP request body, when appropriate.
 * @param <ResponseType> Type of result from requests, when appropriate.
 *
 * @author Danilo Reinert
 */
public interface FluentRequestSender<RequestType, ResponseType> extends FluentRequest<RequestType, ResponseType>,
        RequestSender<RequestType, ResponseType> {

    /**
     * Set the strategy for appending parameters with multiple values.
     *
     * @param strategy the strategy.
     *
     * @return the updated UriBuilder
     *
     * @throws IllegalArgumentException if strategy is null
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> multipleParamStrategy(MultipleParamStrategy strategy)
            throws IllegalArgumentException;
    
    /**
     * Set the URI scheme.
     *
     * @param scheme the URI scheme. A null value will unset the URI scheme.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if scheme is invalid
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> scheme(String scheme) throws IllegalArgumentException;

    /**
     * Set the URI host.
     *
     * @param host the URI host. A null value will unset the host component of the URI.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if host is invalid.
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> host(String host) throws IllegalArgumentException;

    /**
     * Set the URI port.
     *
     * @param port the URI port, a negative value will unset an explicit port.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if port is invalid
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> port(int port) throws IllegalArgumentException;

    /**
     * Set the URI path. This method will overwrite any existing path and associated matrix parameters. Existing '/'
     * characters are preserved thus a single value can represent multiple URI path segments.
     *
     * @param path the path. A null value will unset the path component of the URI.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> path(String path);

    /**
     * Append path segments to the existing path. When constructing the final path, a '/' separator will be inserted
     * between the existing path and the first path segment if necessary and each supplied segment will also be
     * separated by '/'. Existing '/' characters are encoded thus a single value can only represent a single URI path
     * segment.
     *
     * @param segments the path segment values
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if segments or any element of segments is null
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> segment(String... segments) throws IllegalArgumentException;

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
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if name or values is null
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> matrixParam(String name, Object... values)
            throws IllegalArgumentException;

    /**
     * Append a query parameter to the existing set of query parameters. If multiple values are supplied the parameter
     * will be added once per value.
     *
     * @param name   the query parameter name
     * @param values the query parameter value(s), each object will be converted to a {@code String} using its {@code
     *               toString()} method.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     *
     * @throws IllegalArgumentException if name or values is null
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> queryParam(String name, Object... values)
            throws IllegalArgumentException;

    /**
     * Set the URI fragment.
     *
     * @param fragment the URI fragment. A null value will remove any existing fragment.
     *
     * @return the updated AdvancedFluentRequest<RequestType, ResponseType>
     */
    @Override
    FluentRequestSender<RequestType, ResponseType> fragment(String fragment);

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
    FluentRequestSender<RequestType, ResponseType> header(String header, String value);

    /**
     * Sets the user name that will be used in the request URL.
     *
     * @param user user name to use
     *
     * @throws IllegalArgumentException if the user is empty
     * @throws NullPointerException if the user is null
     */
    FluentRequestSender<RequestType, ResponseType> user(String user);

    /**
     * Sets the password to use in the request URL. This is ignored if there is no
     * user specified.
     *
     * @param password password to use in the request URL
     *
     * @throws IllegalArgumentException if the password is empty
     * @throws NullPointerException if the password is null
     */
    FluentRequestSender<RequestType, ResponseType> password(String password);

    /**
     * Sets the number of milliseconds to wait for a request to complete. Should
     * the request timeout, the
     * {@link com.google.gwt.http.client.RequestCallback#onError(com.google.gwt.http.client.Request, Throwable)}
     * method will be called on the callback instance given to the
     * {@link com.google.gwt.http.client.RequestBuilder#sendRequest(String, com.google.gwt.http.client.RequestCallback)}
     * method. The callback method will receive an instance of the
     * {@link com.google.gwt.http.client.RequestTimeoutException} class as its
     * {@link Throwable} argument.
     *
     * @param timeoutMillis number of milliseconds to wait before canceling the
     *          request, a value of zero disables timeouts
     *
     * @throws IllegalArgumentException if the timeout value is negative
     */
    FluentRequestSender<RequestType, ResponseType> timeout(int timeoutMillis);

    /**
     * Set a callback to handle specific HTTP status code response.
     * <p/>
     * The informed code can represent a group of codes, e.g. 4 will handle any code in [400,499].
     * Similarly, 20 will handle any code in [200,209].
     * <p/>
     * The codes have priority for specificity, e.g. 201 has a higher priority than 20,
     * which has a higher priority than 2.
     *
     * @param statusCode    the unit, dozen or hundred expected on response's status code.
     * @param callback      the callback to handle informed code
     */
    FluentRequestSender<RequestType, ResponseType> on(int statusCode, SingleCallback callback);

    /**
     * Deserialize result to T.
     *
     * @param type The class from T.
     * @param <T> The type to be deserialized.
     * @return The new FluentRequest capable of deserializing T.
     * @throws IllegalArgumentException if no Deserializer is registered for type T.
     */
    <T> FluentRequestSender<RequestType, T> deserializeAs(Class<T> type) throws IllegalArgumentException;

    /**
     * Serialize request data from T.
     *
     * @param type The class from T.
     * @param <T> The type to be serialized.
     * @return The new FluentRequest capable of serializing T.
     * @throws IllegalArgumentException if no Serializer is registered for type T.
     */
    <T> FluentRequestSender<T, ResponseType> serializeAs(Class<T> type) throws IllegalArgumentException;

    /**
     * Serialize and Deserialize transmitting data from/to T.
     *
     * @param type The class from T.
     * @param <T> The type to be de/serialized.
     * @return The new FluentRequest capable of de/serializing T.
     * @throws IllegalArgumentException if no Deserializer or Serializer is registered for type T.
     */
    <T> FluentRequestSender<T, T> serializeDeserializeAs(Class<T> type) throws IllegalArgumentException;
}