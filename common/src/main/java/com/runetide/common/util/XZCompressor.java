package com.runetide.common.util;

import org.eclipse.jetty.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.UnsupportedOptionsException;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Singleton
public class XZCompressor implements Compressor {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LZMA2Options LZMA_OPTIONS = new LZMA2Options();

    @Inject
    public XZCompressor() throws UnsupportedOptionsException {
        LZMA_OPTIONS.setPreset(6);
    }

    @Override
    public byte[] compress(final byte[] uncompressed) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(final XZOutputStream xzOutputStream = new XZOutputStream(outputStream, LZMA_OPTIONS)) {
            xzOutputStream.write(uncompressed);
        } catch (final IOException e) {
            LOG.error("Caught IOException during compression", e);
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    @Override
    public byte[] decompress(final byte[] compressed) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
        try(final XZInputStream xzInputStream = new XZInputStream(inputStream)) {
            return IO.readBytes(xzInputStream);
        } catch (final IOException e) {
            LOG.error("Caught IOException during decompression", e);
            throw new RuntimeException(e);
        }
    }
}
