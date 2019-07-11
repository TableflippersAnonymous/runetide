package com.runetide.common.services.blobstore;

import com.runetide.common.Constants;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class S3BlobStore implements BlobStore {
    private final S3Client s3;
    private final String bucket;

    public S3BlobStore(final S3Client s3, final String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @Override
    public InputStream get(final String namespace, final String key) throws IOException {
        return s3.getObject(GetObjectRequest.builder().bucket(bucket).key(namespace + "/" + key).build());
    }

    @Override
    public OutputStream put(final String namespace, final String key) throws IOException {
        final CreateMultipartUploadResponse multipartUploadResponse = s3.createMultipartUpload(CreateMultipartUploadRequest.builder()
                .bucket(bucket).key(namespace + "/" + key).build());
        return new BufferedOutputStream(new OutputStream() {
            private final List<CompletedPart> parts = new ArrayList<>();
            private boolean closed = false;

            @Override
            public void write(final int b) throws IOException {
                write(new byte[] {(byte) b});
            }

            @Override
            public void write(final byte[] b) throws IOException {
                write(b, 0, b.length);
            }

            @Override
            public void write(final byte[] b, final int off, final int len) throws IOException {
                if(closed)
                    throw new IOException("Stream Closed");
                final int partNumber = parts.size() + 1;
                final String etag = s3.uploadPart(UploadPartRequest.builder()
                        .bucket(bucket).key(namespace + "/" + key)
                        .uploadId(multipartUploadResponse.uploadId())
                        .partNumber(partNumber).build(), RequestBody.fromBytes(b)).eTag();
                parts.add(CompletedPart.builder().partNumber(partNumber).eTag(etag).build());
            }

            @Override
            public void close() throws IOException {
                if(closed)
                    return;
                final CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                        .parts(parts).build();
                s3.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                        .bucket(bucket).key(namespace + "/" + key).multipartUpload(completedMultipartUpload).build());
                closed = true;
            }
        }, Constants.S3_UPLOAD_PART_SIZE);
    }

    @Override
    public boolean delete(final String namespace, final String key) throws IOException {
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(namespace + "/" + key).build());
        return true;
    }
}
