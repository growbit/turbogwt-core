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

package org.turbogwt.core.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.turbogwt.core.js.collections.JsArrayList;
import org.turbogwt.core.js.collections.JsMap;

/**
 * Manager of container (collection) factories.
 *
 * @author Danilo Reinert
 */
public final class ContainerFactoryManager {

    private final JsMap<Factory<? extends Collection>> factories;

    public ContainerFactoryManager() {
        factories = JsMap.create();
        final Factory<ArrayList> arrayListFactory = new Factory<ArrayList>() {
            @Override
            public ArrayList get() {
                return new ArrayList<>();
            }
        };
        factories.set(Collection.class.getName(), arrayListFactory);
        factories.set(List.class.getName(), arrayListFactory);
        factories.set(ArrayList.class.getName(), arrayListFactory);
        factories.set(LinkedList.class.getName(), new Factory<LinkedList>() {
            @Override
            public LinkedList get() {
                return new LinkedList<>();
            }
        });
        factories.set(JsArrayList.class.getName(), new Factory<JsArrayList>() {
            @Override
            public JsArrayList get() {
                return new JsArrayList();
            }
        });
        final Factory<HashSet> hashSetFactory = new Factory<HashSet>() {
            @Override
            public HashSet get() {
                return new HashSet();
            }
        };
        factories.set(Set.class.getName(), hashSetFactory);
        factories.set(HashSet.class.getName(), hashSetFactory);
        factories.set(TreeSet.class.getName(), new Factory<TreeSet>() {
            @Override
            public TreeSet get() {
                return new TreeSet();
            }
        });
    }

    /**
     * Map a {@link Factory} to some collection class.
     *
     * @param type      The type of the collection
     * @param factory   The factory of the collection
     * @param <C>       The type of the collection
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link ContainerFactoryManager}
     */
    public <C extends Collection> Registration registerFactory(Class<C> type, Factory<C> factory) {
        final String typeName = type.getName();
        factories.set(typeName, factory);

        return new Registration() {
            @Override
            public void removeHandler() {
                factories.remove(typeName);
            }
        };
    }

    /**
     * Given collection some class, return its {@link Factory}.
     *
     * @param <C> Collection type
     */
    @SuppressWarnings("unchecked")
    public <C extends Collection> Factory<C> getFactory(Class<C> type) {
        return (Factory<C>) factories.get(type.getName(), null);
    }
}
