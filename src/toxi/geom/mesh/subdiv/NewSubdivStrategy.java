package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.Vec3D;

/**
 *
 * @author tux
 */
public interface NewSubdivStrategy {

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param resultVertices
     * @return
     */
    public List<Vec3D[]> subdivideTriangle(Vec3D a, Vec3D b, Vec3D c,
            List<Vec3D[]> resultVertices);
}
