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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Arrays;
import java.util.List;

import org.turbogwt.core.http.client.mock.ResponseMock;
import org.turbogwt.core.http.client.mock.ServerConnectionMock;

/**
 * @author Danilo Reinert
 */
public class FluentRequestImplTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.http.HttpTest";
    }

    public void testVoidRequest() {
        final  Requestory requestory = new Requestory();

        final String uri = "/void";
        ServerConnectionMock.responseFor(uri, ResponseMock.of(null, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, Void.class, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
                assertNull(result);
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }

    public void testStringRequest() {
        final  Requestory requestory = new Requestory();

        final String uri = "/string";
        final String response = "Some string response";
        final String serializedResponse = "\"Some string response\"";
        ServerConnectionMock.responseFor(uri, ResponseMock.of(serializedResponse, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, String.class, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                assertEquals(response, result);
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }

    public void testStringArrayRequest() {
        final  Requestory requestory = new Requestory();

        final String uri = "/string-array";
        final String[] response = {"Some", "string", "array", "response"};
        final String irregularStringArray = " [ \"Some\", \"string\" ,  \"array\", \"response\" ]  ";
        ServerConnectionMock.responseFor(uri, ResponseMock.of(irregularStringArray, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, String.class, new ListAsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<String> result) {
                assertTrue(Arrays.equals(result.toArray(), response));
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }
}
