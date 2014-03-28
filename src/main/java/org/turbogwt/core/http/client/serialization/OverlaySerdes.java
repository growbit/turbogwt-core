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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.turbogwt.core.http.client.Headers;
import org.turbogwt.core.js.client.Native;
import org.turbogwt.core.js.collections.client.JsArrayList;

/**
 * Serializer of Overlay types.
 *
 * @param <T> The overlay type of the data to be serialized.
 *
 * @author Danilo Reinert
 */
public class OverlaySerdes<T extends JavaScriptObject> implements Serdes<T> {

    private static OverlaySerdes<JavaScriptObject> INSTANCE = new OverlaySerdes<>();

    @SuppressWarnings("unchecked")
    public static <O extends JavaScriptObject> OverlaySerdes<O> getInstance() {
        return (OverlaySerdes<O>) INSTANCE;
    }

    @Override
    public T deserialize(String response, Headers headers) {
        return JsonUtils.safeEval(response);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Collection<T>> C deserializeAsCollection(Class<C> collectionType, String response,
                                                               Headers headers) {
        JsArray<T> jsArray = JsonUtils.safeEval(response);
        if (collectionType.equals(List.class) || collectionType.equals(Collection.class)) {
            return (C) new JsArrayList<>(jsArray);
        } else if (collectionType.equals(Set.class)) {
            Set set = new HashSet(jsArray.length());
            for (int i = 0; i < jsArray.length(); i++) {
                T t = jsArray.get(i);
                set.add(t);
            }
            return (C) set;
        }
        throw new UnableToDeserializeException("The class " + collectionType.getName() + " is not supported.");
    }

    @Override
    public String serialize(T t, Headers headers) {
        return Native.stringify(t);
    }

    @Override
    public String serializeFromCollection(Collection<T> c, Headers headers) {
        if (c instanceof JsArrayList) {
            return Native.stringify(((JsArrayList<T>)c).asJsArray());
        }

        if (c instanceof JavaScriptObject) {
            return Native.stringify((JavaScriptObject)c);
        }

        @SuppressWarnings("unchecked")
        JsArray<T> jsArray = (JsArray<T>) JsArray.createArray();
        for (T t : c) jsArray.push(t);
        return Native.stringify(jsArray);
    }
}
