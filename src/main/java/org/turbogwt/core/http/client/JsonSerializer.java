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
import com.google.gwt.core.client.JsonUtils;

import org.turbogwt.core.js.client.Native;

/**
 * Serializer of Overlay types.
 *
 * @param <T> The overlay type of the data to be serialized.
 *
 * @author Danilo Reinert
 */
public class JsonSerializer<T extends JavaScriptObject> implements Serializer<T> {

    private static JsonSerializer<JavaScriptObject> INSTANCE = new JsonSerializer<>();

    @SuppressWarnings("unchecked")
    public static <O extends JavaScriptObject> JsonSerializer<O> getInstance() {
        return (JsonSerializer<O>) INSTANCE;
    }

    @Override
    public T deserialize(String response) {
        return JsonUtils.safeEval(response);
    }

    @Override
    public String serialize(T t) {
        return Native.stringify(t);
    }
}