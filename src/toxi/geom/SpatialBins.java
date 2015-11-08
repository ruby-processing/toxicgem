package toxi.geom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import toxi.math.MathUtils;

/**
 *
 * @author tux
 * @param <T>
 */
public class SpatialBins<T> implements SpatialIndex<T> {

    private final float invBinWidth;
    private final float minOffset;
    private final int numBins;
    private int numItems;

    private final List<HashSet<T>> bins;
    private final CoordinateExtractor<T> extractor;

    /**
     *
     * @param min
     * @param max
     * @param numBins
     * @param extractor
     */
    public SpatialBins(float min, float max, int numBins,
            CoordinateExtractor<T> extractor) {
        this.extractor = extractor;
        this.bins = new ArrayList<HashSet<T>>();
        for (int i = 0; i < numBins; i++) {
            bins.add(new HashSet<T>());
        }
        this.minOffset = min;
        this.numBins = numBins;
        this.invBinWidth = numBins / (max - min);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#clear()
     */

    /**
     *
     */
    
    public void clear() {
        for (HashSet<T> bin : bins) {
            bin.clear();
        }
        numItems = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#index(T)
     */

    /**
     *
     * @param p
     * @return
     */
    
    public boolean index(T p) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins - 1);
        if (bins.get(id).add(p)) {
            numItems++;
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#isIndexed(T)
     */

    /**
     *
     * @param item
     * @return
     */
    
    public boolean isIndexed(T item) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     *
     * @param p
     * @param radius
     * @param results
     * @return
     */
    public List<T> itemsWithinRadius(T p, float radius, List<T> results) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        int tol = (int) Math.ceil(radius * invBinWidth);
        for (int i = Math.max(id - tol, 0), n = Math.min(
                Math.min(id + tol, numBins), numBins - 1); i <= n; i++) {
            if (results == null) {
                results = new ArrayList<T>();
            }
            results.addAll(bins.get(i));
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#reindex(float, T)
     */

    /**
     *
     * @param p
     * @param q
     * @return
     */
    
    public boolean reindex(T p, T q) {
        int id1 = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        int id2 = (int) MathUtils.clip((extractor.coordinate(q) - minOffset)
                * invBinWidth, 0, numBins);
        if (id1 != id2) {
            if (bins.get(id1).remove(p)) {
                numItems--;
            }
            if (bins.get(id2).add(q)) {
                numItems++;
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#size()
     */

    /**
     *
     * @return
     */
    
    public int size() {
        return numItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.SpatialIndex#unindex(T)
     */

    /**
     *
     * @param p
     * @return
     */
    
    public boolean unindex(T p) {
        int id = (int) MathUtils.clip((extractor.coordinate(p) - minOffset)
                * invBinWidth, 0, numBins);
        return bins.get(id).remove(p);
    }
}
