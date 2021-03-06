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
package com.couchbase.client.core.retry;

import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.core.ResponseEvent;
import com.couchbase.client.core.ResponseHandler;
import com.couchbase.client.core.env.CoreEnvironment;
import com.couchbase.client.core.message.CouchbaseRequest;
import com.lmax.disruptor.RingBuffer;

/**
 * A helper class which contains methods related to retrying operations.
 *
 * @author Michael Nitschinger
 * @since 1.1.0
 */
public class RetryHelper {

    /**
     * Either retry or cancel a request, based on the strategy used.
     *
     * @param environment the core environment for context.
     * @param request the request to either retry or cancel.
     * @param responseBuffer the response buffer where to maybe retry on.
     */
    public static void retryOrCancel(final CoreEnvironment environment, final CouchbaseRequest request,
        final RingBuffer<ResponseEvent> responseBuffer) {
        if (environment.retryStrategy().shouldRetry(request, environment)) {
            responseBuffer.publishEvent(ResponseHandler.RESPONSE_TRANSLATOR, request, request.observable());
        } else {
            request.observable().onError(new RequestCancelledException("Could not dispatch request, cancelling "
                + "instead of retrying."));
        }
    }
}
