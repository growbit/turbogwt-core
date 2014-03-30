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

import java.util.Collection;

/**
 * Capable of submitting requests with type callbacks support.
 *
 * @param <RequestType> Type of data to be sent in the HTTP request body, when appropriate.
 * @param <ResponseType> Type of result from requests, when appropriate.
 *
 * @author Danilo Reinert
 */
public interface RequestSender<RequestType, ResponseType> {

    Request get(AsyncCallback<ResponseType> callback);

    <C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>> Request get(A callback);

    Request get();

    Request post(RequestType data, AsyncCallback<ResponseType> callback);

    <C extends Collection<RequestType>> Request post(C data, AsyncCallback<ResponseType> callback);

    <C extends Collection<RequestType>, B extends Collection<ResponseType>,
            A extends CollectionAsyncCallback<B, ResponseType>> Request post(C data, A callback);

    Request post(AsyncCallback<ResponseType> callback);

    <C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>> Request post(A callback);

    Request post();

    Request put(RequestType data, AsyncCallback<ResponseType> callback);

    <C extends Collection<RequestType>> Request put(C data, AsyncCallback<ResponseType> callback);

    <C extends Collection<RequestType>, B extends Collection<ResponseType>,
            A extends CollectionAsyncCallback<B, ResponseType>> Request put(C data, A callback);

    Request put(AsyncCallback<ResponseType> callback);

    <C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>> Request put(A callback);

    Request put();

    Request delete(AsyncCallback<ResponseType> callback);

    <C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>> Request delete(A callback);

    Request delete();

    Request head(AsyncCallback<ResponseType> callback);

    <C extends Collection<ResponseType>, A extends CollectionAsyncCallback<C, ResponseType>> Request head(A callback);

    Request head();
}
