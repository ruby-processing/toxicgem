package toxi.newmesh;

import toxi.color.ReadonlyTColor;
import toxi.util.datatypes.ItemIndex;

/**
 *
 * @author tux
 */
public class MeshVertexColorCompiler extends MeshAttributeCompiler {

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
        int[] vn = f.attribs.get(IndexedTriangleMesh.ATTR_VCOLORS);
        ReadonlyTColor c = (ReadonlyTColor) index.forID(vn[0]);
        c.toRGBAArray(buf, offset);
        c = (ReadonlyTColor) index.forID(vn[1]);
        c.toRGBAArray(buf, offset + 4);
        c = (ReadonlyTColor) index.forID(vn[2]);
        c.toRGBAArray(buf, offset + 8);
    }

    /**
     *
     * @return
     */
    @Override
    public ItemIndex<?> getIndex() {
        return mesh.attributes.get(IndexedTriangleMesh.ATTR_VCOLORS);
    }

    /**
     *
     * @return
     */
    @Override
    public int getStride() {
        return 4;
    }

}
