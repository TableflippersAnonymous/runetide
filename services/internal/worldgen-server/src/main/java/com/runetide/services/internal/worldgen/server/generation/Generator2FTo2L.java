package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.dto.ContainerRef;

public class Generator2FTo2L<PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        implements Generator2<PointType, long[]> {
    private final Generator2<PointType, double[]> parent;
    private final boolean scaling;
    private final double fromLow;
    private final double fromHigh;
    private final long toLow;
    private final long toHigh;

    public Generator2FTo2L(final Generator2<PointType, double[]> parent, final double fromLow, final double fromHigh,
                           final long toLow, final long toHigh) {
        this.parent = parent;
        this.scaling = true;
        this.fromLow = fromLow;
        this.fromHigh = fromHigh;
        this.toLow = toLow;
        this.toHigh = toHigh;
    }

    public Generator2FTo2L(final Generator2<PointType, double[]> parent) {
        this.parent = parent;
        this.scaling = false;
        this.fromLow = 0;
        this.fromHigh = 0;
        this.toLow = 0;
        this.toHigh = 0;
    }

    @Override
    public long[][] generate(final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox) {
        final Vector2L dimensions = boundingBox.getDimensions();
        final int xSize = dimensions.getX().intValue();
        final int zSize = dimensions.getZ().intValue();
        final long[][] ret = new long[xSize][zSize];
        final double[][] convert = parent.generate(boundingBox);
        /* This check could be moved into the loop body, but it saves very few additional lines of code to do so, and
         * potentially causes worse performance.
         */
        if(scaling) {
            final double fromScale = fromHigh - fromLow;
            final long toScale = toHigh - toLow;
            final double scale = toScale / fromScale;
            final double offset = toLow - fromLow * scale;
            for (int x = 0; x < xSize; x++)
                for (int z = 0; z < zSize; z++) {
                    final long val = (long) (convert[x][z] * scale + offset);
                    /* This could be ret[x][z] = Math.min(Math.max(val, toLow), toHigh), but Math.max(long, long) and
                     * Math.min(long, long) are not @IntrinsicCandidates (though the int variants are).  Given that
                     * this is in a fairly tight performance loop, the direct solution may have performance benefits.
                     */
                    //noinspection ManualMinMaxCalculation
                    ret[x][z] = val > toHigh ? toHigh : (val < toLow ? toLow : val);
                }
        } else
            for (int x = 0; x < xSize; x++)
                for (int z = 0; z < zSize; z++)
                    ret[x][z] = (long) convert[x][z];
        return ret;
    }
}
