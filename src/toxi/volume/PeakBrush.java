package toxi.volume;

import toxi.math.MathUtils;

/**
 *
 * @author tux
 */
public class PeakBrush implements BrushMode {

    /**
     *
     * @param orig
     * @param brush
     * @return
     */
    @Override
    public final float apply(float orig, float brush) {
        return MathUtils.max(orig, brush);
    }
}
