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

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Registration objects returned when any kind of binding is performed, used to deregister.
 * <p>
 * It inherits from {@link com.google.web.bindery.event.shared.HandlerRegistration} in order to maintain compatibility.
 *
 * @author Danilo Reinert
 */
public interface Registration extends HandlerRegistration {

    /**
     * Deregisters the object associated with this registration if the object is still attached to the source manager.
     * If the object is no longer attached to the source manager, this is a no-op.
     */
    @Override
    void removeHandler();
}
