package com.runetide.common;

import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.domain.geometry.Vector3L;

import java.util.concurrent.TimeUnit;

public class Constants {
    /* A sector is 1 << 30 blocks, or roughly 1 billion blocks.  This is used to ensure that all generation
     * algorithms that use ints for coordinates (I'm looking at you, OpenSimplex), continue to work.  Generation
     * between sectors will need to be smoothed together, and things like difficulty maps may be disjoint at borders.
     *
     * Given the vast size of sectors, disjoint generation near the edges is, I think, acceptable.  Given the
     * rectilinear alignment, this does have a 4-way meeting of sectors at (0,0), which will likely mean that
     * generation smoothing will definitely need to happen near it, though this could be solved by moving the main
     * starter areas to somewhere closer to (500,000,000; 500,000,000).
     *
     * Something to consider.
     *
     * This value can also be reasonably easily changed if worldgen needs it changed.  Sectors are calculated by taking
     * regional coordinates and dividing, rather than having regional coordinates based off of sectors.
     */
    public static final int REGIONS_PER_SECTOR_X = 1 << 20;
    public static final int REGIONS_PER_SECTOR_Z = 1 << 20;
    public static final long REGIONS_PER_SECTOR = (long) REGIONS_PER_SECTOR_X * REGIONS_PER_SECTOR_Z;
    public static final Vector2L REGIONS_PER_SECTOR_VEC = Vector2L.of(REGIONS_PER_SECTOR_X, REGIONS_PER_SECTOR_Z);

    public static final int CHUNKS_PER_REGION_X = 64;
    public static final int CHUNKS_PER_REGION_Z = 64;
    public static final int CHUNKS_PER_REGION = CHUNKS_PER_REGION_X * CHUNKS_PER_REGION_Z;
    public static final Vector2L CHUNKS_PER_REGION_VEC = Vector2L.of(CHUNKS_PER_REGION_X, CHUNKS_PER_REGION_Z);

    public static final int CHUNK_SECTIONS_PER_CHUNK = 256;
    public static final Vector3L CHUNK_SECTIONS_PER_CHUNK_VEC = Vector3L.of(1, CHUNK_SECTIONS_PER_CHUNK, 1);

    /* These are occasionally bit-packed into bytes.  Other locations in the source depend on these being 16 or less. */
    public static final int BLOCKS_PER_CHUNK_SECTION_X = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION_Y = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION_Z = 16;
    public static final int BLOCKS_PER_CHUNK_SECTION = BLOCKS_PER_CHUNK_SECTION_X * BLOCKS_PER_CHUNK_SECTION_Y
            * BLOCKS_PER_CHUNK_SECTION_Z;
    public static final Vector3L BLOCKS_PER_CHUNK_SECTION_VEC = Vector3L.of(BLOCKS_PER_CHUNK_SECTION_X,
            BLOCKS_PER_CHUNK_SECTION_Y, BLOCKS_PER_CHUNK_SECTION_Z);

    public static final int COLUMNS_PER_CHUNK_X = BLOCKS_PER_CHUNK_SECTION_X;
    public static final int COLUMNS_PER_CHUNK_Z = BLOCKS_PER_CHUNK_SECTION_Z;
    public static final int COLUMNS_PER_CHUNK = COLUMNS_PER_CHUNK_X * COLUMNS_PER_CHUNK_Z;
    public static final Vector2L COLUMNS_PER_CHUNK_VEC = BLOCKS_PER_CHUNK_SECTION_VEC.toVec2D();

    public static final int BLOCKS_PER_CHUNK_Y = BLOCKS_PER_CHUNK_SECTION_Y * CHUNK_SECTIONS_PER_CHUNK;

    public static final int BYTES_PER_BLOCK_ID = 2;
    public static final int BYTES_PER_LIGHT = 1;
    public static final int BYTES_PER_COLUMN = 3;

    /* A Voxel is the smallest renderable resolution of the world.  Block types are made up of
     * 32x32x32 grid of voxels rendered as tiny cubes of solid color.
     */
    public static final int VOXELS_PER_BLOCK_X = 32;
    public static final int VOXELS_PER_BLOCK_Y = 32;
    public static final int VOXELS_PER_BLOCK_Z = 32;
    public static final int VOXELS_PER_BLOCK = VOXELS_PER_BLOCK_X * VOXELS_PER_BLOCK_Y * VOXELS_PER_BLOCK_Z;
    public static final Vector3L VOXELS_PER_BLOCK_VEC = Vector3L.of(VOXELS_PER_BLOCK_X, VOXELS_PER_BLOCK_Y,
            VOXELS_PER_BLOCK_Z);

