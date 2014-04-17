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

import org.turbogwt.core.js.collections.client.JsMap;

/**
 * Stores form params and values.
 *
 * @author Danilo Reinert
 */
public class FormData {

    private String contentType = "application/x-www-form-urlencoded";
    private boolean encode = true;
    private MultipleParamStrategy multipleParamStrategy = MultipleParamStrategy.REPEATED_PARAM;
    private JsMap<Object> params = JsMap.create();

    private FormData() {
    }

    public static FormData of() {
        return new FormData();
    }

    public static FormData of(String contentType, boolean encode) {
        return new FormData().contentType(contentType).encode(encode);
    }

    public static FormData of(String contentType, boolean encode, MultipleParamStrategy strategy) {
        return new FormData().contentType(contentType).encode(encode).strategy(strategy);
    }

    public static FormData of(MultipleParamStrategy strategy) {
        return new FormData().strategy(strategy);
    }

    public Object get(String param) {
        return params.get(param);
    }

    public String getContentType() {
        return contentType;
    }

    public MultipleParamStrategy getMultipleParamStrategy() {
        return multipleParamStrategy;
    }

    public boolean isEncode() {
        return encode;
    }

    public FormData put(String name, Object... values) {
        params.set(name, values);
        return this;
    }

    protected FormData contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    protected FormData encode(boolean encode) {
        this.encode = encode;
        return this;
    }

    protected FormData strategy(MultipleParamStrategy multipleParamStrategy) {
        this.multipleParamStrategy = multipleParamStrategy;
        return this;
    }
}
