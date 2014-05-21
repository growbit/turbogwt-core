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

import com.google.gwt.core.client.JsArrayString;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A Map from String to Object implementation over a Javascript Object.
 *
 * @param <T> The type of the map values
 *
 * @author Danilo Reinert
 */
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
        assert o instanceof String : "Key should be of type String";

        String key = (String) o;
        return innerMap.contains(key);
    }

    @Override
    public boolean containsValue(Object o) {
        checkNotNull(o);
        assert o instanceof String : "Key should be of type String";

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
        assert o instanceof String : "Key should be of type String";

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
        assert o instanceof String : "Key should be of type String";

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
        // TODO: implement a new set extending this one, reflecting all operations to the map
        return new JsSafeSet<>(innerMap.keys());
    }

    @Override
    public Collection<T> values() {
        // TODO: implement a new collection extending this one, reflecting all operations to the map
        return new JsArrayList<>(innerMap.values());
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        // TODO: implement a new set extending this one, reflecting all operations to the map
        return null;
    }

    private void checkNotNull(Object o) {
        if (o == null) throw new NullPointerException("This map does not support null values");
    }

    private static class JsEntry<T> implements Entry<String, T> {

        private final JsMapWrapper<T> map;
        private final String key;

        private JsEntry(JsMapWrapper<T> map, String key) {
            this.map = map;
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return map.get(key);
        }

        @Override
        public T setValue(T t) {
            return map.put(key, t);
        }
    }
}
