package toxi.util.datatypes;

import java.util.List;

/**
 *
 * @author tux
 * @param <T>
 */
public interface ItemIndex<T> {

    /**
     *
     */
    public void clear();

    /**
     *
     * @param id
     * @return
     */
    public T forID(int id);

    /**
     *
     * @param item
     * @return
     */
    public int getID(T item);

    /**
     *
     * @return
     */
    public List<T> getItems();

    /**
     *
     * @param item
     * @return
     */
    public int index(T item);

    /**
     *
     * @param item
     * @return
     */
    public boolean isIndexed(T item);

    /**
     *
     * @param item
     * @param newItem
     * @return
     */
    public int reindex(T item, T newItem);

    /**
     *
     * @return
     */
    public int size();

    /**
     *
     * @param item
     * @return
     */
    public int unindex(T item);

}