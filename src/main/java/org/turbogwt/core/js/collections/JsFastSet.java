package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JsArrayString;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A fast implementation of Set indexed with {@link Object#toString()}.
 * <p/>
 *
 * This class indexes the objects by resorting to their toString method.
 * <br>
 * In order to use it, T should implement toString,
 * so t.toString().equals(otherT.toString()) is equivalent to t.equals(otherT).
 *
 * @param <T> Type of set values
 */
public class JsFastSet<T> extends AbstractSet<T> {

    private final JsMap<T> innerMap = JsMap.create();

    public JsFastSet() {
    }

    public JsFastSet(Iterable<T> iterable) {
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
        return innerMap.get(o.toString()) != null;
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

        innerMap.set(t.toString(), t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        checkNotNull(o);

        if (contains(o)) {
            innerMap.remove(o.toString());
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        innerMap.clear();
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
