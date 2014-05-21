package org.turbogwt.core.js.collections;

import com.google.gwt.core.client.JsArrayString;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An implementation of Set based on {@link JsHashTable}.
 * <p/>
 *
 * This class indexes the objects by resorting to their hashCode method.<br>
 * In order to use it, the values must implement hashCode consistently.
 *
 * @param <T> Type of set values
 */
public class JsHashSet<T> implements Set<T> {

    private final JsHashTable<T> hashTable = JsHashTable.create();
    private int size;

    public JsHashSet() {
    }

    public JsHashSet(Iterable<T> iterable) {
        for (T t : iterable) {
            add(t);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        checkNotNull(o);
        return hashTable.contains((T) o);
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return hashTable.values().toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        JsArray<T> jsArray = hashTable.values();
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

        if (contains(t)) return false;

        ++size;
        hashTable.put(t);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        checkNotNull(o);
        --size;
        return hashTable.remove((T) o);
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
        hashTable.clear();
    }

    private void checkNotNull(Object o) {
        if (o == null) throw new NullPointerException("This Set does not support null values");
    }

    private class Itr implements Iterator<T> {

        private JsArrayString keys = hashTable.hashCodes();
        private int cursor;       // index of next element to return
        private T lastRet;
        private Iterator<T> bucketItr = new JsArrayIterator<>(hashTable.get(keys.get(cursor)));

        public boolean hasNext() {
            return cursor != keys.length() && bucketItr.hasNext();
        }

        @Override
        public T next() {
            int i = cursor;
            if (i >= keys.length()) {
                throw new NoSuchElementException();
            }

            if (!bucketItr.hasNext()) {
                bucketItr = new JsArrayIterator<>(hashTable.get(keys.get(cursor)));
                cursor = i + 1;
            }

            lastRet = bucketItr.next();
            return lastRet;
        }

        @Override
        public void remove() {
            if (lastRet == null) {
                throw new IllegalStateException();
            }
            try {
                bucketItr.remove();
                JsHashSet.this.remove(lastRet);
                lastRet = null;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
