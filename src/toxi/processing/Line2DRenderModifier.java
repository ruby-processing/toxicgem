package toxi.processing;

import toxi.geom.ReadonlyVec2D;

/**
 *
 * @author tux
 */
public interface Line2DRenderModifier {

    /**
     *
     * @param gfx
     * @param a
     * @param b
     */
    public void apply(ToxiclibsSupport gfx, ReadonlyVec2D a, ReadonlyVec2D b);
}
