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
import java.util.List;

import org.turbogwt.core.http.client.Headers;
import org.turbogwt.core.js.client.Overlays;
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
        } else {
            C col = getCollectionInstance(collectionType);
            for (int i = 0; i < jsArray.length(); i++) {
                T t = jsArray.get(i);
                col.add(t);
            }
            return col;
        }
    }

    @Override
    public String serialize(T t, Headers headers) {
        return Overlays.stringify(t);
    }

    @Override
    public String serializeFromCollection(Collection<T> c, Headers headers) {
        if (c instanceof JsArrayList) {
            return Overlays.stringify(((JsArrayList<T>) c).asJsArray());
        }

        if (c instanceof JavaScriptObject) {
            return Overlays.stringify((JavaScriptObject) c);
        }

        @SuppressWarnings("unchecked")
        JsArray<T> jsArray = (JsArray<T>) JsArray.createArray();
        for (T t : c) {
            jsArray.push(t);
        }
        return Overlays.stringify(jsArray);
    }

    /**
     * Given a collection class, returns a new instance of it.
     * You should override this method in order to additionally support other collection types.
     * WARNING! If you override this method, you MUST call return super(), at the final.
     *
     * @param collectionType The class of the collection.
     * @param <C> The type of the collection.
     * @return A new instance to the collection.
     */
    protected <C extends Collection<T>> C getCollectionInstance(Class<C> collectionType) {
        final CollectionFactoryManager.Factory<C> factory = CollectionFactoryManager.getFactory(collectionType);
        if (factory == null)
            throw new UnableToDeserializeException("Could not instantiate the given collection type.");
        return factory.get();
    }
}
