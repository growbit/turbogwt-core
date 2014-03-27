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

/**
 * Capable of submitting requests with type callbacks support.
 *
 * @param <T> Type of result from requests when appropriate.
 *
 * @author Danilo Reinert
 */
public interface RequestSender<T> {

    /*
    Request send(RequestBuilder.Method httpMethod, T data, AsyncCallback<T> callback);

    Request send(RequestBuilder.Method httpMethod, T data);

    Request send(RequestBuilder.Method httpMethod, AsyncCallback<T> callback);

    Request send(RequestBuilder.Method httpMethod);
    */

    Request get(AsyncCallback<T> callback);

    Request get();

    Request post(T data, AsyncCallback<T> callback);

    Request post(AsyncCallback<T> callback);

    Request post();

    Request put(T data, AsyncCallback<T> callback);

    Request put(AsyncCallback<T> callback);

    Request put();

    Request delete(AsyncCallback<T> callback);

    Request delete();

    Request head(AsyncCallback<T> callback);

    Request head();
}
