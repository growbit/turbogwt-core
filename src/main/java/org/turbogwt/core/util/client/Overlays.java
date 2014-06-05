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

package org.turbogwt.core.util.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Utility methods for Overlay Types manipulation.
 *
 * @author Danilo Reinert
 * @author Javier Ferrero (#deepCopy)
 */
public final class Overlays {

    private Overlays() {
    }

    public static native Integer boxPropertyAsInteger(JavaScriptObject jso, String property) /*-{
        return jso[property] ? @java.lang.Integer::valueOf(I)(jso[property]) : null;
    }-*/;

    public static native Double boxPropertyAsDouble(JavaScriptObject jso, String property) /*-{
        return jso[property] ? @java.lang.Double::valueOf(D)(jso[property]) : null;
    }-*/;

    public static native Long boxPropertyAsLong(JavaScriptObject jso, String property) /*-{
        return jso[property] ? @java.lang.Long::valueOf(Ljava/lang/String;)(jso[property]+'') : null;
    }-*/;

    public static native Boolean boxPropertyAsBoolean(JavaScriptObject jso, String property) /*-{
        return jso[property] ? @java.lang.Boolean::valueOf(Z)(jso[property]) : null;
    }-*/;

    public static void unboxValueToProperty(JavaScriptObject jso, String property, Integer value) {
        if (value != null) {
            setValueToProperty(jso, property, value.intValue());
        } else {
            setNullToProperty(jso, property);
        }
    }

    public static void unboxValueToProperty(JavaScriptObject jso, String property, Double value) {
        if (value != null) {
            setValueToProperty(jso, property, value.doubleValue());
        } else {
            setNullToProperty(jso, property);
        }
    }

    public static void unboxValueToProperty(JavaScriptObject jso, String property, Long value) {
        if (value != null) {
            setValueToProperty(jso, property, value.doubleValue());
        } else {
            setNullToProperty(jso, property);
        }
    }

    public static void unboxValueToProperty(JavaScriptObject jso, String property, Boolean value) {
        if (value != null) {
            setValueToProperty(jso, property, value.booleanValue());
        } else {
            setNullToProperty(jso, property);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends JavaScriptObject> T deepCopy(T obj) {
        return (T) deepCopyNative(obj);
    }

    public static JsArrayString getPropertyNames(JavaScriptObject jso) {
        return getPropertyNames(jso, false);
    }

    public static native JsArrayString getPropertyNames(JavaScriptObject jso, boolean sorted) /*-{
        if (sorted) return Object.keys(jso).sort();
        return Object.keys(jso);
    }-*/;

    public static native boolean getPropertyAsBoolean(JavaScriptObject jso, String property) /*-{
        return jso[property];
    }-*/;

    public static native double getPropertyAsDouble(JavaScriptObject jso, String property) /*-{
        return jso[property];
    }-*/;

    public static native int getPropertyAsInt(JavaScriptObject jso, String property) /*-{
        return jso[property];
    }-*/;

    @SuppressWarnings("unchecked")
    public static <T extends JavaScriptObject> T getPropertyAsObject(JavaScriptObject jso, String property) {
        return (T) getPropertyAsObjectNative(jso, property);
    }

    public static native String getPropertyAsString(JavaScriptObject jso, String property) /*-{
        return jso[property];
    }-*/;

    public static native boolean isPropertyNullOrUndefined(JavaScriptObject jso, String property) /*-{
        return jso[property] ? false : true;
    }-*/;

    public static native void setNullToProperty(JavaScriptObject jso, String property) /*-{
        jso[property] = null;
    }-*/;

    public static native void setValueToProperty(JavaScriptObject jso, String property, String value) /*-{
        jso[property] = value;
    }-*/;

    public static native void setValueToProperty(JavaScriptObject jso, String property, double value) /*-{
        jso[property] = value;
    }-*/;

    public static native void setValueToProperty(JavaScriptObject jso, String property, int value) /*-{
        jso[property] = value;
    }-*/;

    public static native void setValueToProperty(JavaScriptObject jso, String property, boolean value) /*-{
        jso[property] = value;
    }-*/;

    public static native String stringify(JavaScriptObject jso) /*-{
        return JSON.stringify(jso);
    }-*/;

    private static native JavaScriptObject deepCopyNative(JavaScriptObject obj) /*-{
        if (obj == null) return obj;

        var copy;

        if (obj instanceof Date) {
            // Handle Date
            copy = new Date();
            copy.setTime(obj.getTime());
        } else if (obj instanceof Array) {
            // Handle Array
            copy = [];
            for (var i = 0, len = obj.length; i < len; i++) {
                if (obj[i] == null || typeof obj[i] != "object") copy[i] = obj[i];
                // CHECKSTYLE:OFF
                else copy[i] = @org.turbogwt.core.util.client.Overlays::deepCopyNative(Lcom/google/gwt/core/client/JavaScriptObject;)(obj[i]);
                // CHECKSTYLE:ON
            }
        } else {
            // Handle Object
            copy = {};
            for (var attr in obj) {
                if (obj.hasOwnProperty(attr)) {
                    if (obj[attr] == null || typeof obj[attr] != "object") copy[attr] = obj[attr];
                    // CHECKSTYLE:OFF
                    else copy[attr] = @org.turbogwt.core.util.client.Overlays::deepCopyNative(Lcom/google/gwt/core/client/JavaScriptObject;)(obj[attr]);
                    // CHECKSTYLE:ON
                }
            }
        }
        return copy;
    }-*/;

    private static native JavaScriptObject getPropertyAsObjectNative(JavaScriptObject jso, String property) /*-{
        return jso[property];
    }-*/;
}
