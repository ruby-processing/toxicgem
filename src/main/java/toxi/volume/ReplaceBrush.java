package toxi.volume;

/**
 *
 * @author tux
 */
public class ReplaceBrush implements BrushMode {

    /**
     *
     * @param orig
     * @param brush
     * @return
     */
    @Override
    public final float apply(float orig, float brush) {
        return brush;
    }
}
