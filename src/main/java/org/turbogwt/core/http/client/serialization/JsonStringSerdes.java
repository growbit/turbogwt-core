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

package org.turbogwt.core.http.client.serialization;

import org.turbogwt.core.http.client.Headers;

/**
 * SerDes of JSON strings.
 *
 * @author Danilo Reinert
 */
public class JsonStringSerdes extends JsonValueSerdes<String> {

    private static JsonStringSerdes INSTANCE = new JsonStringSerdes();

    public static JsonStringSerdes getInstance() {
        return INSTANCE;
    }

    /**
     * Deserialize the plain text into a String.
     *
     * @param response Http response body content.
     * @param headers  Http response headers.
     *
     * @return The string deserialized.
     */
    @Override
    public String deserialize(String response, Headers headers) {
        return response.substring(1, response.length() - 1);
    }

    /**
     * Serialize String to plain text.
     *
     * @param s       The string to be serialized.
     * @param headers Http headers from current request.
     *
     * @return The string serialized.
     */
    @Override
    public String serialize(String s, Headers headers) {
        return "\"" + s + "\"";
    }
}
