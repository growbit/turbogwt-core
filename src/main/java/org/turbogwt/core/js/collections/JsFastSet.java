package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JsArrayString;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A fast implementation of Set indexed with {@link Object#toString()}.
 * <p/>
 *
 * This class indexes the objects by resorting to their toString method.<br>
 * In order to use it, T should implement toString, so t.toString().equals(otherT.toString())
 * is equivalent to t.equals(otherT).
 *
 * @param <T> Type of set values
 */
public class JsFastSet<T> implements Set<T> {

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
    public boolean isEmpty() {
        return innerMap.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        /*
        JsArrayString keys = innerMap.keys();
        for (int i = 0; i < keys.length(); i++) {
            String key = keys.get(i);
            T t = innerMap.get(key);
            if (t.equals(o)) return true;
        }
        return false;
        */

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

        if (contains(t))
            return false;

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
    public boolean containsAll(Collection<?> objects) {
        for (Object object : objects) {
            if (!contains(object)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        boolean hasChanged = false;
        for (T t : ts) {
            if (add(t))
                hasChanged = true;
        }
        return hasChanged;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        boolean hasChanged = false;

        for (T t : this) {
            if (!objects.contains(t)) {
                remove(t);
                hasChanged = true;
            }
        }

        return hasChanged;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        boolean hasChanged = false;
        for (Object o : objects) {
            if (remove(o)) {
                hasChanged = true;
            }
        }
        return hasChanged;
    }

    @Override
    public void clear() {
        innerMap.clear();
    }

    private void checkNotNull(Object o) {
        if (o == null) throw new NullPointerException("This Set does not support null values");
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