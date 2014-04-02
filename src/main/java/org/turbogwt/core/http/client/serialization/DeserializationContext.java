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

import org.turbogwt.core.http.client.CollectionFactoryManager;
import org.turbogwt.core.http.client.Headers;
import org.turbogwt.core.js.client.Overlays;

/**
 * Context of deserialization.
 *
 * @author Danilo Reinert
 */
public class DeserializationContext {

    private final Headers headers;
    private final CollectionFactoryManager collectionFactoryManager;

    private DeserializationContext(Headers headers, CollectionFactoryManager collectionFactoryManager) {
        this.headers = headers;
        this.collectionFactoryManager = collectionFactoryManager;
    }

    public static DeserializationContext of(Headers headers, CollectionFactoryManager collectionFactoryManager) {
        return new DeserializationContext(Overlays.deepCopy(headers), collectionFactoryManager);
    }

    public Headers getHeaders() {
        return headers;
    }

    public CollectionFactoryManager getCollectionFactoryManager() {
        return collectionFactoryManager;
    }
}
