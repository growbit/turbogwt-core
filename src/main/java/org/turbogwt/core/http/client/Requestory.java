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

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.turbogwt.core.http.client.serialization.Deserializer;
import org.turbogwt.core.http.client.serialization.Serdes;
import org.turbogwt.core.http.client.serialization.SerdesManager;
import org.turbogwt.core.http.client.serialization.Serializer;

/**
 * This class is configurable {@link FluentRequest} factory.
 * Usually, you will use it as a singleton along your project.
 *
 * @author Danilo Reinert
 */
public class Requestory {

    private final SerdesManager serdesManager = new SerdesManager();
    private MultipleParamStrategy defaultStrategy;

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

    public Request get(String uri) {
        return createFluentRequestImpl(Void.class, Void.class, defaultStrategy).setUri(uri).get();
    }

    public <ResponseType> Request get(String uri, Class<ResponseType> responseType,
                                      AsyncCallback<ResponseType> callback) {
        return createFluentRequestImpl(Void.class, responseType, defaultStrategy).setUri(uri).get(callback);
    }

    /*
    @Override
    public <T, R> Request post(T data, AsyncCallback<R> callback) {
        return null;
    }

    @Override
    public Request post(AsyncCallback<Object> callback) {
        return null;
    }

    @Override
    public Request post() {
        return null;
    }

    @Override
    public Request put(Object data, AsyncCallback<Object> callback) {
        return null;
    }

    @Override
    public Request put(AsyncCallback<Object> callback) {
        return null;
    }

    @Override
    public Request put() {
        return null;
    }

    @Override
    public Request delete(AsyncCallback<Object> callback) {
        return null;
    }

    @Override
    public Request delete() {
        return null;
    }

    @Override
    public Request head(AsyncCallback<Object> callback) {
        return null;
    }

    @Override
    public Request head() {
        return null;
    }
    */

    public MultipleParamStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    public void setDefaultStrategy(MultipleParamStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    public <T> void registerDeserializer(Class<T> type, Deserializer<T> deserializer) {
        serdesManager.registerDeserializer(type, deserializer);
    }

    public <T> void registerSerializer(Class<T> type, Serializer<T> serializer) {
        serdesManager.registerSerializer(type, serializer);
    }

    public <T> void registerSerdes(Class<T> type, Serdes<T> serdes) {
        serdesManager.registerSerdes(type, serdes);
    }

    private <RequestType, ResponseType> FluentRequestImpl<RequestType, ResponseType> createFluentRequestImpl
            (Class<RequestType> requestType, Class<ResponseType> responseType, MultipleParamStrategy strategy) {
        final FluentRequestImpl<RequestType, ResponseType> request = new
                FluentRequestImpl<>(serdesManager, requestType, responseType);
        request.multipleParamStrategy(strategy);
        return request;
    }
}
