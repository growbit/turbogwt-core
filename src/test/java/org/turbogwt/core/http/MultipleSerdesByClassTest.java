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

package org.turbogwt.core.http;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

import org.turbogwt.core.http.books.Book;
import org.turbogwt.core.http.books.BookSerdes;
import org.turbogwt.core.http.books.BookXmlSerdes;
import org.turbogwt.core.http.mock.ResponseMock;
import org.turbogwt.core.http.mock.ServerStub;
import org.turbogwt.core.http.serialization.Serdes;
import org.turbogwt.core.http.serialization.SerializationException;

/**
 * @author Danilo Reinert
 */
public class MultipleSerdesByClassTest extends GWTTestCase {

    final String uri = "/book";

    final List<Book> bookList = new ArrayList<>(2);
    final Book firstBook = new Book(1, "RESTful Web Services", "Leonard Richardson");
    final Book secondBook = new Book(2, "Agile Software Development: Principles, Patterns and Practices",
            "Robert C. Martin");

    final String firstBookSerializedAsXml = "<book>" +
            "<id>1</id>" +
            "<title>RESTful Web Services</title>" +
            "<author>Leonard Richardson</author>" +
            "</book>";
    final String secondBookSerializedAsXml = "<book>" +
            "<id>2</id>" +
            "<title>Agile Software Development: Principles, Patterns and Practices</title>" +
            "<author>Robert C. Martin</author>" +
            "</book>";

    final String bookArraySerializedAsXml = "<books type\"array\">" +
            firstBookSerializedAsXml + secondBookSerializedAsXml +
            "</books>";

    final String firstBookSerializedAsJson = "{\"id\":1, \"title\":\"RESTful Web Services\", \"author\":\"Leonard " +
            "Richardson\"}";

    final String secondBookSerializedAsJson = "{\"id\":2, \"title\":\"Agile Software Development: Principles, " +
            "Patterns and Practices\", \"author\":\"Robert C. Martin\"}";

    final String bookArraySerializedAsJson = "[" + firstBookSerializedAsJson + "," + secondBookSerializedAsJson + "]";

    final Serdes<Book> jsonSerdes = new BookSerdes();
    final Serdes<Book> xmlSerdes = new BookXmlSerdes();


    @Override
    public String getModuleName() {
        return "org.turbogwt.core.http.HttpTest";
    }

    @Override
    public void gwtSetUp() throws Exception {
        bookList.add(firstBook);
        bookList.add(secondBook);
    }

    public void testXmlMatching() {
        prepareStub("application/xml", firstBookSerializedAsXml);
        final Requestor requestory = getRequestory();

        final boolean[] callbackCalled = new boolean[3];

        try {
            requestory.request(Void.class, Book.class).path(uri).get(new AsyncCallback<Book>() {
                @Override
                public void onFailure(Throwable caught) {
                    callbackCalled[0] = true;
                }

                @Override
                public void onSuccess(Book result) {
                    callbackCalled[1] = true;
                    assertEquals(firstBook, result);
                }
            });
        } catch (SerializationException e) {
            // This piece of code should not be called
            callbackCalled[2] = true;
        }

        assertFalse(callbackCalled[0]);
        assertTrue(callbackCalled[1]);
        assertFalse(callbackCalled[2]);
    }

    public void testJsonMatching() {
        prepareStub("application/json", firstBookSerializedAsJson);
        final Requestor requestory = getRequestory();

        final boolean[] callbackCalled = new boolean[3];

        try {
            requestory.request(Void.class, Book.class).path(uri).get(new AsyncCallback<Book>() {
                @Override
                public void onFailure(Throwable caught) {
                    callbackCalled[0] = true;
                }

                @Override
                public void onSuccess(Book result) {
                    callbackCalled[1] = true;
                    assertEquals(firstBook, result);
                }
            });
        } catch (SerializationException e) {
            // This piece of code should not be called
            callbackCalled[2] = true;
        }

        assertFalse(callbackCalled[0]);
        assertTrue(callbackCalled[1]);
        assertFalse(callbackCalled[2]);
    }

    private Requestor getRequestory() {
        final Requestor requestory = new Requestor();
        requestory.registerSerdes(Book.class, jsonSerdes);
        requestory.registerSerdes(Book.class, xmlSerdes);
        return requestory;
    }

    private void prepareStub(String responseContentType, String serializedResponse) {
        ServerStub.clearStub();
        ServerStub.responseFor(uri, ResponseMock.of(serializedResponse, 200, "OK",
                new ContentTypeHeader(responseContentType)));
    }
}
