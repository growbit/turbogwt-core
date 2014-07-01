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

package org.turbogwt.core.future.shared.impl;

import org.turbogwt.core.future.shared.Context;
import org.turbogwt.core.future.shared.Deferred;
import org.turbogwt.core.future.shared.Promise;

/**
 * An implementation of {@link org.turbogwt.core.future.shared.Deferred} interface.
 *
 * <pre>
 * <code>
 * final {@link org.turbogwt.core.future.shared.Deferred} deferredObject = new {@link AbstractDeferred}
 *
 * {@link org.turbogwt.core.future.shared.Promise} promise = deferredObject.promise();
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
 * @see org.turbogwt.core.future.shared.DoneCallback
 * @see org.turbogwt.core.future.shared.FailCallback
 *
 * @param <D> Type of DoneCallback
 * @param <F> Type of FailCallback
 * @param <P> Type of ProgressCallback
 * @param <C> Type of Context
 *
 * @author Ray Tsang
 */
public abstract class AbstractDeferred<D, F, P, C extends Context> extends AbstractPromise<D, F, P, C>
        implements Deferred<D, F, P, C> {

    @Override
    public Deferred<D, F, P, C> resolve(final D resolve) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot resolve again");

            this.state = State.RESOLVED;
            this.resolveResult = resolve;

            try {
                triggerDone(resolve);
            } finally {
                triggerAlways(getContext(), resolve, null);
            }
        }
        return this;
    }

    @Override
    public Deferred<D, F, P, C> notify(final P progress) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot notify progress");

            triggerProgress(progress);
        }
        return this;
    }

    @Override
    public Deferred<D, F, P, C> reject(final F reject) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot reject again");
            this.state = State.REJECTED;
            this.rejectResult = reject;

            try {
                triggerFail(reject);
            } finally {
                triggerAlways(getContext(), null, reject);
            }
        }
        return this;
    }

    public Promise<D, F, P, C> promise() {
        return this;
    }
}
