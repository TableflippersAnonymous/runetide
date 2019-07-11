package com.runetide.common;

public class Constants {
    public static final int CHUNKS_PER_REGION_X = 64;
    public static final int CHUNKS_PER_REGION_Z = 64;
    public static final int CHUNKS_PER_REGION = CHUNKS_PER_REGION_X * CHUNKS_PER_REGION_Z;

    public static final int CHUNK_SECTIONS_PER_CHUNK = 256;

    public static final int BLOCKS_PER_CHUNK_SECTION_X = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION_Y = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION_Z = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION = BLOCKS_PER_CHUNK_SECTION_X * BLOCKS_PER_CHUNK_SECTION_Y
            * BLOCKS_PER_CHUNK_SECTION_Z;
    public static final int COLUMNS_PER_CHUNK_X = BLOCKS_PER_CHUNK_SECTION_X;
    public static final int COLUMNS_PER_CHUNK_Z = BLOCKS_PER_CHUNK_SECTION_Z;
    public static final int COLUMNS_PER_CHUNK = COLUMNS_PER_CHUNK_X * COLUMNS_PER_CHUNK_Z;

    public static final int BYTES_PER_BLOCK_ID = 2;
    public static final int BYTES_PER_LIGHT = 1;
    public static final int BYTES_PER_COLUMN = 3;
    public static final int S3_UPLOAD_PART_SIZE = 5 * 1024 * 1024;
    public static final String REGION_BLOBSTORE_NAMESPACE = "region";
}
