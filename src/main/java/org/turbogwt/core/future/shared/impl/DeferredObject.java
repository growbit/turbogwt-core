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

import org.turbogwt.core.future.shared.Deferred;
import org.turbogwt.core.future.shared.Promise;

/**
 * An implementation of {@link org.turbogwt.core.future.shared.Deferred} interface.
 *
 * <pre>
 * <code>
 * final {@link org.turbogwt.core.future.shared.Deferred} deferredObject = new {@link DeferredObject}
 *
 * {@link org.turbogwt.core.future.shared.Promise} promise = deferredObject.promise();
 * promise
 *   .done(new DoneCallback() { ... })
 *   .fail(new FailCallback() { ... })
 *   .progress(new ProgressCallback() { ... });
 *
 * deferredObject.notify(someNotificationObject);
 * deferredObject.resolve(someSuccessObject);
 * deferredObject.reject(someFailObject);
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
 *
 * @author Ray Tsang
 */
public class DeferredObject<D, F, P> extends AbstractPromise<D, F, P>
        implements Deferred<D, F, P> {

    @Override
    public Deferred<D, F, P> resolve(final D resolve) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot resolve again");

            state = State.RESOLVED;
            resolveResult = resolve;

            try {
                triggerDone(resolve);
            } finally {
                triggerAlways(resolve, null);
            }
        }
        return this;
    }

    @Override
    public Deferred<D, F, P> notify(final P progress) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot notify progress");

            triggerProgress(progress);
        }
        return this;
    }

    @Override
    public Deferred<D, F, P> reject(final F reject) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot reject again");

            state = State.REJECTED;
            rejectResult = reject;

            try {
                triggerFail(reject);
            } finally {
                triggerAlways(null, reject);
            }
        }
        return this;
    }

    public Promise<D, F, P> promise() {
        return this;
    }
}
