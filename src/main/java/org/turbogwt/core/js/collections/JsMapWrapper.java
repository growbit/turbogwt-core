package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JsArrayString;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class JsMapWrapper<T> implements Map<String, T> {

    // TODO: IMPLEMENT UNIT TESTS
    JsMap<T> innerMap = JsMap.create();

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.size() <= 0;
    }

    @Override
    public boolean containsKey(Object o) {
        checkNotNull(o);
        assert (o instanceof String) : "Key should be of type String";

        String key = (String) o;
        return innerMap.contains(key);
    }

    @Override
    public boolean containsValue(Object o) {
        checkNotNull(o);
        assert (o instanceof String) : "Key should be of type String";

        JsArrayString keys = innerMap.keys();
        for (int i = 0; i < keys.length(); i++) {
            String key = keys.get(i);
            T t = innerMap.get(key);
            if (o.equals(t)) return true;
        }
        return false;
    }

    @Override
    public T get(Object o) {
        checkNotNull(o);
        assert (o instanceof String) : "Key should be of type String";

        String key = (String) o;
        return innerMap.get(key);
    }

    @Override
    public T put(String s, T t) {
        checkNotNull(s);
        checkNotNull(t);

        T old = innerMap.get(s);
        innerMap.set(s, t);
        return old;
    }

    @Override
    public T remove(Object o) {
        checkNotNull(o);
        assert (o instanceof String) : "Key should be of type String";

        final String key = (String) o;
        T t = innerMap.get(key);
        innerMap.remove(key);
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        final Set<? extends String> keySet = map.keySet();
        for (String s : keySet) {
            put(s, map.get(s));
        }
    }

    @Override
    public void clear() {
        innerMap.clear();
    }

    @Override
    public Set<String> keySet() {
        // TODO: IMPLEMENT THIS!
        return null;
    }

    @Override
    public Collection<T> values() {
        // TODO: IMPLEMENT THIS!
        return null;
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        // TODO: IMPLEMENT THIS!
        return null;
    }

    private void checkNotNull(Object o) {
        if (o == null) throw new NullPointerException("This map does not support null values");
    }
}
