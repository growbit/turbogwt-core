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

package org.turbogwt.core.http.client.mock;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import java.util.HashMap;
import java.util.Map;

import org.turbogwt.core.http.client.Headers;
import org.turbogwt.core.http.client.ServerConnection;

/**
 * A mock of {@link ServerConnection}.
 *
 * You should add expected {@link Response}s to the underlying server stub with #responseFor
 * in order to mock responses from server.
 *
 * @author Danilo Reinert
 */
public class ServerConnectionMock implements ServerConnection {

    private static Map<String, Response> serverStub = new HashMap<>();
    private static Map<String, RequestMock> requestData = new HashMap<>();

    private boolean returnSuccess = true;

    public static void responseFor(String uri, Response response) {
        serverStub.put(uri, response);
    }

    public static RequestMock getRequestData(String uri) {
        return requestData.get(uri);
    }

    public static void clearStub() {
        serverStub.clear();
        requestData.clear();
    }

    private static RequestMock setRequestData(String uri, RequestMock requestMock) {
        return requestData.put(uri, requestMock);
    }

    public boolean isReturnSuccess() {
        return returnSuccess;
    }

    public void setReturnSuccess(boolean returnSuccess) {
        this.returnSuccess = returnSuccess;
    }

    @Override
    public void sendRequest(RequestBuilder.Method method, String url, String data, RequestCallback callback)
            throws RequestException {
        setRequestData(url, new RequestMock(method, url, data));
        if (returnSuccess) {
            callback.onResponseReceived(null, serverStub.get(url));
        } else {
            callback.onError(null, new RequestException("This is a mock exception."));
        }
    }

    @Override
    public void sendRequest(int timeout, String user, String password, Headers headers, RequestBuilder.Method method,
                            String url, String data, RequestCallback callback) throws RequestException {
        setRequestData(url, new RequestMock(method, url, data));
        if (returnSuccess) {
            callback.onResponseReceived(null, serverStub.get(url));
        } else {
            callback.onError(null, new RequestException("This is a mock exception."));
        }
    }
}
