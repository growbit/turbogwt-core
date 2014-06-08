/*
 * Copyright 2009 Thomas Broyer
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract implementation of {@link Promise}.
 *
 * @param <D>
 *     The type of the result received when the promise is done
 * @param <F>
 *     The type of the result received when the promise failed
 */
public abstract class AbstractPromise<D, F> implements Promise<D, F> {
    protected final Logger log = Logger.getLogger(String.valueOf(AbstractPromise.class));

    protected volatile State state = State.PENDING;

    protected final List<DoneCallback<D>> doneCallbacks = new ArrayList<DoneCallback<D>>();
    protected final List<FailCallback<F>> failCallbacks = new ArrayList<FailCallback<F>>();
    protected final List<AlwaysCallback<D, F>> alwaysCallbacks = new ArrayList<AlwaysCallback<D, F>>();

    protected D resolveResult;
    protected F rejectResult;

    @Override
    public State state() {
        return state;
    }

    @Override
    public Promise<D, F> done(DoneCallback<D> callback) {
        synchronized (this) {
            doneCallbacks.add(callback);
            if (isResolved()) triggerDone(callback, resolveResult);
        }
        return this;
    }

    @Override
    public Promise<D, F> fail(FailCallback<F> callback) {
        synchronized (this) {
            failCallbacks.add(callback);
            if (isRejected()) triggerFail(callback, rejectResult);
        }
        return this;
    }

    @Override
    public Promise<D, F> always(AlwaysCallback<D, F> callback) {
        synchronized (this) {
            alwaysCallbacks.add(callback);
            if (!isPending()) triggerAlways(callback, state, resolveResult, rejectResult);
        }
        return this;
    }

    protected void triggerDone(D resolved) {
        for (DoneCallback<D> callback : doneCallbacks) {
            try {
                triggerDone(callback, resolved);
            } catch (Exception e) {
                log.log(Level.SEVERE, "an uncaught exception occured in a DoneCallback", e);
            }
        }
    }

    protected void triggerDone(DoneCallback<D> callback, D resolved) {
        callback.onDone(resolved);
    }

    protected void triggerFail(F rejected) {
        for (FailCallback<F> callback : failCallbacks) {
            try {
                triggerFail(callback, rejected);
            } catch (Exception e) {
                log.log(Level.SEVERE, "an uncaught exception occured in a FailCallback", e);
            }
        }
    }

    protected void triggerFail(FailCallback<F> callback, F rejected) {
        callback.onFail(rejected);
    }

    protected void triggerAlways(State state, D resolve, F reject) {
        for (AlwaysCallback<D, F> callback : alwaysCallbacks) {
            try {
                triggerAlways(callback, state, resolve, reject);
            } catch (Exception e) {
                log.log(Level.SEVERE, "an uncaught exception occured in a AlwaysCallback", e);
            }
        }

        synchronized (this) {
            this.notifyAll();
        }
    }

    protected void triggerAlways(AlwaysCallback<D, F> callback, State state, D resolve, F reject) {
        callback.onAlways(state, resolve, reject);
    }

    @Override
    public boolean isPending() {
        return state == State.PENDING;
    }

    @Override
    public boolean isResolved() {
        return state == State.RESOLVED;
    }

    @Override
    public boolean isRejected() {
        return state == State.REJECTED;
    }
}
