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

package org.turbogwt.core.js.collections.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link java.util.List} with an underlying {@link JsArray}.
 *
 * @param <T> Type of list values
 * @author Danilo Reinert
 */
public class JsArrayList<T extends JavaScriptObject> implements List<T> {

    private final JsArray<T> jsArray;

    @SuppressWarnings("unchecked")
    public JsArrayList() {
        this.jsArray = (JsArray<T>) JavaScriptObject.createArray();
    }

    @SuppressWarnings("unchecked")
    public JsArrayList(com.google.gwt.core.client.JsArray<T> jsArray) {
        this.jsArray = (JsArray<T>) (jsArray != null ? jsArray : JavaScriptObject.createArray());
    }

    public com.google.gwt.core.client.JsArray<T> asJsArray() {
        return jsArray;
    }

    @Override
    public int size() {
        return jsArray.length();
    }

    @Override
    public boolean isEmpty() {
        return jsArray.length() == 0;
    }

    @Override
    public native boolean contains(Object o) /*-{
        return this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray.indexOf(o) > -1;
    }-*/;

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return jsArray.toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        return (E[]) jsArray.toArray();
    }

    @Override
    public boolean add(T t) {
        jsArray.push(t);
        return true;
    }

    @Override
    public native boolean remove(Object o) /*-{
        var a = this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray;
        var i = a.indexOf(o);
        if (i == -1)
            return false;
        a.splice(i, 1);
        return true;
    }-*/;

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean containsAll = true;
        for (Object o : c) {
            int indexOfIt = jsArray.indexOf((JavaScriptObject) o);
            if (indexOfIt == -1) {
                containsAll = false;
                break;
            }
        }
        return containsAll;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("addAll not supported by JsArrayList");
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> c) {
        throw new UnsupportedOperationException("addAll not supported by JsArrayList");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not supported by JsArrayList");
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        throw new UnsupportedOperationException("retainAll not supported by JsArrayList");
    }

    @Override
    public native void clear() /*-{
        this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray.length = 0;
    }-*/;

    @Override
    public T get(int i) {
        if (i > -1 && i < jsArray.length()) {
            return jsArray.get(i);
        } else {
            return null;
        }
    }

    @Override
    public T set(int i, T t) {
        if (i < -1 || i > jsArray.length()) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
        jsArray.set(i, t);
        return t;
    }

    @Override
    public void add(int i, T t) {
        if (i < -1 || i > jsArray.length()) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
        jsArray.add(i, t);
    }

    @Override
    public native T remove(int i) /*-{
        var a = this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray;
        if (i >= a.length) {
            return false;
        }
        var toReturn = a[i];
        a.splice(i, 1);
        return toReturn;
    }-*/;

    @Override
    public native int indexOf(Object o) /*-{
        return this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray.indexOf(o);
    }-*/;

    @Override
    public native int lastIndexOf(Object o) /*-{
        return this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray.lastIndexOf(o);
    }-*/;

    @Override
    public ListIterator<T> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        if (i < 0 || i > jsArray.length()) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
        return new ListItr(i);
    }

    @Override
    public List<T> subList(int i, int i2) {
        return new JsArrayList<T>(subArray(i, i2));
    }

    public native com.google.gwt.core.client.JsArray<T> subArray(int i, int i2) /*-{
        return this.@org.turbogwt.core.js.collections.client.JsArrayList::jsArray.slice(i, i2);
    }-*/;

    private class Itr implements Iterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        public boolean hasNext() {
            return cursor != JsArrayList.this.size();
        }

        public T next() {
            int i = cursor;
            if (i >= JsArrayList.this.size()) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return JsArrayList.this.jsArray.get(lastRet = i);
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                JsArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends Itr implements ListIterator<T> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public T previous() {
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            cursor = i;
            return JsArrayList.this.jsArray.get(lastRet = i);
        }

        public void set(T e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            JsArrayList.this.set(lastRet, e);
        }

        public void add(T e) {
            int i = cursor;
            JsArrayList.this.add(i, e);
            cursor = i + 1;
            lastRet = -1;
        }
    }
}