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

package org.turbogwt.core.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

import java.util.HashSet;
import java.util.Set;

import org.turbogwt.core.js.collections.JsArraySet;
import org.turbogwt.core.js.collections.JsArrays;

/**
 * @author Danilo Reinert
 */
public class OverlaysTest extends GWTTestCase {

    static final class Person extends JavaScriptObject {

        protected Person() {
        }

        public Long getId() {
            return Overlays.boxPropertyAsLong(this, "id");
        }

        public String getName() {
            return Overlays.getPropertyAsString(this, "name");
        }

        public Integer getAge() {
            return Overlays.boxPropertyAsInteger(this, "age");
        }

        public Double getWeight() {
            return Overlays.boxPropertyAsDouble(this, "weight");
        }

        public Boolean isActive() {
            return Overlays.boxPropertyAsBoolean(this, "active");
        }

        public void setId(Long id) {
            Overlays.unboxValueToProperty(this, "id", id);
        }

        public void setName(String name) {
            Overlays.setValueToProperty(this, "name", name);
        }

        public void setAge(Integer age) {
            Overlays.unboxValueToProperty(this, "age", age);
        }

        public void setWeight(Double weight) {
            Overlays.unboxValueToProperty(this, "weight", weight);
        }

        public void setActive(Boolean active) {
            Overlays.unboxValueToProperty(this, "active", active);
        }
    }

    private Person person;

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.js.JsTest";
    }

    public void gwtSetUp() {
        person = (Person) JavaScriptObject.createObject();
        person.setId(123456789000L);
        person.setName("John Doe");
        person.setAge(30);
        person.setWeight(5.92);
        person.setActive(true);
    }

    public void testBoxingAndUnboxing() {
        assertEquals(new Long(123456789000L), person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals(new Integer(30), person.getAge());
        assertEquals(new Double(5.92), person.getWeight());
        assertEquals(Boolean.TRUE, person.isActive());
    }

    public void testPropertyNames() {
        Set<String> expected = new HashSet<>(5);
        expected.add("id");
        expected.add("name");
        expected.add("age");
        expected.add("weight");
        expected.add("active");

        final String[] properties = JsArrays.toArray(Overlays.getPropertyNames(person));
        Set<String> actual = new JsArraySet<>(properties);

        assertEquals(expected, actual);
    }
}
