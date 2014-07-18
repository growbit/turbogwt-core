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
 * Promise interface to observe when some action has occurred on the corresponding {@link Deferred} object.
 *
 * A promise object should be obtained from {@link Deferred#promise()), or
 * by using DeferredManager.
 *
 * <pre>
 * <code>
 * Deferred deferredObject = new DeferredObject();
 * Promise promise = deferredObject.promise();
 * promise.done(new DoneCallback() {
 *   void onDone(Object result) {
 *       // Done!
 *   }
 * });
 *
 * // another thread using the same deferredObject
 * deferredObject.resolve("OK");
 *
 * </code>
 * </pre>
 *
 * @see Deferred#resolve(Object)
 * @see Deferred#reject(Object)
 * @see Deferred#notify(Object)
 *
 * @author Ray Tsang
 * @author Danilo Reinert
 *
 * @param <D>
 *            Type used for {@link #done(DoneCallback)}
 * @param <F>
 *            Type used for {@link #fail(FailCallback)}
 * @param <P>
 *            The type of the progress notification
 */
public interface Promise<D, F, P> {

    /**
     * State of a Promise
     */
    public enum State {
        /**
         * The Promise is still pending - it could be created, submitted for execution,
         * or currently running, but not yet finished.
         */
        PENDING,

        /**
         * The Promise has finished running and a failure occurred.
         * Thus, the Promise is rejected.
         *
         * @see Deferred#reject(Object)
         */
        REJECTED,

        /**
         * The Promise has finished running successfully.
         * Thus the Promise is resolved.
         *
         * @see Deferred#resolve(Object)
         */
        RESOLVED
    }

    State state();

    /**
     * @see State#PENDING
     * @return
     */
    boolean isPending();

    /**
     * @see State#RESOLVED
     * @return
     */
    boolean isResolved();

    /**
     * @see State#REJECTED
     * @return
     */
    boolean isRejected();

    /**
     * This method will register {@link DoneCallback} so that when a Deferred object
     * is resolved ({@link Deferred#resolve(Object)}), {@link DoneCallback} will be triggered.
     *
     * You can register multiple {@link DoneCallback} by calling the method multiple times.
     * The order of callback trigger is based on the order you call this method.
     *
     * <pre>
     * <code>
     * promise.progress(new DoneCallback(){
     *   void onDone(Object done) {
     *     ...
     *   }
     * });
     * </code>
     * </pre>
     *
     * @see Deferred#resolve(Object)
     * @param callback
     * @return
     */
    Promise<D, F, P> done(DoneCallback<D> callback);

    /**
     * This method will register {@link FailCallback} so that when a Deferred object
     * is rejected ({@link Deferred#reject(Object)}), {@link FailCallback} will be triggered.
     *
     * You can register multiple {@link FailCallback} by calling the method multiple times.
     * The order of callback trigger is based on the order you call this method.
     *
     * <pre>
     * <code>
     * promise.fail(new FaillCallback(){
     *   void onFail(Object rejection) {
     *     ...
     *   }
     * });
     * </code>
     * </pre>
     *
     * @see Deferred#reject(Object)
     * @param callback
     * @return
     */
    Promise<D, F, P> fail(FailCallback<F> callback);

    /**
     * This method will register {@link AlwaysCallback} so that when it's always triggered
     * regardless of whether the corresponding Deferred object was resolved or rejected.
     *
     * You can register multiple {@link AlwaysCallback} by calling the method multiple times.
     * The order of callback trigger is based on the order you call this method.
     *
     * <pre>
     * <code>
     * promise.always(new AlwaysCallback(){
     *   void onAlways(Context context, Object result, Object rejection) {
     *     if (context.getState() == State.RESOLVED) {
     *       // do something w/ result
     *     } else {
     *       // do something w/ rejection
     *     }
     *   }
     * });
     * </code>
     * </pre>
     *
     * @see Deferred#resolve(Object)
     * @see Deferred#reject(Object)
     * @param callback
     * @return
     */
     Promise<D, F, P> always(AlwaysCallback<D, F> callback);

    /**
     * This method will register {@link ProgressCallback} so that when a Deferred object
     * is notified of progress ({@link Deferred#notify(Object)}), {@link ProgressCallback} will be triggered.
     *
     * You can register multiple {@link ProgressCallback} by calling the method multiple times.
     * The order of callback trigger is based on the order you call this method.
     *
     * <pre>
     * <code>
     * promise.progress(new ProgressCallback(){
     *   public void onProgress(Object progress) {
     *     // e.g., update progress in the GUI while the background task is still running.
     *   }
     * });
     * </code>
     * </pre>
     *
     * @see Deferred#notify(Object)
     * @param callback
     * @return
     */
    Promise<D, F, P> progress(ProgressCallback<P> callback);

}
