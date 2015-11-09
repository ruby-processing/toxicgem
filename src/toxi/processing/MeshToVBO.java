/*
 * This library adds translate from toxiclibs Mesh to processing PShape (vbo)
 * Copyright (c) 2015 Martin Prout
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * http://creativecommons.org/licenses/LGPL/2.1/
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
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
    public PShape meshToShape(Mesh3D mesh, boolean smooth) {
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
