package toxi.newmesh;

import toxi.geom.Vec3D;
import toxi.util.datatypes.ItemIndex;

/**
 *
 * @author tux
 */
public class MeshVertexCompiler extends MeshAttributeCompiler {

    /**
     *
     * @param f
     * @param index
     * @param buf
     * @param offset
     */
    @Override
    public void compileFace(AttributedFace f, ItemIndex<?> index, float[] buf,
            int offset) {
        Vec3D v = (Vec3D) index.forID(f.a);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(f.b);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(f.c);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
    }

    /**
     *
     * @return
     */
    @Override
    public ItemIndex<?> getIndex() {
        return mesh.vertices;
    }

    /**
     *
     * @return
     */
    @Override
    public int getStride() {
        return 3;
    }

}