    /* An Offset is the smallest renderable resolution of an entity.  Offsets are 8-bit unsigned
     * integers specifying a fraction of a block -- thus, they are 1/256th of a block in each dimension.
     * This also means that they are 1/8th of a Voxel in each dimension.
     */
    public static final int OFFSETS_PER_VOXEL_X = 8;
    public static final int OFFSETS_PER_VOXEL_Y = 8;
    public static final int OFFSETS_PER_VOXEL_Z = 8;
    public static final int OFFSETS_PER_VOXEL = OFFSETS_PER_VOXEL_X * OFFSETS_PER_VOXEL_Y * OFFSETS_PER_VOXEL_Z;
    public static final Vector3L OFFSETS_PER_VOXEL_VEC = Vector3L.of(OFFSETS_PER_VOXEL_X, OFFSETS_PER_VOXEL_Y,
            OFFSETS_PER_VOXEL_Z);

    public static final int OFFSETS_PER_BLOCK_X = OFFSETS_PER_VOXEL_X * VOXELS_PER_BLOCK_X;
    public static final int OFFSETS_PER_BLOCK_Y = OFFSETS_PER_VOXEL_Y * VOXELS_PER_BLOCK_Y;
    public static final int OFFSETS_PER_BLOCK_Z = OFFSETS_PER_VOXEL_Z * VOXELS_PER_BLOCK_Z;
    public static final int OFFSETS_PER_BLOCK = OFFSETS_PER_VOXEL * VOXELS_PER_BLOCK;
    public static final Vector3L OFFSETS_PER_BLOCK_VEC = OFFSETS_PER_VOXEL_VEC.scale(VOXELS_PER_BLOCK_VEC);

    public static final int S3_UPLOAD_PART_SIZE = 5 * 1024 * 1024;

    public static final String ZK_LOCKS = "/runetide/locks/";
    public static final String ZK_SERVICES = "/runetide/services/";
    public static final String ZK_SVC_INTEREST = "/runetide/svcinterest/";
    public static final String ZK_SVC_LEADERS = "/runetide/svcleaders/";
    public static final String ZK_LEADERS = "/runetide/leaders/";

    public static final String QUEUE_DELETE_PREFIX = "sulm:";

    public static final String LOCK_MIGRATION = "common:migration";
    public static final String LOCK_SVC_PREFIX = "ulm:";
    public static final String LOCK_DELETE_PREFIX = "sulm:";

    public static final String LOADING_TOPIC_PREFIX = "ulm:";

    public static final String ACCOUNT_SERVICE_NAME = "account";
    public static final String CHARACTER_SERVICE_NAME = "character";
    public static final String ENTITY_SERVICE_NAME = "entity";
    public static final String ITEM_SERVICE_NAME = "item";
    public static final String LOADER_SERVICE_NAME = "loader";
    public static final String MULTIVERSE_SERVICE_NAME = "multiverse";
    public static final String REGION_SERVICE_NAME = "region";
    public static final String RESOURCE_POOL_SERVICE_NAME = "resourcepool";
    public static final String TIME_SERVICE_NAME = "time";
    public static final String WORLD_SERVICE_NAME = "world";
    public static final String WORLDGEN_SERVICE_NAME = "worldgen";
    public static final String XP_SERVICE_NAME = "xp";

    public static final String ACCOUNT_LOADING_NAMESPACE = ACCOUNT_SERVICE_NAME;

    public static final String CHARACTER_LOADING_NAMESPACE = CHARACTER_SERVICE_NAME;

    public static final String ENTITY_LOADING_NAMESPACE = ENTITY_SERVICE_NAME;

    public static final String MULTIVERSE_LOADING_NAMESPACE = MULTIVERSE_SERVICE_NAME;

    public static final String REGION_BLOBSTORE_NAMESPACE = REGION_SERVICE_NAME;
    public static final String REGION_TOPIC_PREFIX = REGION_SERVICE_NAME + ":";
    public static final String REGION_JOURNAL_PREFIX = REGION_SERVICE_NAME + ":journal:";
    public static final String REGION_LOADING_NAMESPACE = REGION_SERVICE_NAME;

    public static final String RESOURCE_POOL_TOPIC_PREFIX = RESOURCE_POOL_SERVICE_NAME + ":";
    public static final String RESOURCE_POOL_LOADING_NAMESPACE = RESOURCE_POOL_SERVICE_NAME;

    public static final String WORLD_LOADING_NAMESPACE = WORLD_SERVICE_NAME;

    public static final String XP_TOPIC_PREFIX = XP_SERVICE_NAME + ":";
    public static final String XP_LOADING_NAMESPACE = XP_SERVICE_NAME;

    public static final String TIME_REDIS = "time:clock";
    public static final long TIME_TICK_RATE_MS = 50;
    public static final String TIME_TOPIC = "time:clock";

    public static final long SAVE_RATE_MS = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
}
