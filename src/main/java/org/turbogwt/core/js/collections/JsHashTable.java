package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class JsHashTable<T> extends JavaScriptObject {

    protected JsHashTable() {
    }

    public static native <T> JsHashTable<T> create() /*-{
        return {};
    }-*/;

    public final native void clear() /*-{
        for (var key in this) delete this[key];
    }-*/;

    public final boolean contains(T value) {
        JsArray<T> bucket = get(value.hashCode());
        return (bucket != null) && (bucket.indexOf(value) > -1);
    }

    public final native JsArray<T> get(int hashCode) /*-{
        return this[hashCode];
    }-*/;

    public final native JsArrayString hashCodes() /*-{
        return Object.keys(this);
    }-*/;

    public final void put(T value) {
        checkNotNull(value);
        put(value.hashCode(), value);
    }

    public final boolean remove(T value) {
        final int hashCode = value.hashCode();
        JsArray<T> bucket = get(hashCode);
        if (bucket != null) {
            final int i = bucket.indexOf(value);
            if (i > -1) {
                bucket.splice(i);
                if (bucket.length() == 0) {
                    // Save memory by disposing empty buckets
                    deleteBucket(hashCode);
                }
                return true;
            }
        }
        return false;
    }

    private void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException("This hashtable does not support null values");
        }
    }

    private final native void deleteBucket(int hashCode) /*-{
        delete this[hashCode];
    }-*/;

    private native void put(int hashCode, T value) /*-{
        if (!this[hashCode]) this[hashCode] = [];
        this[hashCode].push(value);
    }-*/;
}
