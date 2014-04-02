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

import org.turbogwt.core.http.client.Headers;
import org.turbogwt.core.js.client.Overlays;

/**
 * Context of serialization.
 *
 * @author Danilo Reinert
 */
public class SerializationContext {

    private final Headers headers;

    private SerializationContext(Headers headers) {
        this.headers = headers;
    }

    public static SerializationContext of(Headers headers) {
        return new SerializationContext(Overlays.deepCopy(headers));
    }

    public Headers getHeaders() {
        return headers;
    }
}
