package com.runetide.common.services.blobstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface BlobStore {
    InputStream get(final String namespace, final String key) throws IOException;
    OutputStream put(final String namespace, final String key) throws IOException;
    boolean delete(final String namespace, final String key) throws IOException;
}
