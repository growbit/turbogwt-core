/*
 * Copyright 2013 Ray Tsang
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

package org.turbogwt.core.future.shared;

/**
 * An implementation of {@link Deferred} interface.
 *
 * <pre>
 * <code>
 * final {@link Deferred} deferredObject = new {@link DeferredObject}
 *
 * {@link Promise} promise = deferredObject.promise();
 * promise
 *   .done(new DoneCallback() { ... })
 *   .fail(new FailCallback() { ... })
 *   .progress(new ProgressCallback() { ... });
 *
 * {@link Runnable} runnable = new {@link Runnable}() {
 *   public void run() {
 *     int sum = 0;
 *     for (int i = 0; i < 100; i++) {
 *       // something that takes time
 *       sum += i;
 *       deferredObject.notify(i);
 *     }
 *     deferredObject.resolve(sum);
 *   }
 * }
 * // submit the task to run
 *
 * </code>
 * </pre>
 *
 * @see DoneCallback
 * @see FailCallback
 * @author Ray Tsang
 */
public class DeferredObject<D, F> extends AbstractPromise<D, F> implements Deferred<D, F> {

    @Override
    public Deferred<D, F> resolve(final D resolve) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot resolve again");

            this.state = State.RESOLVED;
            this.resolveResult = resolve;

            try {
                triggerDone(resolve);
            } finally {
                triggerAlways(state, resolve, null);
            }
        }
        return this;
    }

    @Override
    public Deferred<D, F> reject(final F reject) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot reject again");
            this.state = State.REJECTED;
            this.rejectResult = reject;

            try {
                triggerFail(reject);
            } finally {
                triggerAlways(state, null, reject);
            }
        }
        return this;
    }

    public Promise<D, F> promise() {
        return this;
    }
}
