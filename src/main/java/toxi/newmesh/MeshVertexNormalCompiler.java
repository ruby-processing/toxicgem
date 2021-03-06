package toxi.newmesh;

import toxi.geom.Vec3D;
import toxi.util.datatypes.ItemIndex;

/**
 *
 * @author tux
 */
public class MeshVertexNormalCompiler extends MeshAttributeCompiler {

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
        int[] vn = f.attribs.get(IndexedTriangleMesh.ATTR_VNORMALS);
        Vec3D v = (Vec3D) index.forID(vn[0]);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(vn[1]);
        buf[offset++] = v.x;
        buf[offset++] = v.y;
        buf[offset++] = v.z;
        v = (Vec3D) index.forID(vn[2]);
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
        return mesh.attributes.get(IndexedTriangleMesh.ATTR_VNORMALS);
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
