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

/**
 * @author Danilo Reinert
 */
public class UriBuilderImplTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.turbogwt.core.http.HttpTest";
    }

    public void testBasicFlow() {
        String expected = "http://user:pwd@localhost:8888/server/root;class=2;class=5;class=6" +
                "/child;group=A;subGroup=A.1;subGroup=A.2?age=12&name=Aa&name=Zz#first";

        String uri = new UriBuilderImpl()
                .scheme("http")
                .userInfo("user:pwd")
                .host("localhost")
                .port(8888)
                .path("server")
                .segment("root")
                .matrixParam("class", 2, 5, 6)
                .segment("child")
                .matrixParam("group", "A")
                .matrixParam("subGroup", "A.1", "A.2")
                .queryParam("age", 12)
                .queryParam("name", "Aa", "Zz")
                .fragment("first")
                .build();

        assertEquals(expected, uri);
    }

    public void testCommaSeparatedStrategy() {
        String expected = "http://user:pwd@localhost:8888/server/root;class=2,5,6" +
                "/child;group=A;subGroup=A.1,A.2?age=12&name=Aa,Zz#first";

        String uri = new UriBuilderImpl()
                .multipleParamStrategy(MultipleParamStrategy.COMMA_SEPARATED)
                .scheme("http")
                .userInfo("user:pwd")
                .host("localhost")
                .port(8888)
                .path("server")
                .segment("root")
                .matrixParam("class", 2, 5, 6)
                .segment("child")
                .matrixParam("group", "A")
                .matrixParam("subGroup", "A.1", "A.2")
                .queryParam("age", 12)
                .queryParam("name", "Aa", "Zz")
                .fragment("first")
                .build();

        assertEquals(expected, uri);
    }
}