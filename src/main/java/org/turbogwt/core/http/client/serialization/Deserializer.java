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

import java.util.Collection;

import org.turbogwt.core.http.client.Headers;

/**
 * Performs deserialization of Types.
 *
 * @param <T> The type it can deserialize.
 *
 * @author Danilo Reinert
 */
public interface Deserializer<T> {

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param response  Http response body content.
     * @param headers   Http response headers.
     * @return The object deserialized.
     */
    T deserialize(String response, Headers headers);

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param response  Http response body content.
     * @param headers   Http response headers.
     * @return The object deserialized.
     */
    <C extends Collection<T>> C deserializeAsCollection(Class<C> collectionType, String response, Headers headers);
}
