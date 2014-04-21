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
 * SerDes for Void type.
 * Returns null for every method.
 *
 * @author Danilo Reinert
 */
public class VoidSerdes implements Serdes<Void> {

    public static final String[] CONTENT_TYPE_HEADER = new String[] {"*/*"};
    public static final String[] ACCEPT_HEADER = new String[] {"*/*"};

    private static VoidSerdes INSTANCE = new VoidSerdes();

    public static VoidSerdes getInstance() {
        return INSTANCE;
    }

    /**
     * Method for accessing type of Objects this serializer can handle.
     *
     * @return The class which this deserializer can deserialize
     */
    @Override
    public Class<Void> handledType() {
        return Void.class;
    }

    /**
     * Informs the content type this serializer serializes.
     *
     * @return The content type serialized.
     */
    @Override
    public String[] contentType() {
        return CONTENT_TYPE_HEADER;
    }

    /**
     * Informs the content type this serializer handle.
     *
     * @return The content type handled by this serializer.
     */
    @Override
    public String[] accept() {
        return ACCEPT_HEADER;
    }

    @Override
    public Void deserialize(String response, DeserializationContext context) {
        return null;
    }

    @Override
    public <C extends Collection<Void>> C deserializeAsCollection(Class<C> collectionType, String response,
                                                                  DeserializationContext context) {
        return null;
    }

    @Override
    public String serialize(Void v, SerializationContext context) {
        return null;
    }

    @Override
    public String serializeFromCollection(Collection<Void> c, SerializationContext context) {
        return null;
    }
}