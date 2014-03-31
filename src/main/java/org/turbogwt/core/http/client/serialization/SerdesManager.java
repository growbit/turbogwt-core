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

import org.turbogwt.core.js.collections.client.JsMap;

/**
 * Manager for registering and retrieving Serializers and Deserializers.
 *
 * @author Danilo Reinert
 */
public class SerdesManager {

    private final JsMap<Deserializer<?>> deserializers = JsMap.create();
    private final JsMap<Serializer<?>> serializers = JsMap.create();

    /**
     * Register a deserializer of the given type.
     *
     * @param type          The class of the deserializer's type.
     * @param deserializer  The deserializer of T.
     * @param <T>           The type of the object to be deserialized.
     */
    public <T> void registerDeserializer(Class<T> type, Deserializer<T> deserializer) {
        deserializers.set(type.getName(), deserializer);
    }

    /**
     * Register a serializer of the given type.
     *
     * @param type          The class of the serializer's type.
     * @param serializer  The serializer of T.
     * @param <T>           The type of the object to be serialized.
     */
    public <T> void registerSerializer(Class<T> type, Serializer<T> serializer) {
        serializers.set(type.getName(), serializer);
    }

    /**
     * Register a serializer/deserializer of the given type.
     *
     * @param type      The class of the serializer/deserializer's type.
     * @param serdes    The serializer/deserializer of T.
     * @param <T>       The type of the object to be serialized/deserialized.
     */
    public <T> void registerSerdes(Class<T> type, Serdes<T> serdes) {
        registerDeserializer(type, serdes);
        registerSerializer(type, serdes);
    }

    /**
     * Retrieve Deserializer from manager.
     *
     * @param type The type class of the deserializer.
     * @param <T> The type of the deserializer.
     * @return The deserializer of the specified type.
     * @throws SerializationException if no deserializer was registered for the class.
     */
    @SuppressWarnings("unchecked")
    public <T> Deserializer<T> getDeserializer(Class<T> type) throws SerializationException {
        if (!deserializers.contains(type.getName())) {
            throw new SerializationException("There is no Deserializer registered for " + type.getName() + ".");
        }
        return (Deserializer<T>) deserializers.get(type.getName());
    }

    /**
     * Retrieve Serializer from manager.
     *
     * @param type The type class of the serializer.
     * @param <T> The type of the serializer.
     * @return The serializer of the specified type.
     * @throws SerializationException if no serializer was registered for the class.
     */
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getSerializer(Class<T> type) throws SerializationException {
        if (!serializers.contains(type.getName())) {
            throw new SerializationException("There is no Serializer registered for " + type.getName() + ".");
        }
        return (Serializer<T>) serializers.get(type.getName());
    }
}
