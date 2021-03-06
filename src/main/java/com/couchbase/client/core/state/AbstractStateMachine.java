/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.couchbase.client.core.state;

import com.couchbase.client.core.logging.CouchbaseLogger;
import com.couchbase.client.core.logging.CouchbaseLoggerFactory;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Abstract {@link Stateful} implementation which acts like a simple state machine.
 *
 * This class is thread safe, so state transitions can be issued from any thread without any further synchronization.
 *
 * @author Michael Nitschinger
 * @since 1.0
 */
public class AbstractStateMachine<S extends Enum> implements Stateful<S> {

    /**
     * The logger used.
     */
    private static final CouchbaseLogger LOGGER = CouchbaseLoggerFactory.getInstance(Stateful.class);

    /**
     * The observable which emits all the subsequent state changes.
     */
    private final Subject<S, S> observable;

    /**
     * The current state of the state machine.
     */
    private volatile S currentState;

    /**
     * Creates a new state machine.
     *
     * @param initialState the initial state of the state machine.
     */
    protected AbstractStateMachine(final S initialState) {
        currentState = initialState;
        observable = BehaviorSubject.create(currentState).toSerialized();
    }

    @Override
    public final Observable<S> states() {
        return observable;
    }

    @Override
    public final S state() {
        return currentState;
    }

    @Override
    public final boolean isState(final S state) {
        return currentState == state;
    }

    @Override
    public boolean hasSubscribers() {
        return observable.hasObservers();
    }

    /**
     * Transition into a new state.
     *
     * This method is intentionally not public, because the subclass should only be responsible for the actual
     * transitions, the other components only react on those transitions eventually.
     *
     * @param newState the states to transition into.
     */
    protected void transitionState(final S newState) {
        if (newState != currentState) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("State (" + getClass().getSimpleName() + ") " + currentState + " -> " + newState);
            }
            currentState = newState;
            observable.onNext(newState);
        }
    }

}
