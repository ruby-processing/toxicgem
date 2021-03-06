/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import toxi.geom.AABB;
import toxi.geom.Line3D;
import toxi.geom.Matrix4x4;
import toxi.geom.Quaternion;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.subdiv.MidpointSubdivision;
import toxi.geom.mesh.subdiv.SubdivisionStrategy;

/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * build face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class WETriangleMesh extends TriangleMesh {

    /**
     * WEVertex buffer & lookup index when adding new faces
     */
    public LinkedHashMap<Line3D, WingedEdge> edges;

    private final Line3D edgeCheck = new Line3D(new Vec3D(), new Vec3D());

    private int uniqueEdgeID;

    /**
     *
     */
    public WETriangleMesh() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with initial default buffer sizes.
     * 
     * @param name
     *            mesh name
     */
    public WETriangleMesh(String name) {
        this(name, DEFAULT_NUM_VERTICES, DEFAULT_NUM_FACES);
    }

    /**
     * Creates a new mesh instance with the given initial buffer sizes. These
     * numbers are no limits and the mesh can be smaller or grow later on.
     * They're only used to initialise the underlying collections.
     * 
     * @param name
     *            mesh name
     * @param numV
     *            initial vertex buffer size
     * @param numF
     *            initial face list size
     */
    public WETriangleMesh(String name, int numV, int numF) {
        super(name, numV, numF);
    }

    @Override
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c) {
        return addFace(a, b, c, null, null, null, null);
    }

    @Override
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uvA,
            Vec2D uvB, Vec2D uvC) {
        return addFace(a, b, c, null, uvA, uvB, uvC);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param n
     * @return
     */
    @Override
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n) {
        return addFace(a, b, c, n, null, null, null);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param n
     * @param uvA
     * @param uvB
     * @param uvC
     * @return
     */
    @Override
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n,
            Vec2D uvA, Vec2D uvB, Vec2D uvC) {
        WEVertex va = checkVertex(a);
        WEVertex vb = checkVertex(b);
        WEVertex vc = checkVertex(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "ignorning invalid face: {0},{1},{2}", new Object[]{a, b, c});
            }
        } else {
            if (n != null) {
                Vec3D nc = va.sub(vc).crossSelf(va.sub(vb));
                if (n.dot(nc) < 0) {
                    WEVertex t = va;
                    va = vb;
                    vb = t;
                }
            }
            WEFace f = new WEFace(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaces++;
            updateEdge(va, vb, f);
            updateEdge(vb, vc, f);
            updateEdge(vc, va, f);
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     * @return 
     */
    @Override
    public WETriangleMesh addMesh(Mesh3D m) {
        super.addMesh(m);
        return this;
    }

    @Override
    public AABB center(ReadonlyVec3D origin) {
        super.center(origin);
        rebuildIndex();
        return bounds;
    }

    private WEVertex checkVertex(Vec3D v) {
        WEVertex vertex = (WEVertex) vertices.get(v);
        if (vertex == null) {
            vertex = createVertex(v, uniqueVertexID++);
            vertices.put(vertex, vertex);
            numVertices++;
        }
        return vertex;
    }

    /**
     * Clears all counters, and vertex & face buffers.
     * @return 
     */
    @Override
    public WETriangleMesh clear() {
        super.clear();
        edges.clear();
        return this;
    }

    /**
     * Creates a deep clone of the mesh. The new mesh name will have "-copy" as
     * suffix.
     * 
     * @return new mesh instance
     */
    @Override
    public WETriangleMesh copy() {
        WETriangleMesh m = new WETriangleMesh(name + "-copy", numVertices,
                numFaces);
        faces.stream().forEach((f) -> {
            m.addFace(f.a, f.b, f.c, f.normal, f.uvA, f.uvB, f.uvC);
        });
        return m;
    }

    /**
     *
     * @param v
     * @param id
     * @return
     */
    @Override
    protected WEVertex createVertex(Vec3D v, int id) {
        return new WEVertex(v, id);
    }

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. WEFace
     * normals are updated automatically too.
     * 
     * @return itself
     */
    @Override
    public WETriangleMesh flipVertexOrder() {
        super.flipVertexOrder();
        return this;
    }

    @Override
    public WETriangleMesh flipYAxis() {
        super.flipYAxis();
        return this;
    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public WEVertex getClosestVertexToPoint(ReadonlyVec3D p) {
        return (WEVertex) super.getClosestVertexToPoint(p);
    }

    /**
     *
     * @return
     */
    public Collection<WingedEdge> getEdges() {
        return edges.values();
    }

    /**
     *
     * @return
     */
    public int getNumEdges() {
        return edges.size();
    }

    /**
     *
     * @param axis
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh getRotatedAroundAxis(Vec3D axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    /**
     *
     * @param scale
     * @return
     */
    @Override
    public WETriangleMesh getScaled(float scale) {
        return copy().scale(scale);
    }

    /**
     *
     * @param scale
     * @return
     */
    @Override
    public WETriangleMesh getScaled(Vec3D scale) {
        return copy().scale(scale);
    }

    /**
     *
     * @param trans
     * @return
     */
    @Override
    public WETriangleMesh getTranslated(Vec3D trans) {
        return copy().translate(trans);
    }

    /**
     *
     * @param v
     * @return
     */
    @Override
    public WEVertex getVertexAtPoint(Vec3D v) {
        return (WEVertex) vertices.get(v);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public WEVertex getVertexForID(int id) {
        return (WEVertex) super.getVertexForID(id);
    }

    @Override
    public WETriangleMesh init(String name, int numV, int numF) {
        super.init(name, numV, numF);
        edges = new LinkedHashMap<>(numV, 1.5f, false);
        return this;
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version uses the positive Z-axis as default
     * forward direction.
     * 
     * @param dir
     *            new target direction to point in
     * @return itself
     */
    @Override
    public WETriangleMesh pointTowards(ReadonlyVec3D dir) {
        return transform(Quaternion.getAlignmentQuat(dir, Vec3D.Z_AXIS)
                .toMatrix4x4(matrix), true);
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version allows to specify the forward
     * direction.
     * 
     * @param dir
     *            new target direction to point in
     * @param forward
     *            current forward axis
     * @return itself
     */
    @Override
    public WETriangleMesh pointTowards(ReadonlyVec3D dir, ReadonlyVec3D forward) {
        return transform(
                Quaternion.getAlignmentQuat(dir, forward).toMatrix4x4(matrix),
                true);
    }

    /**
     *
     */
    public void rebuildIndex() {
        LinkedHashMap<Vec3D, Vertex> newV = new LinkedHashMap<>(
                vertices.size());
        vertices.values().stream().forEach((v) -> {
            newV.put(v, v);
        });
        vertices = newV;
        LinkedHashMap<Line3D, WingedEdge> newE = new LinkedHashMap<>(
                edges.size());
        edges.values().stream().forEach((e) -> {
            newE.put(e, e);
        });
        edges = newE;
    }

    /**
     *
     * @param e
     */
    protected void removeEdge(WingedEdge e) {
        e.remove();
        WEVertex v = (WEVertex) e.a;
        if (v.edges.isEmpty()) {
            vertices.remove(v);
        }
        v = (WEVertex) e.b;
        if (v.edges.isEmpty()) {
            vertices.remove(v);
        }
        e.faces.stream().forEach((f) -> {
            removeFace(f);
        });
        WingedEdge removed = edges.remove(edgeCheck.set(e.a, e.b));
        if (removed != e) {
            throw new IllegalStateException("can't remove edge");
        }
    }

    /**
     *
     * @param f
     */
    @Override
    public void removeFace(Face f) {
        faces.remove(f);
        ((WEFace) f).edges.stream().map((WingedEdge e) -> {
            e.faces.remove((WEFace)f);
            return e;
        }).filter((e) -> (e.faces.isEmpty())).forEach((e) -> {
            removeEdge(e);
        });
    }

    // FIXME

    /**
     *
     */
        public void removeUnusedVertices() {
        for (Iterator<Vertex> i = vertices.values().iterator(); i.hasNext();) {
            Vertex v = i.next();
            boolean isUsed = false;
            for (Face f : faces) {
                if (f.a == v || f.b == v || f.c == v) {
                    isUsed = true;
                    break;
                }
            }
            if (!isUsed) {
                logger.log(Level.INFO, "removing vertex: {0}", v);
                i.remove();
            }
        }
    }

    /**
     *
     * @param selection
     */
    public void removeVertices(Collection<Vertex> selection) {
        selection.stream().map((v) -> (WEVertex) v).forEach((WEVertex wv) -> {
            new ArrayList<>(wv.edges).stream().forEach((e) -> {
                new ArrayList<>(e.faces).stream().forEach((f) -> {
                    removeFace(f);
                });
            });
        }); // rebuildIndex();
    }

    /**
     *
     * @param axis
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh rotateAroundAxis(Vec3D axis, float theta) {
        return transform(matrix.identity().rotateAroundAxis(axis, theta));
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh rotateX(float theta) {
        return transform(matrix.identity().rotateX(theta));
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh rotateY(float theta) {
        return transform(matrix.identity().rotateY(theta));
    }

    /**
     *
     * @param theta
     * @return
     */
    @Override
    public WETriangleMesh rotateZ(float theta) {
        return transform(matrix.identity().rotateZ(theta));
    }

    /**
     *
     * @param scale
     * @return
     */
    @Override
    public WETriangleMesh scale(float scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    /**
     *
     * @param scale
     * @return
     */
    @Override
    public WETriangleMesh scale(Vec3D scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    /**
     *
     * @param a
     * @param b
     * @param subDiv
     */
    public void splitEdge(ReadonlyVec3D a, ReadonlyVec3D b,
            SubdivisionStrategy subDiv) {
        WingedEdge e = edges.get(edgeCheck.set(a, b));
        if (e != null) {
            splitEdge(e, subDiv);
        }
    }

    /**
     *
     * @param e
     * @param subDiv
     */
    public void splitEdge(WingedEdge e, SubdivisionStrategy subDiv) {
        List<Vec3D> mid = subDiv.computeSplitPoints(e);
        splitFace(e.faces.get(0), e, mid);
        if (e.faces.size() > 1) {
            splitFace(e.faces.get(1), e, mid);
        }
        removeEdge(e);
    }

    /**
     *
     * @param f
     * @param e
     * @param midPoints
     */
    protected void splitFace(WEFace f, WingedEdge e, List<Vec3D> midPoints) {
        Vec3D p = null;
        for (int i = 0; i < 3; i++) {
            WingedEdge ec = f.edges.get(i);
            if (!ec.equals(e)) {
                if (ec.a.equals(e.a) || ec.a.equals(e.b)) {
                    p = ec.b;
                } else {
                    p = ec.a;
                }
                break;
            }
        }
        Vec3D prev = null;
        for (int i = 0, num = midPoints.size(); i < num; i++) {
            Vec3D mid = midPoints.get(i);
            if (i == 0) {
                addFace(p, e.a, mid, f.normal);
            } else {
                addFace(p, prev, mid, f.normal);
            }
            if (i == num - 1) {
                addFace(p, mid, e.b, f.normal);
            }
            prev = mid;
        }
    }

    /**
     *
     */
    public void subdivide() {
        subdivide(0);
    }

    /**
     *
     * @param minLength
     */
    public void subdivide(float minLength) {
        subdivide(new MidpointSubdivision(), minLength);
    }

    /**
     *
     * @param subDiv
     */
    public void subdivide(SubdivisionStrategy subDiv) {
        subdivide(subDiv, 0);
    }

    /**
     *
     * @param subDiv
     * @param minLength
     */
    public void subdivide(SubdivisionStrategy subDiv, float minLength) {
        subdivideEdges(new ArrayList<>(edges.values()), subDiv,
                minLength);
    }

    /**
     *
     * @param origEdges
     * @param subDiv
     * @param minLength
     */
    protected void subdivideEdges(List<WingedEdge> origEdges,
            SubdivisionStrategy subDiv, float minLength) {
        Collections.sort(origEdges, subDiv.getEdgeOrdering());
        minLength *= minLength;
        for (WingedEdge e : origEdges) {
            if (edges.containsKey(e)) {
                if (e.getLengthSquared() >= minLength) {
                    splitEdge(e, subDiv);
                }
            }
        }
    }

    /**
     *
     * @param faces
     * @param subDiv
     * @param minLength
     */
    public void subdivideFaceEdges(List<WEFace> faces,
            SubdivisionStrategy subDiv, float minLength) {
        List<WingedEdge> fedges = new ArrayList<>();
        faces.stream().forEach((f) -> {
            f.edges.stream().filter((e) -> (!fedges.contains(e))).forEach((e) -> {
                fedges.add(e);
            });
        });
        subdivideEdges(fedges, subDiv, minLength);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "WETriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaces() + " edges:" + getNumEdges();
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    @Override
    public WETriangleMesh transform(Matrix4x4 mat) {
        return transform(mat, true);
    }

    /**
     * Applies the given matrix transform to all mesh vertices. If the
     * updateNormals flag is true, all face normals are updated automatically,
     * however vertex normals still need a manual update.
     * 
     * @param mat
     * @param updateNormals
     * @return itself
     */
    @Override
    public WETriangleMesh transform(Matrix4x4 mat, boolean updateNormals) {
        vertices.values().stream().forEach((v) -> {
            mat.applyToSelf(v);
        });
        rebuildIndex();
        if (updateNormals) {
            computeFaceNormals();
        }
        return this;
    }

    /**
     *
     * @param trans
     * @return
     */
    @Override
    public WETriangleMesh translate(Vec3D trans) {
        return transform(matrix.identity().translateSelf(trans));
    }

    /**
     *
     * @param va
     * @param vb
     * @param f
     */
    protected void updateEdge(WEVertex va, WEVertex vb, WEFace f) {
        edgeCheck.set(va, vb);
        WingedEdge e = edges.get(edgeCheck);
        if (e != null) {
            e.addFace(f);
        } else {
            e = new WingedEdge(va, vb, f, uniqueEdgeID++);
            edges.put(e, e);
            va.addEdge(e);
            vb.addEdge(e);
        }
        f.addEdge(e);
    }
}
