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

package org.turbogwt.core.http.client;

/**
 * HTTP Header with relative quality factors.
 *
 * @author Danilo Reinert
 */
public class QualityFactorHeader extends MultipleHeader {

    public class Value {

        private final double factor;
        private final String value;

        public Value(double factor, String value) throws IllegalArgumentException {
            if (factor > 1 || factor < 0)
                throw new IllegalArgumentException("Factor must be between 0 and 1.");
            this.factor = factor;
            this.value = value;
        }

        public double getFactor() {
            return factor;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            if (factor == 1) {
                return value;
            }
            return value + "; " + factor;
        }
    }

    private final Value[] values;

    public QualityFactorHeader(String name, Value... values) {
        super(name, (Object[]) values);
        this.values = values;
    }

    public Value[] getQualityFactorValues() {
        return values;
    }
}
