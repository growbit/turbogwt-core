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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;

import java.util.Collection;

import org.turbogwt.core.http.client.Headers;

/**
 * Base class for all Serdes that manipulates serialized JSON objects.
 *
 * @param <T>   Type of the object to serialize/deserialize.
 *
 * @author Danilo Reinert
 */
public abstract class JsonObjectSerdes<T> extends JsonSerdes<T> {

    /**
     * Map response deserialized to JavaScriptObject.
     * You may use {@link org.turbogwt.core.js.client.Overlays} helper methods to easily perform this mapping.
     *
     * @param overlay   The response deserialized to JavaScriptObject using
     *                  {@link com.google.gwt.core.client.JsonUtils#safeEval(String)}.
     * @return The object deserialized.
     */
    public abstract T mapFromOverlay(JavaScriptObject overlay, Headers headers);

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param response Http response body content.
     * @param headers  Http response headers.
     *
     * @return The object deserialized.
     */
    @Override
    public T deserialize(String response, Headers headers) {
        if (!isObject(response))
            throw new UnableToDeserializeException("Response content is not an object");
        return mapFromOverlay(JsonUtils.safeEval(response), headers);
    }

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param collectionType The class of the collection.
     * @param response       Http response body content.
     * @param headers        Http response headers.
     *
     * @return The object deserialized.
     */
    @Override
    public <C extends Collection<T>> C deserializeAsCollection(Class<C> collectionType, String response, Headers
            headers) {
        if (!isArray(response)) throw new UnableToDeserializeException("Response content is not an array.");

        C col = getCollectionInstance(collectionType);
        JsArray<JavaScriptObject> jsArray = JsonUtils.safeEval(response);
        for (int i = 0; i < jsArray.length(); i++) {
            JavaScriptObject jso = jsArray.get(i);
            col.add(mapFromOverlay(jso, headers));
        }

        return col;
    }

    protected boolean isObject(String text) {
        final String trim = text.trim();
        return trim.startsWith("{") && trim.endsWith("}");
    }
}
