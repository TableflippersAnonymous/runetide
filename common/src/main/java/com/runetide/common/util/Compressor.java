package com.runetide.common.util;

public interface Compressor {
    byte[] compress(byte[] uncompressed);
    byte[] decompress(byte[] compressed);
}
