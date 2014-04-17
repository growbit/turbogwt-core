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

package org.turbogwt.core.http.client.serialization;

import java.util.Map;
import java.util.TreeMap;

import org.turbogwt.core.http.client.Registration;

/**
 * Manager for registering and retrieving Serializers and Deserializers.
 *
 * @author Danilo Reinert
 */
public class SerdesManager {

    private final Map<Key, Deserializer<?>> deserializers = new TreeMap<>();
    private final Map<Key, Serializer<?>> serializers = new TreeMap<>();

    /**
     * Register a deserializer of the given type.
     *
     * @param type          The class of the deserializer's type.
     * @param deserializer  The deserializer of T.
     * @param <T>           The type of the object to be deserialized.
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerDeserializer(Class<T> type, Deserializer<T> deserializer) {
        final String[] accept = deserializer.accept();
        final Key[] keys = new Key[accept.length];
        for (int i = 0; i < accept.length; i++) {
            String pattern = accept[i];
            final Key key = new Key(type, pattern);
            deserializers.put(key, deserializer);
            keys[i] = key;
        }

        return new Registration() {
            @Override
            public void removeHandler() {
                for (Key key : keys) {
                    deserializers.remove(key);
                }
            }
        };
    }

    /**
     * Register a serializer of the given type.
     *
     * @param type          The class of the serializer's type.
     * @param serializer  The serializer of T.
     * @param <T>           The type of the object to be serialized.
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerSerializer(Class<T> type, Serializer<T> serializer) {
        final String[] contentType = serializer.contentType();
        final Key[] keys = new Key[contentType.length];
        for (int i = 0; i < contentType.length; i++) {
            String pattern = contentType[i];
            final Key key = new Key(type, pattern);
            serializers.put(key, serializer);
            keys[i] = key;
        }

        return new Registration() {
            @Override
            public void removeHandler() {
                for (Key key : keys) {
                    serializers.remove(key);
                }
            }
        };
    }

    /**
     * Register a serializer/deserializer of the given type.
     *
     * @param type      The class of the serializer/deserializer's type.
     * @param serdes    The serializer/deserializer of T.
     * @param <T>       The type of the object to be serialized/deserialized.
     *
     * @return  The {@link Registration} object, capable of cancelling this registration
     *          to the {@link SerdesManager}.
     */
    public <T> Registration registerSerdes(Class<T> type, Serdes<T> serdes) {
        final Registration desReg = registerDeserializer(type, serdes);
        final Registration serReg = registerSerializer(type, serdes);

        return new Registration() {
            @Override
            public void removeHandler() {
                desReg.removeHandler();
                serReg.removeHandler();
            }
        };
    }

    /**
     * Retrieve Deserializer from manager.
     *
     * @param type The type class of the deserializer.
     * @param <T> The type of the deserializer.
     * @return The deserializer of the specified type.
     * @throws SerializationException if no deserializer was registered for the class.
     */
    @SuppressWarnings("unchecked")
    public <T> Deserializer<T> getDeserializer(Class<T> type, String contentType) throws SerializationException {
        final Key key = new Key(type, contentType);

        for (Key k : deserializers.keySet()) {
            if (k.matches(key)) return (Deserializer<T>) deserializers.get(k);
        }

        throw new SerializationException("There is no Deserializer registered for " + type.getName() +
                " and content-type " + contentType + ".");
    }

    /**
     * Retrieve Serializer from manager.
     *
     * @param type The type class of the serializer.
     * @param <T> The type of the serializer.
     * @return The serializer of the specified type.
     * @throws SerializationException if no serializer was registered for the class.
     */
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getSerializer(Class<T> type, String contentType) throws SerializationException {
        final Key key = new Key(type, contentType);

        for (Key k : serializers.keySet()) {
            if (k.matches(key)) return (Serializer<T>) serializers.get(k);
        }

        throw new SerializationException("There is no Serializer registered for type " + type.getName() +
                " and content-type " + contentType + ".");
    }

    private static class Key implements Comparable<Key> {

        final Class<?> type;
        final String contentType;
        final double factor;

        private Key(Class<?> type, String contentType) {
            this.type = type;
            this.contentType = contentType;
            this.factor = 1.0;
        }

