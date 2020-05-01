package toxi.geom;

import java.util.List;

/**
 *
 * @author tux
 * @param <T>
 */
public interface SpatialIndex<T> {

    /**
     *
     */
    public void clear();

    /**
     *
     * @param p
     * @return
     */
    public boolean index(T p);

    /**
     *
     * @param item
     * @return
     */
    public boolean isIndexed(T item);

    /**
     *
     * @param p
     * @param radius
     * @param results
     * @return
     */
    public List<T> itemsWithinRadius(T p, float radius, List<T> results);

    /**
     *
     * @param p
     * @param q
     * @return
     */
    public boolean reindex(T p, T q);

    /**
     *
     * @return
     */
    public int size();

    /**
     *
     * @param p
     * @return
     */
    public boolean unindex(T p);

}