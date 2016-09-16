package com.couchbase.client.core.metrics;

import com.couchbase.client.core.endpoint.AbstractEndpoint;
import com.couchbase.client.core.message.CouchbaseRequest;

/**
 * A listener that will receive internal performance events.
 *
 * @author jlopez
 */
public interface PerformanceListener {
	void onEnqueueSend(CouchbaseRequest request);
	void onDequeueSend(CouchbaseRequest request, long backlog);
	void onEndpointEncode(AbstractEndpoint endpoint);
	void onEndpointDecode(AbstractEndpoint endpoint, int backlog, long delay);
}
