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

package org.turbogwt.core.js.collections;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author Danilo Reinert
 */
public class JsMapTest extends GWTTestCase {

    private JsMap<Integer> list;

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.js.JsTest";
    }

    public void gwtSetUp() {
        list = JsMap.create();
        for (int i = 0; i < 3; i++) {
            list.set("" + i, i);
        }
    }

    public void testInsert() {
        assertEquals(list.size(), 3);
        list.set("4", 4);
        assertEquals(list.size(), 4);
        assertEquals(list.get("1"), new Integer(1));
    }

    public void testReplace() {
        assertEquals(list.size(), 3);
        list.set("1", 20);
        assertEquals(list.size(), 3);
        assertEquals(list.get("1"), new Integer(20));
    }

    public void testRemove() {
        assertEquals(list.size(), 3);
        list.remove("1");
        list.remove("5"); // Should not affect size
        assertEquals(list.size(), 2);
    }
}
