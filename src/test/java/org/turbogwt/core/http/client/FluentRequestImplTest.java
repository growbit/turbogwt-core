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
import com.google.gwt.core.client.JsArray;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.turbogwt.core.http.client.mock.ResponseMock;
import org.turbogwt.core.http.client.mock.ServerConnectionMock;
import org.turbogwt.core.http.client.model.Person;
import org.turbogwt.core.http.client.model.PersonJso;
import org.turbogwt.core.http.client.serialization.JsonObjectSerdes;
import org.turbogwt.core.js.client.Overlays;
import org.turbogwt.core.js.collections.client.JsArrayList;

/**
 * @author Danilo Reinert
 */
public class FluentRequestImplTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.http.HttpTest";
    }

    public void testVoidRequest() {
        final Requestory requestory = new Requestory();

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
        final Requestory requestory = new Requestory();

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
        final Requestory requestory = new Requestory();

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

    public void testOverlayRequest() {
        final Requestory requestory = new Requestory();

        final String uri = "/person-jso";

        final PersonJso person = PersonJso.create(1, "John Doe", 6.3, new Date(329356800));
        final String serializedResponse = "{ \"id\" : 1, \"name\":\"John Doe\",\"weight\" :6.3,  \"birthday\": 329356800}";

        ServerConnectionMock.responseFor(uri, ResponseMock.of(serializedResponse, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, PersonJso.class, new AsyncCallback<PersonJso>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(PersonJso result) {
                assertEquals(Overlays.stringify(person), Overlays.stringify(result));
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }

    public void testOverlayArrayRequest() {
        final Requestory requestory = new Requestory();

        final String uri = "/person-jso-array";

        final PersonJso p1 = PersonJso.create(1, "John Doe", 6.3, new Date(329356800));
        final PersonJso p2 = PersonJso.create(2, "Alice", 5.87, new Date(355343600));
        @SuppressWarnings("unchecked")
        final JsArray<PersonJso> persons = (JsArray<PersonJso>) JavaScriptObject.createArray();
        persons.push(p1);
        persons.push(p2);

        final String serializedResponse = "[{\"id\": 1, \"name\": \"John Doe\", \"weight\": 6.3, \"birthday\": 329356800}, {\"id\": 2, \"name\": \"Alice\", \"weight\": 5.87, \"birthday\": 355343600}]";

        ServerConnectionMock.responseFor(uri, ResponseMock.of(serializedResponse, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, PersonJso.class, new ListAsyncCallback<PersonJso>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<PersonJso> result) {
                JsArray<PersonJso> resultArray = ((JsArrayList<PersonJso>) result).asJsArray();
                assertEquals(Overlays.stringify(persons), Overlays.stringify(resultArray));
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }


    public void testCustomObjectRequest() {
        final Requestory requestory = new Requestory();
        requestory.registerSerdes(Person.class, new JsonObjectSerdes<Person>() {

            @Override
            public Person mapFromOverlay(JavaScriptObject overlay, Headers headers) {
                return new Person(Overlays.getPropertyAsInt(overlay, "id"),
                        Overlays.getPropertyAsString(overlay, "name"),
                        Overlays.getPropertyAsDouble(overlay, "weight"),
                        new Date((long) Overlays.getPropertyAsDouble(overlay, "birthday")));
            }

            @Override
            public String serialize(Person person, Headers headers) {
                return "{" + "\"id\":" + person.getId() + ",\"name\":\"" + person.getName() + "\"," +
                        "\"weight\":" + person.getWeight() + ",\"birthday\":" + person.getBirthday().getTime() + "}";
            }
        });

        final String uri = "/person";

        final Person person = new Person(1, "John Doe", 6.3, new Date(329356800));
        final String serializedResponse = "{\"id\":1, \"name\":\"John Doe\", \"weight\":6.3, \"birthday\":329356800}";

        ServerConnectionMock.responseFor(uri, ResponseMock.of(serializedResponse, 200, "OK"));

        final boolean[] callbackSuccessCalled = new boolean[1];

        requestory.get(uri, Person.class, new AsyncCallback<Person>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Person result) {
                assertEquals(person, result);
                callbackSuccessCalled[0] = true;
            }
        });

        assertTrue(callbackSuccessCalled[0]);
    }
}
