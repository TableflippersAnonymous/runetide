package com.runetide.common;

import java.util.concurrent.TimeUnit;

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
    public static final int BLOCKS_PER_CHUNK_Y = BLOCKS_PER_CHUNK_SECTION_Y * CHUNK_SECTIONS_PER_CHUNK;

    public static final int BYTES_PER_BLOCK_ID = 2;
    public static final int BYTES_PER_LIGHT = 1;
    public static final int BYTES_PER_COLUMN = 3;
    public static final int S3_UPLOAD_PART_SIZE = 5 * 1024 * 1024;

    public static final String ZK_LOCKS = "/runetide/locks/";
    public static final String ZK_SERVICES = "/runetide/services/";
    public static final String ZK_SVC_INTEREST = "/runetide/svcinterest/";
    public static final String ZK_SVC_LEADERS = "/runetide/svcleaders/";
    public static final String ZK_LEADERS = "/runetide/leaders/";

    public static final String ENTITY_SERVICE_NAME = "entity";
    public static final String ITEM_SERVICE_NAME = "item";
    public static final String LOADER_SERVICE_NAME = "loader";
    public static final String REGION_SERVICE_NAME = "region";
    public static final String RESOURCE_POOL_SERVICE_NAME = "resourcepool";
    public static final String TIME_SERVICE_NAME = "time";

    public static final String ENTITY_LOADING_NAMESPACE = ENTITY_SERVICE_NAME;

    public static final String REGION_BLOBSTORE_NAMESPACE = REGION_SERVICE_NAME;
    public static final String REGION_TOPIC_PREFIX = REGION_SERVICE_NAME + ":";
    public static final String REGION_JOURNAL_PREFIX = REGION_SERVICE_NAME + ":journal:";
    public static final String REGION_LOADING_NAMESPACE = REGION_SERVICE_NAME;

    public static final String TIME_REDIS = "time:clock";
    public static final long TIME_TICK_RATE_MS = 50;
    public static final String TIME_TOPIC = "time:clock";

    public static final long SAVE_RATE_MS = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
}
