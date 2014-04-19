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

package org.turbogwt.core.http.books;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

import org.turbogwt.core.http.ContentTypeHeader;
import org.turbogwt.core.http.ListAsyncCallback;
import org.turbogwt.core.http.Requestor;
import org.turbogwt.core.http.mock.ResponseMock;
import org.turbogwt.core.http.mock.ServerStub;

/**
 * @author Danilo Reinert
 */
public class RestTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.http.HttpTest";
    }

    public void testCreate() {
        ServerStub.clearStub();

        final Requestor requestory = new Requestor();
        requestory.registerSerdes(Book.class, BookSerdes.getInstance());

        final String uri = "/server/books";

        ServerStub.responseFor(uri, ResponseMock.of(null, 200, "OK", new ContentTypeHeader("application/json")));

        final String expected = "{\"id\":1,\"title\":\"RESTful Web Services\",\"author\":\"Leonard Richardson\"}";

        final boolean[] callbacksCalled = new boolean[1];
        final Book data = new Book(1, "RESTful Web Services", "Leonard Richardson");

        requestory.request(Book.class, Void.class)
                .path("server").segment("books")
                .post(data, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // Ignored
                    }

                    @Override
                    public void onSuccess(Void result) {
                        callbacksCalled[0] = true;
                    }
                });

        assertTrue(callbacksCalled[0]);
        assertEquals(expected, ServerStub.getRequestData(uri).getData());
        assertEquals(RequestBuilder.POST, ServerStub.getRequestData(uri).getMethod());
    }

    public void testDelete() {
        ServerStub.clearStub();

        final Requestor requestory = new Requestor();

        final String uri = "/server/books/1";

        ServerStub.responseFor(uri, ResponseMock.of(null, 200, "OK", new ContentTypeHeader("application/json")));

        requestory.request() // The same as request(Void.class, Void.class)
                .path("server").segment("books").segment(1)
                .delete(); // You can optionally dismiss any server response

        assertEquals(RequestBuilder.DELETE, ServerStub.getRequestData(uri).getMethod());
    }

    public void testGetAll() {
        ServerStub.clearStub();

        final Requestor requestory = new Requestor();
        requestory.registerSerdes(Book.class, BookSerdes.getInstance());

        final String uri = "/server/books";

        final String responseText = "[{\"id\":1, \"title\":\"RESTful Web Services\", \"author\":\"Leonard Richardson\"}"
                + ", {\"id\":2, \"title\":\"Agile Software Development: Principles, Patterns, and Practices\", "
                + "\"author\":\"Robert C. Martin\"}]";
        ServerStub.responseFor(uri, ResponseMock.of(responseText, 200, "OK",
                new ContentTypeHeader("application/json")));

        final List<Book> expected = new ArrayList<>(2);
        expected.add(new Book(1, "RESTful Web Services", "Leonard Richardson"));
        expected.add(new Book(2, "Agile Software Development: Principles, Patterns, and Practices",
                "Robert C. Martin"));

        final boolean[] callbacksCalled = new boolean[1];

        requestory.request(Void.class, Book.class).path("server").segment("books").get(new ListAsyncCallback<Book>() {
            @Override
            public void onFailure(Throwable caught) {
                // Ignored
            }

            @Override
            public void onSuccess(List<Book> result) {
                callbacksCalled[0] = true;
                assertEquals(expected, result);
            }
        });

        assertTrue(callbacksCalled[0]);
        assertEquals(RequestBuilder.GET, ServerStub.getRequestData(uri).getMethod());
    }

    public void testGetOne() {
        ServerStub.clearStub();

        final Requestor requestory = new Requestor();
        requestory.registerSerdes(Book.class, BookSerdes.getInstance());

        final String uri = "/server/books/1";

        final String responseText = "{\"id\":1, \"title\":\"RESTful Web Services\", \"author\":\"Leonard Richardson\"}";
        ServerStub.responseFor(uri, ResponseMock.of(responseText, 200, "OK",
                new ContentTypeHeader("application/json")));

        final Book expected = new Book(1, "RESTful Web Services", "Leonard Richardson");

        final boolean[] callbacksCalled = new boolean[1];

        requestory.request(Void.class, Book.class)
                .path("server").segment("books").segment(1).get(new AsyncCallback<Book>() {
            @Override
            public void onFailure(Throwable caught) {
                // Ignored
            }

            @Override
            public void onSuccess(Book result) {
                callbacksCalled[0] = true;
                assertEquals(expected, result);
            }
        });

        assertTrue(callbacksCalled[0]);
        assertEquals(RequestBuilder.GET, ServerStub.getRequestData(uri).getMethod());
    }

    public void testUpdate() {
        ServerStub.clearStub();

        final Requestor requestory = new Requestor();
        requestory.registerSerdes(Book.class, BookSerdes.getInstance());

        final String uri = "/server/books/1";

        ServerStub.responseFor(uri, ResponseMock.of(null, 200, "OK", new ContentTypeHeader("application/json")));

        final String expected = "{\"id\":1,\"title\":\"RESTful Web Services\",\"author\":\"Leonard Richardson\"}";

        final boolean[] callbacksCalled = new boolean[1];
        final Book data = new Book(1, "RESTful Web Services", "Leonard Richardson");

        requestory.request(Book.class, Void.class)
                .path("server").segment("books").segment(1)
                .put(data, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // Ignored
                    }

                    @Override
                    public void onSuccess(Void result) {
                        callbacksCalled[0] = true;
                    }
                });

        assertTrue(callbacksCalled[0]);
        assertEquals(expected, ServerStub.getRequestData(uri).getData());
        assertEquals(RequestBuilder.PUT, ServerStub.getRequestData(uri).getMethod());
    }
}
