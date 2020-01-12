# Region Service
The region service is responsible for storing and manipulating world block data.

Each region is separated into chunks (`Constants.CHUNKS_PER_REGION_X`, `Constants.CHUNKS_PER_REGION_Z`), which is a
vertical stack of chunk sections (`Constants.CHUNK_SECTIONS_PER_CHUNK`).  A chunk section is a small, manageable
grouping of blocks (`Constants.BLOCKS_PER_CHUNK_SECTION_X`, `Constants.BLOCKS_PER_CHUNK_SECTION_Y`,
`Constants.BLOCKS_PER_CHUNK_SECTION_Z`).

Due to consistency concerns as well as the sheer size of the data involved in a region, regions are loaded onto a
region server before they are manipulated, and they can only be loaded on one region server at any given time.
(See `UniqueLoadingManager`)

For performance reasons, regions remain loaded while they are at all relevant.

The region service holds these regions in memory (either compressed or uncompressed as necessary) to provide quick
access to these regions.  When a region is unloaded, it is serialized out to permanent storage (`BlobStore`) in a
binary, compressed format.

Due to the volatility of memory, as well as crash potential, the region service also journals changes to regions to
redis so it can be replayed in the event of an unclean unload.