        private Key(Class<?> type, String contentType, double factor) {
            this.type = type;
            this.contentType = contentType;
            this.factor = factor;
        }

        // TODO: test exhaustively
        public boolean matches(Key key) {
            if (!key.type.equals(this.type)) {
                return false;
            }

            boolean matches;

            final int thisSep = this.contentType.indexOf("/");
            final int otherSep = key.contentType.indexOf("/");

            String thisInitialPart = this.contentType.substring(0, thisSep);
            String otherInitialPart = key.contentType.substring(0, otherSep);

            if (thisInitialPart.contains("*")) {
                matches = matchPartsSafely(thisInitialPart, otherInitialPart);
            } else if (otherInitialPart.contains("*")) {
                matches = matchPartsUnsafely(otherInitialPart, thisInitialPart);
            } else {
                matches = thisInitialPart.equals(otherInitialPart);
            }

            if (!matches) return false;

            final String thisFinalPart = this.contentType.substring(thisSep + 1);
            final String otherFinalPart = key.contentType.substring(otherSep + 1);

            if (thisFinalPart.contains("*")) {
                matches = matchPartsSafely(thisFinalPart, otherFinalPart);
            } else if (otherFinalPart.contains("*")) {
                matches = matchPartsUnsafely(otherFinalPart, thisFinalPart);
            } else {
                matches = thisFinalPart.equals(otherFinalPart);
            }

            return matches;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }

            final Key key = (Key) o;

            if (Double.compare(key.factor, factor) != 0) {
                return false;
            }
            if (!contentType.equals(key.contentType)) {
                return false;
            }
            if (!type.equals(key.type)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = type.hashCode();
            result = 31 * result + contentType.hashCode();
            temp = Double.doubleToLongBits(factor);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public int compareTo(Key key) {
            int result = this.type.getSimpleName().compareTo(key.type.getSimpleName());

            if (result == 0) {
                final int thisSep = this.contentType.indexOf("/");
                final int otherSep = key.contentType.indexOf("/");

                String thisInitialPart = this.contentType.substring(0, thisSep);
                String otherInitialPart = key.contentType.substring(0, otherSep);
                result = thisInitialPart.compareTo(otherInitialPart);

                // Invert the result if the winner contains wildcard
                if ((result == -1 && thisInitialPart.contains("*")) || (result == 1 && otherInitialPart.contains("*")))
                    result = -result;

                if (result == 0) {
                    String thisFinalPart = this.contentType.substring(thisSep + 1);
                    String otherFinalPart = key.contentType.substring(otherSep + 1);
                    result = thisFinalPart.compareTo(otherFinalPart);

                    // Invert the result if the winner contains wildcard
                    if ((result == -1 && thisFinalPart.contains("*")) || (result == 1 && otherFinalPart.contains("*")))
                        result = -result;

                    if (result == 0) {
                        // Invert comparison because the greater the factor the greater the precedence.
                        result = Double.compare(key.factor, this.factor);
                    }
                }
            }

            return result;
        }

        private boolean matchPartsSafely(String left, String right) {
            boolean matches = true;
            final String rightCleaned = right.replace("*", "");
            String[] parts = left.split("\\*");
            final boolean otherEndsWithWildcard = right.endsWith("*");
            final int otherCleanedLength = rightCleaned.length();
            int i = 0;
            for (String part : parts) {
                if (i == otherCleanedLength && otherEndsWithWildcard) {
                    break;
                }
                if (!part.isEmpty()) {
                    int newIdx = rightCleaned.indexOf(part, i);
                    if (newIdx == -1) {
                        matches = false;
                        break;
                    }
                    i = newIdx + part.length();
                }
            }
            return matches;
        }

        private boolean matchPartsUnsafely(String left, String right) {
            boolean matches = true;
            String[] parts = left.split("\\*");
            int i = 0;
            for (String part : parts) {
                if (!part.isEmpty()) {
                    int newIdx = right.indexOf(part, i);
                    if (newIdx == -1) {
                        matches = false;
                        break;
                    }
                    i = newIdx + part.length();
                }
            }
            return matches;
        }
    }
}
