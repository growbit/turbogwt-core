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

package org.turbogwt.core.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Map of String to String implemented on a JavaScriptObject.
 *
 * @author Thomas Broyer
 */
public class JsMapString extends JavaScriptObject {

    protected JsMapString() {
    }

    public static JsMapString create() {
        return JavaScriptObject.createObject().cast();
    }

    public final native String get(String key) /*-{
        return this[key];
    }-*/;

    public final native String get(String key, String defaultValue) /*-{
        var ret = this[key];
        return ret != null ? ret : defaultValue;
    }-*/;

    public final native void set(String key, String value) /*-{
        this[key] = value;
    }-*/;

    public final native boolean contains(String key) /*-{
        return (key in this);
    }-*/;

    public final native void remove(String key) /*-{
        delete this[key];
    }-*/;
}
