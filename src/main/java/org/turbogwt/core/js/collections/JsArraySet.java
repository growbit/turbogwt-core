package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of Set underpinned by an array.
 * <p/>
 *
 * This is not the fastest implementation of Set at all,
 * but if you have an array and want to wrap in a set mainly for iteration purposes
 * it is a good alternative.
 *
 * @param <T> Type of set values
 */
public class JsArraySet<T> implements Set<T> {

    private final JsArray<T> innerArray;

    public JsArraySet() {
        innerArray = JsArray.create();
    }

    public JsArraySet(T[] array) {
        innerArray = JsArray.fromArray(array);
    }

    public <E extends JavaScriptObject> JsArraySet(com.google.gwt.core.client.JsArray<E> jsArray) {
        this.innerArray = JsArray.cast(jsArray);
    }

    JsArraySet(JavaScriptObject jsArray) {
        this.innerArray = JsArray.cast(jsArray);
    }

    @Override
    public int size() {
        return innerArray.length();
    }

    @Override
    public boolean isEmpty() {
        return innerArray.length() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return innerArray.indexOf(o) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new JsArrayIterator<>(innerArray);
    }

    @Override
    public Object[] toArray() {
        return innerArray.toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a != null && a.length >= innerArray.length()) {
            for (int i = 0; i < innerArray.length(); i++) {
                E e = (E) innerArray.get(i);
                a[i] = e;
            }
            return a;
        }
        return (E[]) innerArray.toArray();
    }

    @Override
    public boolean add(T t) {
        if (contains(t))
            return false;

        innerArray.push(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = innerArray.indexOf(o);

        if (i == -1)
            return false;

        innerArray.splice(i, 1);
        return true;
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
            if (add(t)) hasChanged = true;
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
            if (remove(o)) hasChanged = true;
        }
        return hasChanged;
    }

    @Override
    public void clear() {
        innerArray.setLength(0);
    }
}
