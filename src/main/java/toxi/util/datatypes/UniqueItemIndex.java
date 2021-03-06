package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to build an unique set of items and offers a
 * bi-directional mapping between items and their associated ID values. Items
 * are added via the {@link #index(Object)} method and removed via
 * {@link #unindex(Object)}. The item's {@link #hashCode()} is used as unique
 * identifier, so you MUST ensure the item class satisfies the contract of
 * {@link #hashCode()} and {@link #equals(Object)}.
 * @param <T>
 */
public class UniqueItemIndex<T> implements ItemIndex<T> {

    /**
     *
     */
    protected final HashMap<T, Integer> uniqueItems = new HashMap<>();

    /**
     *
     */
    protected final ArrayList<T> index = new ArrayList<>();

    /**
     *
     */
    public UniqueItemIndex() {
    }

    /**
     *
     * @param items
     */
    public UniqueItemIndex(Collection<T> items) {
        for (T item : items) {
            index(item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#clear()
     */

    /**
     *
     */
    @Override
    public void clear() {
        uniqueItems.clear();
        index.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#forID(int)
     */

    /**
     *
     * @param id
     * @return
     */
    
    @Override
    public T forID(int id) {
        return index.get(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#getID(T)
     */

    /**
     *
     * @param item
     * @return
     */
    
    @Override
    public int getID(T item) {
        Integer id = uniqueItems.get(item);
        return (id != null) ? id : -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#getItems()
     */

    /**
     *
     * @return
     */
    
    @Override
    public List<T> getItems() {
        return index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#index(T)
     */

    /**
     *
     * @param item
     * @return
     */
    
    @Override
    public int index(T item) {
        Integer id = uniqueItems.get(item);
        if (id == null) {
            id = index.size();
            uniqueItems.put(item, id);
            index.add(item);
        }
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#isIndexed(T)
     */

    /**
     *
     * @param item
     * @return
     */
    
    @Override
    public boolean isIndexed(T item) {
        return uniqueItems.get(item) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#reindex(T, T)
     */

    /**
     *
     * @param item
     * @param newItem
     * @return
     */
    
    @Override
    public int reindex(T item, T newItem) {
        int id = getID(item);
        if (id != -1) {
            int newID = getID(newItem);
            if (newID != -1) {
                unindex(item);
                id = getID(newItem);
            } else {
                index.set(id, newItem);
                uniqueItems.remove(item);
                uniqueItems.put(newItem, id);
            }
        }
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#size()
     */

    /**
     *
     * @return
     */
    
    @Override
    public int size() {
        return index.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.util.ItemIndex#unindex(T)
     */

    /**
     *
     * @param item
     * @return
     */
    
    @Override
    public int unindex(T item) {
        Integer id = uniqueItems.get(item);
        if (id != null) {
            uniqueItems.remove(item);
            index.remove(item);
            for (int i = id, num = index.size(); i < num; i++) {
                uniqueItems.put(index.get(i), i);
            }
        } else {
            id = -1;
        }
        return id;
    }
}
