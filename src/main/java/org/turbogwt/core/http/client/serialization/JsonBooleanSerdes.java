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
 * Serdes of JSON booleans.
 *
 * @author Danilo Reinert
 */
public class JsonBooleanSerdes extends JsonValueSerdes<Boolean> {

    private static JsonBooleanSerdes INSTANCE = new JsonBooleanSerdes();

    public static JsonBooleanSerdes getInstance() {
        return INSTANCE;
    }

    /**
     * Deserialize the plain text into a Boolean.
     *
     * @param response Http response body content.
     * @param headers  Http response headers.
     *
     * @return The object deserialized.
     */
    @Override
    public Boolean deserialize(String response, Headers headers) {
        return Boolean.valueOf(response);
    }

    /**
     * Serialize Boolean to plain text.
     *
     * @param b       The boolean to be serialized.
     * @param headers Http headers from current request.
     *
     * @return The object serialized.
     */
    @Override
    public String serialize(Boolean b, Headers headers) {
        return String.valueOf(b);
    }
}
