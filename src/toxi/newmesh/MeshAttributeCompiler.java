package toxi.newmesh;

import toxi.util.datatypes.ItemIndex;

/**
 *
 * @author tux
 */
public abstract class MeshAttributeCompiler {

    /**
     *
     */
    protected IndexedTriangleMesh mesh;

    /**
     *
     * @param f
     * @param index
     * @param buf
     * @param offset
     */
    public abstract void compileFace(AttributedFace f, ItemIndex<?> index,
            float[] buf, int offset);

    /**
     *
     * @return
     */
    public abstract ItemIndex<?> getIndex();

    /**
     *
     * @return
     */
    public abstract int getStride();

    /**
     *
     * @param mesh
     */
    public void setMesh(IndexedTriangleMesh mesh) {
        this.mesh = mesh;
    }
}
