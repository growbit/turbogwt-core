/*
 * Copyright 2009 Thomas Broyer
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

package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Map of String to Object implemented on a JavaScriptObject.
 *
 * @param <T> Type of mapped values
 *
 * @author Thomas Broyer
 * @author Danilo Reinert
 */
public class JsMap<T> extends JavaScriptObject {

    protected JsMap() {
    }

    public static native <T> JsMap<T> create() /*-{
        var o = {};
        Object.defineProperties(o, {__size__: {enumerable: false, writable: true, value: 0}});
        return o;
    }-*/;

    public final native void clear() /*-{
        // Although not fast, it's safer than creating new objects
        for (var key in this) delete this[key];
        Object.defineProperties(this, {__size__: {enumerable: false, writable: true, value: 0}});
    }-*/;

    public final native T get(String key) /*-{
        return this[key];
    }-*/;

    public final native T get(String key, T defaultValue) /*-{
        return this[key] || defaultValue;
    }-*/;

    public final native void set(String key, T value) /*-{
        if (!this[key]) {
            // Check if size hidden variable was initialized
            if (!this.__size__) {
                var size = Object.keys(this).length;
                Object.defineProperties(this, {__size__: {enumerable: false, writable: true, value: size}});
            }
            ++this.__size__;
        }
        this[key] = value;
    }-*/;

    public final native boolean contains(String key) /*-{
        return (key in this);
    }-*/;

    public final native void remove(String key) /*-{
        if (this[key]) {
            // Check if size hidden variable was initialized
            if (!this.__size__) {
                var size = Object.keys(this).length;
                Object.defineProperties(this, {__size__: {enumerable: false, writable: true, value: size}});
            }
            --this.__size__;
        }
        delete this[key];
    }-*/;

    public final native int size() /*-{
        // Check if size hidden variable was initialized
        if (!this.__size__) {
            var size = Object.keys(this).length;
            Object.defineProperties(this, {__size__: {enumerable: false, writable: true, value: size}});
        }
        return this.__size__;
    }-*/;

    public final String keyOf(T t) {
        // TODO: improve by implementing in JSNI directly
        JsArrayString keys = keys();
        for (int i = 0; i < keys.length(); i++) {
            String key = keys.get(i);
            if (get(key).equals(t)) return key;
        }
        return null;
    }

    public final native JsArrayString keys() /*-{
        return Object.keys(this);
    }-*/;

    public final native JsArray<T> values() /*-{
        var values = [];
        for (var key in this) values.push(this[key]);
        return values;
    }-*/;
}
