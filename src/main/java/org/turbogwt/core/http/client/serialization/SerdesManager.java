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

import org.turbogwt.core.http.client.Registration;
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
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerDeserializer(Class<T> type, Deserializer<T> deserializer) {
        final String typeName = type.getName();
        deserializers.set(typeName, deserializer);

        return new Registration() {
            @Override
            public void removeHandler() {
                deserializers.remove(typeName);
            }
        };
    }

    /**
     * Register a serializer of the given type.
     *
     * @param type          The class of the serializer's type.
     * @param serializer  The serializer of T.
     * @param <T>           The type of the object to be serialized.
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerSerializer(Class<T> type, Serializer<T> serializer) {
        final String typeName = type.getName();
        serializers.set(typeName, serializer);

        return new Registration() {
            @Override
            public void removeHandler() {
                serializers.remove(typeName);
            }
        };
    }

    /**
     * Register a serializer/deserializer of the given type.
     *
     * @param type      The class of the serializer/deserializer's type.
     * @param serdes    The serializer/deserializer of T.
     * @param <T>       The type of the object to be serialized/deserialized.
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerSerdes(Class<T> type, Serdes<T> serdes) {
        final Registration desReg = registerDeserializer(type, serdes);
        final Registration serReg = registerSerializer(type, serdes);

        return new Registration() {
            @Override
            public void removeHandler() {
                desReg.removeHandler();
                serReg.removeHandler();
            }
        };
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
