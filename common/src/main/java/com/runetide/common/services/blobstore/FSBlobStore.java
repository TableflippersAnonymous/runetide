package com.runetide.common.services.blobstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FSBlobStore implements BlobStore {
    private final String baseDir;

    public FSBlobStore(final String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public InputStream get(final String namespace, final String key) throws IOException {
        final File file = new File(new File(baseDir, namespace), key);
        if(!file.exists())
            throw new FileNotFoundException();
        return new FileInputStream(file);
    }

    @Override
    public OutputStream put(final String namespace, final String key) throws IOException {
        final File dir = new File(baseDir, namespace);
        dir.mkdirs();
        final File file = new File(dir, key);
        return new FileOutputStream(file);
    }

    @Override
    public boolean delete(final String namespace, final String key) throws IOException {
        final File file = new File(new File(baseDir, namespace), key);
        return file.delete();
    }
}
