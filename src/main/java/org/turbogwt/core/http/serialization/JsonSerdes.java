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

package org.turbogwt.core.http.serialization;

import java.util.Collection;

/**
 * Base class for all SerDes that manipulates serialized JSON.
 *
 * @param <T>   Type of the object to be serialized/deserialized
 *
 * @author Danilo Reinert
 */
public abstract class JsonSerdes<T> implements Serdes<T> {

    public static String[] ACCEPT_PATTERNS = new String[] { "application/json", "application/javascript" };
    public static String[] CONTENT_TYPE_PATTERNS = new String[] { "application/json", "application/javascript" };

    private final Class<T> handledType;

    protected JsonSerdes(Class<T> handledType) {
        this.handledType = handledType;
    }

    /**
     * Method for accessing type of Objects this serializer can handle.
     *
     * @return The class which this deserializer can deserialize
     */
    @Override
    public Class<T> handledType() {
        return handledType;
    }

    /**
     * Informs the content type this serializer handle.
     *
     * @return The content type handled by this serializer.
     */
    @Override
    public String[] accept() {
        return ACCEPT_PATTERNS;
    }

    /**
     * Informs the content type this serializer serializes.
     *
     * @return The content type serialized.
     */
    @Override
    public String[] contentType() {
        return CONTENT_TYPE_PATTERNS;
    }

    /**
     * Given a collection class, returns a new instance of it.
     *
     * @param collectionType    The class of the collection.
     * @param <C>               The type of the collection.
     *
     * @return A new instance to the collection.
     */
    public <C extends Collection<T>> C getCollectionInstance(DeserializationContext context, Class<C> collectionType) {
        final C col = context.getContainerFactoryManager().getFactory(collectionType).get();
        if (col == null)
            throw new UnableToDeserializeException("Could not instantiate the given collection type.");
        return col;
    }

    /**
     * Serialize a collection of T to plain text.
     *
     * @param c         The collection of the object to be serialized.
     * @param context   Context of the serialization
     *
     * @return The object serialized.
     */
    @Override
    public String serializeFromCollection(Collection<T> c, SerializationContext context) {
        if (c == null) return null;
        StringBuilder serialized = new StringBuilder("[");
        for (T t : c) {
            serialized.append(serialize(t, context)).append(',');
        }
        serialized.setCharAt(serialized.length() - 1, ']');
        return serialized.toString();
    }

    protected boolean isArray(String text) {
        final String trim = text.trim();
        return trim.startsWith("[") && trim.endsWith("]");
    }
}