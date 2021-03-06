package toxi.newmesh;

import toxi.geom.Vec3D;
import toxi.util.datatypes.UniqueItemIndex;

/**
 *
 * @author tux
 */
public class SpatialIndex extends UniqueItemIndex<Vec3D> {

    public float delta,

    /**
     *
     */

    /**
     *
     */
    deltaSq;

    /**
     *
     * @param delta
     */
    public SpatialIndex(float delta) {
        setDelta(delta);
    }

    private int getClosestIndexed(Vec3D item) {
        int c = -1;
        float minD = deltaSq;
        for (int i = 0, num = index.size(); i < num; i++) {
            float d = index.get(i).distanceToSquared(item);
            if (d < minD) {
                minD = d;
                c = i;
            }
        }
        return c;
    }

    /**
     *
     * @return
     */
    public float getDelta() {
        return delta;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public int index(Vec3D item) {
        int id = getID(item);
        if (id == -1) {
            id = getClosestIndexed(item);
        }
        if (id != -1) {
            return id;
        } else {
            return super.index(item);
        }
    }

    /**
     *
     * @param delta
     */
    public final void setDelta(float delta) {
        this.delta = delta;
        this.deltaSq = delta * delta;
    }
}
