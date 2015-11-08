/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toxi.processing;

import java.util.Collection;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.Face;

/**
 *
 * @author Martin Prout
 */
public class MeshToVBO {

    private final PApplet app;

    public MeshToVBO(PApplet app) {
        this.app = app;
    }

    PShape meshToShape(Mesh3D mesh, boolean smooth) {
        PShape retained = app.createShape();
        retained.beginShape(PConstants.TRIANGLE);
        if (smooth) {
            mesh.computeVertexNormals();
            Collection<Face> faces = mesh.getFaces();
            faces.stream().map((f) -> {
                retained.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.a.x, f.a.y, f.a.z);
                return f;
            }).map((f) -> {
                retained.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).map((f) -> {
                retained.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                return f;
            }).forEach((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        } else {
            Collection<Face> faces = mesh.getFaces();
            faces.stream().map((f) -> {
                retained.normal(f.normal.x, f.normal.y, f.normal.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.a.x, f.a.y, f.a.z);
                return f;
            }).map((f) -> {
                retained.vertex(f.b.x, f.b.y, f.b.z);
                return f;
            }).forEach((f) -> {
                retained.vertex(f.c.x, f.c.y, f.c.z);
            });
        }
        retained.endShape();
        return retained;
    }
}
