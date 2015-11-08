package toxi.geom;

/**
 *
 * @author tux
 * @param <T>
 */
public interface CoordinateExtractor<T> {

    /**
     *
     * @param obj
     * @return
     */
    public float coordinate(T obj);
}
