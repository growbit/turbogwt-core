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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Collection;

import org.turbogwt.core.http.client.serialization.Deserializer;
import org.turbogwt.core.http.client.serialization.JsonBooleanSerdes;
import org.turbogwt.core.http.client.serialization.JsonNumberSerdes;
import org.turbogwt.core.http.client.serialization.JsonStringSerdes;
import org.turbogwt.core.http.client.serialization.OverlaySerdes;
import org.turbogwt.core.http.client.serialization.Serdes;
import org.turbogwt.core.http.client.serialization.SerdesManager;
import org.turbogwt.core.http.client.serialization.Serializer;
import org.turbogwt.core.http.client.serialization.VoidSerdes;

/**
 * This class is a configurable {@link FluentRequest} factory.
 * Usually, you will use it as a singleton along your project.
 * <p/>
 * It provides a convenience API for building/executing HTTP Requests.
 *
 * @author Danilo Reinert
 */
public class Requestory {

    private final SerdesManager serdesManager = new SerdesManager();
    private MultipleParamStrategy defaultStrategy;

    public Requestory() {
        defaultStrategy = MultipleParamStrategy.REPEATED_PARAM;
        serdesManager.registerSerdes(String.class, JsonStringSerdes.getInstance());
        serdesManager.registerSerdes(Number.class, JsonNumberSerdes.getInstance());
        serdesManager.registerSerdes(Boolean.class, JsonBooleanSerdes.getInstance());
        serdesManager.registerSerdes(Void.class, VoidSerdes.getInstance());
        serdesManager.registerSerdes(JavaScriptObject.class, OverlaySerdes.getInstance());
    }

    //===================================================================
    // FluentRequest factory methods
    //===================================================================

    public <RequestType, ResponseType> FluentRequest<RequestType, ResponseType> request(Class<RequestType> requestType,
                                                                                     Class<ResponseType> responseType) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy);
    }

    public <RequestType, ResponseType> FluentRequest<RequestType, ResponseType> request(Class<RequestType> requestType,
                                                                                       Class<ResponseType> responseType,
                                                                                       MultipleParamStrategy strategy) {
        return createFluentRequestImpl(requestType, responseType, strategy);
    }

    public FluentRequest<Void, Void> request() {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy);
    }

    public FluentRequest<Void, Void> request(MultipleParamStrategy strategy) {
        return createFluentRequestImpl(Void.class, Void.class, strategy);
    }

    //===================================================================
    // Request aliases
    //===================================================================

    /* GET */
    public <ResponseType> Request get(String uri, Class<ResponseType> responseType,
                                      AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).get(callback);
    }

    public <ResponseType, C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>>
            Request get(String uri, Class<ResponseType> responseType, A callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).get(callback);
    }

    public Request get(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).get();
    }

    /* POST */
    public <RequestType, ResponseType> Request post(String uri, Class<RequestType> requestType, RequestType data,
                                                    Class<ResponseType> responseType,
                                                    AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri).post(data, callback);
    }

    public <RequestType, C extends Collection<RequestType>, ResponseType> Request post(String uri,
                                                                                       Class<RequestType> requestType,
                                                                                       C dataCollection,
                                                                                       Class<ResponseType> responseType,
                                                                                 AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri)
                .post(dataCollection, callback);
    }

    public <RequestType, ResponseType, C extends Collection<RequestType>, B extends Collection<ResponseType>,
            A extends CollectionAsyncCallback<B, ResponseType>> Request post(String uri,
                                                                             Class<RequestType> requestType,
                                                                             C dataCollection,
                                                                             Class<ResponseType> responseType,
                                                                             A callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri)
                .post(dataCollection, callback);
    }

    public <ResponseType, C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>>
            Request post(String uri, Class<ResponseType> responseType, A callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).post(callback);
    }

    public <ResponseType> Request post(String uri, Class<ResponseType> responseType,
                                       AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).post(callback);
    }

    public Request post(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).post();
    }

    /* PUT */
    public <RequestType, ResponseType> Request put(String uri, Class<RequestType> requestType, RequestType data,
                                                    Class<ResponseType> responseType,
                                                    AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri).put(data, callback);
    }

    public <RequestType, C extends Collection<RequestType>, ResponseType> Request put(String uri,
                                                                                      Class<RequestType> requestType,
                                                                                      C dataCollection,
                                                                                      Class<ResponseType> responseType,
                                                                                 AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri)
                .put(dataCollection, callback);
    }

    public <RequestType, ResponseType, C extends Collection<RequestType>, B extends Collection<ResponseType>,
            A extends CollectionAsyncCallback<B, ResponseType>> Request put(String uri,
                                                                            Class<RequestType> requestType,
                                                                            C dataCollection,
                                                                            Class<ResponseType> responseType,
                                                                            A callback) {
        return createFluentRequestImpl(requestType, responseType, defaultStrategy).setUri(uri)
                .put(dataCollection, callback);
    }

    public <ResponseType, C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>>
            Request put(String uri, Class<ResponseType> responseType, A callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).put(callback);
    }

    public <ResponseType> Request put(String uri, Class<ResponseType> responseType,
                                       AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).put(callback);
    }

    public Request put(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).put();
    }

    /* DELETE */
    public <ResponseType> Request delete(String uri, Class<ResponseType> responseType,
                                      AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).delete(callback);
    }

    public <ResponseType, C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>>
            Request delete(String uri, Class<ResponseType> responseType, A callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).delete(callback);
    }

    public Request delete(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).delete();
    }

    /* HEAD */
    public <ResponseType> Request head(String uri, Class<ResponseType> responseType,
                                      AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).head(callback);
    }

    public <ResponseType, C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>>
            Request head(String uri, Class<ResponseType> responseType, A callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).head(callback);
    }

    public Request head(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).head();
    }

    //===================================================================
    // Requestory configuration
    //===================================================================

    public MultipleParamStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    /**
     * Set the default strategy to separate params with multiple values.
     * You can use one of the constants provided at {@link MultipleParamStrategy} or implement a customized one.
     *
     * @param defaultStrategy   The {@link MultipleParamStrategy} to be initially set
     *                          in all {@link FluentRequest}s created.
     */
    public void setDefaultStrategy(MultipleParamStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    /**
     * Register a deserializer of the given type.
     *
     * @param type          The class of the deserializer's type.
     * @param deserializer  The deserializer of T.
     */
    public <T> void registerDeserializer(Class<T> type, Deserializer<T> deserializer) {
        serdesManager.registerDeserializer(type, deserializer);
    }

    /**
     * Register a serializer of the given type.
     *
     * @param type          The class of the serializer's type.
     * @param serializer  The serializer of T.
     */
    public <T> void registerSerializer(Class<T> type, Serializer<T> serializer) {
        serdesManager.registerSerializer(type, serializer);
    }

    /**
     * Register a serializer/deserializer of the given type.
     *
     * @param type      The class of the serializer/deserializer's type.
     * @param serdes    The serializer/deserializer of T.
     */
    public <T> void registerSerdes(Class<T> type, Serdes<T> serdes) {
        serdesManager.registerSerdes(type, serdes);
    }

    private <RequestType, ResponseType> FluentRequestImpl<RequestType, ResponseType>
        createFluentRequestImpl(Class<RequestType> requestType,
                                Class<ResponseType> responseType,
                                MultipleParamStrategy strategy) {
        final FluentRequestImpl<RequestType, ResponseType> request = new
                FluentRequestImpl<>(serdesManager, requestType, responseType);
        request.multipleParamStrategy(strategy);
        return request;
    }
}
