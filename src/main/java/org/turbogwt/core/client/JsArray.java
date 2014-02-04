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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A more featured extension of {@link com.google.gwt.core.client.JsArray}.
 *
 * @param <T> Type of list values
 * @author Danilo Reinert
 */
public class JsArray<T extends JavaScriptObject> extends com.google.gwt.core.client.JsArray<T> {

    protected JsArray() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends JavaScriptObject> JsArray<T> parseJavaScriptObject(JavaScriptObject jso) {
        return (JsArray<T>) jso;
    }

    public final native void add(int index, T element) /*-{
        this.splice(index, 0, element)
    }-*/;

    public final native JsArray<T> remove(int index, int quantity) /*-{
        return this.splice(index, quantity)
    }-*/;

    public final native int indexOf(JavaScriptObject search) /*-{
        return this.indexOf(search)
    }-*/;

    public final native int lastIndexOf(JavaScriptObject search) /*-{
        return this.lastIndexOf(search)
    }-*/;

    public final native JsArray<T> slice(int begin) /*-{
        return this.slice(begin);
    }-*/;

    public final native JsArray<T> slice(int begin, int end) /*-{
        return this.slice(begin, end);
    }-*/;

    public final native JsArray concat(JsArray<T> a) /*-{
        return this.concat(a)
    }-*/;

    public final native T pop() /*-{
        return this.pop()
    }-*/;

    public final T[] toArray() {
        if (GWT.isScript()) {
            return reinterpretCast(slice(0));
        } else {
            int length = length();
            @SuppressWarnings("unchecked")
            T[] ret = (T[]) new JavaScriptObject[length];
            for (int i = 0, l = length; i < l; i++) {
                ret[i] = get(i);
            }
            return ret;
        }
    }

    private static native <T extends JavaScriptObject> T[] reinterpretCast(
            com.google.gwt.core.client.JsArray<T> value) /*-{
        return value;
    }-*/;
}
