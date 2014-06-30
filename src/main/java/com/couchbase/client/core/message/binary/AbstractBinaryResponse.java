package com.couchbase.client.core.message.binary;

import com.couchbase.client.core.message.AbstractCouchbaseResponse;
import com.couchbase.client.core.message.CouchbaseRequest;
import com.couchbase.client.core.message.ResponseStatus;
import io.netty.buffer.ByteBuf;

/**
 * Created by michael on 22/05/14.
 */
public abstract class AbstractBinaryResponse extends AbstractCouchbaseResponse implements BinaryResponse {

    private final ByteBuf content;
    private final String bucket;

    protected AbstractBinaryResponse(ResponseStatus status, String bucket, ByteBuf content, CouchbaseRequest request) {
        super(status, request);
        this.content = content;
        this.bucket = bucket;
    }

    @Override
    public ByteBuf content() {
        return content;
    }

    @Override
    public String bucket() {
        return bucket;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BinaryResponse{");
        sb.append("bucket='").append(bucket).append('\'');
        sb.append(", status=").append(status());
        sb.append(", request=").append(request());
        sb.append(", content=").append(content);
        sb.append('}');
        return sb.toString();
    }
}