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
 * Base class for all Serdes that manipulates serialized JSON.
 *
 * @param <T>   Type of the object to be serialized/deserialized.
 *
 * @author Danilo Reinert
 */
public abstract class JsonSerdes<T> implements Serdes<T> {

    /**
     * Given a collection class, returns a new instance of it.
     *
     * @param collectionType    The class of the collection.
     * @param <C>               The type of the collection.
     *
     * @return A new instance to the collection.
     */
    public <C extends Collection<T>> C getCollectionInstance(Class<C> collectionType) {
        final C col = CollectionFactoryMap.getFactory(collectionType).create();
        if (col == null)
            throw new UnableToDeserializeException("Could not instantiate the given collection type.");
        return col;
    }

    /**
     * Serialize a collection of T to plain text.
     *
     * @param c       The collection of the object to be serialized.
     * @param headers Http headers from current request.
     *
     * @return The object serialized.
     */
    @Override
    public String serializeFromCollection(Collection<T> c, Headers headers) {
        if (c == null) return null;
        StringBuilder serialized = new StringBuilder("[");
        for (T t : c) {
            serialized.append(serialize(t, headers)).append(',');
        }
        serialized.setCharAt(serialized.length() - 1, ']');
        return serialized.toString();
    }

    protected boolean isArray(String text) {
        final String trim = text.trim();
        return trim.startsWith("[") && trim.endsWith("]");
    }
}
