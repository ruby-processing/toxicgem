package toxi.processing;

import java.util.Collection;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.TriangleMesh;

/**
 *
 * @author Martin Prout
 */
public class MeshToVBO {

    private final PApplet app;
    private final Matrix4x4 normalMap =
    new Matrix4x4().translateSelf(128, 128, 128).scaleSelf(127);

    /**
     *
     * @param app PApplet
     */
    public MeshToVBO(PApplet app) {
        this.app = app;
    }

    /**
     * Use default smooth (smooth = false) for cubes etc
     * @param mesh Toxiclibs mesh
     * @param smooth boolean
     * @return
     */
    public PShape meshToShape(TriangleMesh mesh, boolean smooth) {
        PShape retained = app.createShape();
        retained.beginShape(PConstants.TRIANGLE);
        if (smooth) {
            mesh.faces.stream().map((f) -> {
                retained.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                retained.vertex(f.a.x, f.a.y, f.a.z);
                retained.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                retained.vertex(f.b.x, f.b.y, f.b.z);
                retained.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                return f;
            }).forEach((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        } else {
            mesh.faces.stream().map((f) -> {
                retained.normal(f.normal.x, f.normal.y, f.normal.z);
                retained.vertex(f.a.x, f.a.y, f.a.z);
                retained.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).forEach((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        }
        retained.endShape();
        return retained;
    }
    
    public PShape meshToColoredShape(TriangleMesh mesh, 
      boolean vertexNormals) {
        PShape gfx = app.createShape();
        gfx.beginShape(PConstants.TRIANGLES);
        if (vertexNormals) {
            mesh.faces.stream().map((f) -> {
                Vec3D n = normalMap.applyTo(f.a.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                n = normalMap.applyTo(f.b.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                n = normalMap.applyTo(f.c.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                return f;
            }).forEach((f) -> {
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            });
        } else {
            mesh.faces.stream().map((f) -> {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).forEach((f) -> {
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            });
        }
        gfx.endShape();        
        return gfx;
    }
}
