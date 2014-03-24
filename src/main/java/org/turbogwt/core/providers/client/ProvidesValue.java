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
package org.turbogwt.core.providers.client;

import javax.annotation.Nullable;

/**
 * A value provider of an object, usually calling some getter.
 *
 * @param <T> Model type
 * @param <F> Value type
 *
 * @author Danilo Reinert
 */
public interface ProvidesValue<T, F> {

    /**
     * Given a object, returns the value of a property. The property is known at compile time.
     *
     * @param t object to be accessed
     *
     * @return value of a property of the object
     */
    @Nullable
    F getValue(T t);
}
