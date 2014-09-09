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

package org.turbogwt.core.collections.client;

import com.google.gwt.core.client.JsArrayString;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A fast implementation of Set indexed by a String representation of the object.
 * <p/>
 *
 * This class indexes objects by resorting to {@link Object#toString()}.
 * <br>
 * Additionally, you can provide a implementation of {@link AsString} to generate the string representation.
 * <br>
 * In order to use it correctly, T should implement toString,
 * so that t.toString().equals(otherT.toString()) is equivalent to t.equals(otherT).
 * <br>
 * The same is expected for the {@link AsString} behavior when provided.
 *
 * @param <T> Type of set values
 *
 * @author Danilo Reinert
 */
public class LightSet<T> extends AbstractSet<T> {

    private final AsString<T> asString;

    public interface AsString<T> {
        String asString(T t);
    }

    private final JsMap<T> innerMap = JsMap.create();

    public LightSet() {
        this.asString = null;
    }

    public LightSet(Iterable<T> iterable) {
        this.asString = null;
        for (T t : iterable) {
            add(t);
        }
    }

    public LightSet(AsString<T> asString) {
        this.asString = asString;
    }

    public LightSet(Iterable<T> iterable, AsString<T> asString) {
        this.asString = asString;
        for (T t : iterable) {
            add(t);
        }
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean contains(Object o) {
        checkNotNull(o);
        return innerMap.get(asString(o)) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return innerMap.values().toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        JsArray<T> jsArray = innerMap.values();
        if (a != null && a.length >= jsArray.length()) {
            for (int i = 0; i < jsArray.length(); i++) {
                E e = (E) jsArray.get(i);
                a[i] = e;
            }
            return a;
        }
        return (E[]) jsArray.toArray();
    }

    @Override
    public boolean add(T t) {
        checkNotNull(t);

        if (contains(t)) {
            return false;
        }

        innerMap.put(asString(t), t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        checkNotNull(o);

        if (contains(o)) {
            innerMap.remove(asString(o));
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        innerMap.clear();
    }

    @SuppressWarnings("unchecked")
    private String asString(Object o) {
        try {
            return asString != null ? asString.asString((T) o) : o.toString();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The provided object is not a instance of the type of this Set.", e);
        }
    }

    private void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException("This Set does not support null values");
        }
    }

    private class Itr implements Iterator<T> {

        private JsArrayString keys = innerMap.keys();
        private int cursor;       // index of next element to return
        private int lastRet = -1; // index of last element returned; -1 if no such

        public boolean hasNext() {
            return cursor != keys.length();
        }

        @Override
        public T next() {
            int i = cursor;
            if (i >= keys.length()) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return innerMap.get(keys.get(lastRet = i));
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            try {
                innerMap.remove(keys.get(lastRet));
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
