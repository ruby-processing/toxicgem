package toxi.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.geom.mesh.TriangleMesh;

/**
 *
 * @author Martin Prout
 */
public class MeshToVBO {
    final int DEFAULT_COLOR = -1; // white
    private int fillColor;
    private boolean wire = false;
    private final PApplet app;
    private final Matrix4x4 normalMap
        = new Matrix4x4().translateSelf(128, 128, 128).scaleSelf(127);

    /**
     *
     * @param app PApplet
     */
    public MeshToVBO(PApplet app) {
        this.app = app;
        this.fillColor = DEFAULT_COLOR;
    }

    public void wire(boolean show) {
        this.wire = show;
    }

    /**
     *
     * @param col
     */
    public void fillColor(int col) {
        this.fillColor = col;
    }

    /**
     * Use default smooth (smooth = false) for cubes etc
     *
     * @param mesh Toxiclibs mesh
     * @param smooth boolean
     * @return
     */
    public PShape meshToShape(TriangleMesh mesh, boolean smooth) {
        if (!wire) {
            app.noStroke();
        }
        PShape retained = app.createShape();
        retained.beginShape(PConstants.TRIANGLE);
        retained.fill(fillColor);
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
            if (wire) {
                app.noFill();
            }
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

    public PShape meshToColoredShape(TriangleMesh mesh) {
        app.noStroke();
        PShape gfx = app.createShape();
        gfx.beginShape(PConstants.TRIANGLES);
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
        gfx.endShape();
        return gfx;
    }
    
    public PShape meshToTexturedShape(TriangleMesh mesh, PImage tex) {
        PShape gfx = app.createShape();
        gfx.beginShape(PConstants.TRIANGLE);
        gfx.texture(tex);
        mesh.faces.stream().map((f) -> {
            gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
            gfx.vertex(f.a.x, f.a.y, f.a.z, 0, 0);
            gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
            gfx.vertex(f.b.x, f.b.y, f.b.z, tex.width, 0);
            gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
            return f;
        }).forEach((f) -> {
            gfx.vertex(f.c.x, f.c.y, f.c.z, tex.width, tex.height);
        });
        gfx.endShape();
        return gfx;
    }
}