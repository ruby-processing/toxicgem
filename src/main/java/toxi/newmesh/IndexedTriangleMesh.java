package toxi.newmesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import toxi.geom.AABB;
import toxi.geom.IsectData3D;
import toxi.geom.Ray3D;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Sphere;
import toxi.geom.Triangle3D;
import toxi.geom.TriangleIntersector;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.subdiv.NewSubdivStrategy;
import toxi.util.datatypes.ItemIndex;
import toxi.util.datatypes.UniqueItemIndex;

/**
 *
 * @author tux
 */
public class IndexedTriangleMesh {

    /**
     *
     */
    public static final String ATTR_EDGES = "edges";

    /**
     *
     */
    public static final String ATTR_FNORMALS = "fnormals";

    /**
     *
     */
    public static final String ATTR_UVCOORDS = "uv";

    /**
     *
     */
    public static final String ATTR_VCOLORS = "col";

    /**
     *
     */
    public static final String ATTR_VERTICES = "vertices";

    /**
     *
     */
    public static final String ATTR_VNORMALS = "vnormals";

    /**
     *
     */
    public SpatialIndex vertices = new SpatialIndex(0.001f);

    /**
     *
     */
    public ItemIndex<Vec3D> fnormals = new UniqueItemIndex<>();

    /**
     *
     */
    public final ArrayList<AttributedFace> faces = new ArrayList<>();

    /**
     *
     */
    public final HashMap<String, UniqueItemIndex<Object>> attributes = new HashMap<>();

    /**
     *
     */
    public IndexedTriangleMesh() {
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param attribs
     * @return
     */
    public IndexedTriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c,
            HashMap<String, Object[]> attribs) {
        int idA = vertices.index(a);
        int idB = vertices.index(b);
        int idC = vertices.index(c);
        if (idA != idB && idA != idC && idB != idC) {
            AttributedFace f = new AttributedFace(idA, idB, idC,
                    addFaceAttributes(null, attribs));
            faces.add(f);
        }
        return this;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param uva
     * @param uvb
     * @param uvc
     * @return
     */
    public IndexedTriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uva,
            Vec2D uvb, Vec2D uvc) {
        HashMap<String, Object[]> attribs = null;
        if (uva != null && uvb != null && uvc != null) {
            attribs = new HashMap<>();
            attribs.put(ATTR_UVCOORDS, new Object[] {
                    uva, uvb, uvc
            });
        }
        return addFace(a, b, c, attribs);
    }

    /**
     *
     * @param f
     * @param attrib
     * @param attA
     * @param attB
     * @param attC
     * @return
     */
    public HashMap<String, int[]> addFaceAttribute(AttributedFace f,
            String attrib, Object attA, Object attB, Object attC) {
        if (f != null && attrib != null && attA != null && attB != null
                && attC != null) {
            ItemIndex<Object> idx = getAttributeIndex(attrib);
            f.attribs.put(attrib, new int[] {
                    idx.index(attA), idx.index(attB), idx.index(attC)
            });
            return f.attribs;
        }
        return null;
    }

    /**
     *
     * @param f
     * @param attribs
     * @return
     */
    public HashMap<String, int[]> addFaceAttributes(AttributedFace f,
            HashMap<String, Object[]> attribs) {
        HashMap<String, int[]> fattribs = null;
        if (attribs != null) {
            fattribs = (f != null) ? f.attribs : new HashMap<>(
                    attribs.size(), 1);
            for (String attID : attribs.keySet()) {
                Object[] items = attribs.get(attID);
                if (items.length >= 3) {
                    ItemIndex<Object> idx = getAttributeIndex(attID);
                    int[] ids = new int[] {
                            idx.index(items[0]), idx.index(items[1]),
                            idx.index(items[2])
                    };
                    fattribs.put(attID, ids);
                }
            }
        }
        return fattribs;
    }

    /**
     *
     * @param mesh
     * @return
     */
    public IndexedTriangleMesh addMesh(IndexedTriangleMesh mesh) {
        Vec3D[] v = null;
        for (AttributedFace f : mesh.faces) {
            v = mesh.getFaceVertices(f, v);
            addFace(v[0], v[1], v[2], null);
        }
        return this;
    }

    /**
     *
     * @param mesh
     * @return
     */
    public IndexedTriangleMesh addMeshWithAttribs(IndexedTriangleMesh mesh) {
        HashMap<String, Object[]> attribs = new HashMap<>();
        Vec3D[] v = null;
        for (AttributedFace f : mesh.faces) {
            attribs.clear();
            f.attribs.keySet().forEach((a) -> {
              attribs.put(a, mesh.getFaceAttribValues(f, a));
          });
            v = mesh.getFaceVertices(f, v);
            addFace(v[0], v[1], v[2], attribs);
        }
        return this;
    }

    /**
     *
     * @return
     */
    public IndexedTriangleMesh clear() {
        vertices.clear();
        fnormals.clear();
        attributes.clear();
        faces.clear();
        return this;
    }

    /**
     *
     * @return
     */
    public HashMap<String, float[]> compile() {
        HashSet<String> attribs = new HashSet<>();
        attribs.add(ATTR_VERTICES);
        attribs.add(ATTR_FNORMALS);
        return compile(attribs, null);
    }

    /**
     *
     * @param attribs
     * @param compilers
     * @return
     */
    public HashMap<String, float[]> compile(HashSet<String> attribs,
            HashMap<String, MeshAttributeCompiler> compilers) {
        HashMap<String, MeshAttributeCompiler> mergedComps = new HashMap<>(
                getDefaultCompilers());
        if (compilers != null) {
            mergedComps.putAll(compilers);
        }
        HashMap<String, float[]> buffers = new HashMap<>();
        int numF = faces.size();
        attribs.forEach((attrib) -> {
          MeshAttributeCompiler comp = mergedComps.get(attrib);
        if (comp != null) {
          comp.setMesh(this);
          ItemIndex<?> index = comp.getIndex();
          int faceStride = 3 * comp.getStride();
          float[] buf = new float[numF * faceStride];
          int offset = 0;
          for (AttributedFace f : faces) {
            comp.compileFace(f, index, buf, offset);
            offset += faceStride;
          }
          buffers.put(attrib, buf);
        }
      });
        return buffers;
    }

    /**
     *
     * @return
     */
    public List<Object> computeEdges() {
        ItemIndex<Object> edges = getAttributeIndex(ATTR_EDGES);
        edges.clear();
        faces.stream().map((f) -> {
          if (f.attribs == null) {
            f.attribs = new HashMap<>();
          }
        return f;
      }).forEachOrdered((f) -> {
        f.attribs.put(ATTR_EDGES, new int[] {
          indexFaceEdge(f, f.a, f.b), indexFaceEdge(f, f.b, f.c),
          indexFaceEdge(f, f.c, f.a)
        });
      });
        return edges.getItems();
    }

    /**
     *
     * @return
     */
    public List<Vec3D> computeFaceNormals() {
        fnormals.clear();
        Vec3D[] v = null;
        for (AttributedFace f : faces) {
            v = getFaceVertices(f, v);
            f.normal = fnormals.index(v[0].sub(v[1]).crossSelf(v[0].sub(v[2]))
                    .normalize());
        }
        return fnormals.getItems();
    }

    /**
     *
     * @return
     */
    public List<Object> computeVertexNormals() {
        Vec3D[] vnorms = new Vec3D[vertices.size()];
        for (int i = 0; i < vnorms.length; i++) {
            vnorms[i] = new Vec3D();
        }
        faces.forEach((f) -> {
          final Vec3D n = fnormals.forID(f.normal);
          vnorms[f.a].addSelf(n);
          vnorms[f.b].addSelf(n);
          vnorms[f.c].addSelf(n);
      });
        for (Vec3D vnorm : vnorms) {
            vnorm.normalize();
        }
        ItemIndex<Object> idx = getAttributeIndex(ATTR_VNORMALS);
        idx.clear();
        faces.stream().map((f) -> {
          if (f.attribs == null) {
            f.attribs = new HashMap<>();
          }
        return f;
      }).forEachOrdered((f) -> {
        f.attribs.put(
          ATTR_VNORMALS,
          new int[] {
            idx.index(vnorms[f.a]), idx.index(vnorms[f.b]),
            idx.index(vnorms[f.c])
          });
      });
        return idx.getItems();
    }

    /**
     *
     * @param f
     * @param offset
     * @param scale
     * @return
     */
    public IndexedTriangleMesh extrudeFace(AttributedFace f, Vec3D offset,
            float scale) {
        Vec3D[] v = getFaceVertices(f, null);
        Vec3D[] v2 = new Vec3D[3];
        Vec3D c = v[0].add(v[1]).addSelf(v[2]).scaleSelf(1 / 3f);
        Vec3D n = c.add(offset);
        v2[0] = v[0].sub(c).scaleSelf(scale).addSelf(n);
        v2[1] = v[1].sub(c).scaleSelf(scale).addSelf(n);
        v2[2] = v[2].sub(c).scaleSelf(scale).addSelf(n);
        removeFace(f);
        // extruded copy
        addFace(v2[0], v2[1], v2[2], null);
        // sides
        addFace(v[0], v[1], v2[0], null);
        addFace(v[1], v2[1], v2[0], null);
        addFace(v[1], v[2], v2[1], null);
        addFace(v[2], v2[2], v2[1], null);
        addFace(v[2], v[0], v2[2], null);
        addFace(v[0], v2[0], v2[2], null);
        return this;
    }

    /**
     * Flips the current order of all face vertices and attributes. If face or
     * vertex normals are present their direction will be inverted as well.
     * 
     * @return itself
     */
    public IndexedTriangleMesh flipVertexOrder() {
      faces.forEach((f) -> {
        int t = f.b;
        f.b = f.c;
        f.c = t;
        if (f.attribs != null) {
          for (int[] att : f.attribs.values()) {
            t = att[1];
            att[1] = att[2];
            att[2] = t;
          }
        }
      });
      fnormals.getItems().forEach((n) -> {
        fnormals.reindex(n, n.getInverted());
      });
        ItemIndex<Object> vnormals = attributes.get(ATTR_VNORMALS);
        if (vnormals != null) {
          vnormals.getItems().forEach((n) -> {
            vnormals.reindex(n, ((Vec3D) n).getInverted());
        });
        }
        return this;
    }

    /**
     *
     * @param attID
     * @return
     */
    public ItemIndex<Object> getAttributeIndex(String attID) {
        UniqueItemIndex<Object> idx = attributes.get(attID);
        if (idx == null) {
            idx = new UniqueItemIndex<>();
            attributes.put(attID, idx);
        }
        return idx;
    }

    /**
     *
     * @return
     */
    public Sphere getBoundingSphere() {
        return getBounds().getBoundingSphere();
    }

    /**
     *
     * @return
     */
    public AABB getBounds() {
        return AABB.getBoundingBox(vertices.getItems());
    }

    /**
     *
     * @return
     */
    public Vec3D getCentroid() {
        return new Vec3D(getBounds());
    }

    /**
     *
     * @param p
     * @return
     */
    public Vec3D getClosestVertexToPoint(ReadonlyVec3D p) {
        Vec3D closest = null;
        float minDist = Float.MAX_VALUE;
        for (Vec3D v : vertices.getItems()) {
            float d = v.distanceToSquared(p);
            if (d < minDist) {
                closest = v;
                minDist = d;
            }
        }
        return closest;
    }

    /**
     *
     * @return
     */
    public HashMap<String, MeshAttributeCompiler> getDefaultCompilers() {
        HashMap<String, MeshAttributeCompiler> compilers = new HashMap<>();
        compilers.put(ATTR_VERTICES, new MeshVertexCompiler());
        compilers.put(ATTR_FNORMALS, new MeshFaceNormalCompiler());
        compilers.put(ATTR_VNORMALS, new MeshVertexNormalCompiler());
        compilers.put(ATTR_UVCOORDS, new MeshUVCompiler());
        compilers.put(ATTR_VCOLORS, new MeshVertexColorCompiler());
        return compilers;
    }

    /**
     * @return the edges
     */
    public List<Object> getEdges() {
        return getAttributeIndex(ATTR_EDGES).getItems();
    }

    /**
     *
     * @param v
     * @return
     */
    public List<AttributedEdge> getEdgesForVertex(Vec3D v) {
        List<AttributedEdge> vedges = null;
        int id = vertices.getID(v);
        if (id != -1) {
            vedges = getEdgesForVertexID(id);
        }
        return vedges;
    }

    /**
     *
     * @param id
     * @return
     */
    public List<AttributedEdge> getEdgesForVertexID(int id) {
        List<AttributedEdge> vedges = new ArrayList<>(2);
        getEdges().stream().map((o) -> (AttributedEdge) o).filter((e) -> (e.a == id || e.b == id)).forEachOrdered((e) -> {
          vedges.add(e);
      });
        return vedges;
    }

    /**
     *
     * @param f
     * @return
     */
    public Triangle3D getFaceAsTriangle(AttributedFace f) {
        Vec3D[] verts = getFaceVertices(f, null);
        return new Triangle3D(verts[0], verts[1], verts[2]);
    }

    /**
     *
     * @param f
     * @param attribs
     * @return
     */
    public HashMap<String, Object[]> getFaceAttribValues(AttributedFace f,
            String... attribs) {
        HashMap<String, Object[]> values = new HashMap<>(
                attribs.length, 1);
        for (String a : attribs) {
            values.put(a, getFaceAttribValues(f, a));
        }
        return values;
    }

    /**
     *
     * @param f
     * @param att
     * @return
     */
    public Object[] getFaceAttribValues(AttributedFace f, String att) {
        ItemIndex<Object> idx = attributes.get(att);
        int[] fattribs = f.attribs.get(att);
        if (idx == null || fattribs == null) {
            return null;
        }
        return new Object[] {
                idx.forID(fattribs[0]), idx.forID(fattribs[1]),
                idx.forID(fattribs[2])
        };
    }

    /**
     * @return the fnormals
     */
    public List<Vec3D> getFaceNormals() {
        return fnormals.getItems();
    }

    /**
     * @return the faces
     */
    public List<AttributedFace> getFaces() {
        return faces;
    }

    /**
     *
     * @param v
     * @return
     */
    public List<AttributedFace> getFacesForVertex(Vec3D v) {
        List<AttributedFace> vfaces = null;
        int id = vertices.getID(v);
        if (id != -1) {
            vfaces = new ArrayList<>(2);
            for (AttributedFace f : faces) {
                if (f.a == id || f.b == id || f.c == id) {
                    vfaces.add(f);
                }
            }
        }
        return vfaces;
    }

    /**
     *
     * @param f
     * @param verts
     * @return
     */
    public final Vec3D[] getFaceVertices(AttributedFace f, Vec3D[] verts) {
        if (verts != null) {
            verts[0] = vertices.forID(f.a);
            verts[1] = vertices.forID(f.b);
            verts[2] = vertices.forID(f.c);
            return verts;
        } else {
            return new Vec3D[] {
                    vertices.forID(f.a), vertices.forID(f.b),
                    vertices.forID(f.c)
            };
        }
    }

    /**
     *
     * @param id
     * @param neighbors
     * @return
     */
    public List<Vec3D> getNeighborsForVertexID(int id, List<Vec3D> neighbors) {
        List<AttributedEdge> vedges = getEdgesForVertexID(id);
        if (vedges.size() > 0) {
            if (neighbors == null) {
                neighbors = new ArrayList<>();
            } else {
                neighbors.clear();
            }
            for (AttributedEdge e : vedges) {
                neighbors.add(vertices.forID((e.a == id) ? e.b : e.a));
            }
        } else if (neighbors != null) {
            neighbors.clear();
        }
        return neighbors;
    }

    /**
     *
     * @return
     */
    public final int getNumFaces() {
        return faces.size();
    }

    /**
     *
     * @return
     */
    public final int getNumVertices() {
        return vertices.size();
    }

    /**
     *
     * @return
     */
    public float getVertexDelta() {
        return vertices.getDelta();
    }

    /**
     * @return the vertices
     */
    public List<Vec3D> getVertices() {
        return vertices.getItems();
    }

    /**
     *
     * @param verts
     * @param ids
     * @return
     */
    public List<Vec3D> getVerticesForIDs(List<Vec3D> verts, int... ids) {
        if (verts == null) {
            verts = new ArrayList<>(ids.length);
        }
        for (int id : ids) {
            verts.add(vertices.forID(id));
        }
        return verts;
    }

    /**
     *
     * @param f
     * @param a
     * @param b
     * @return
     */
    protected final int indexFaceEdge(AttributedFace f, int a, int b) {
        final AttributedEdge e1 = new AttributedEdge(a, b);
        final AttributedEdge e2 = new AttributedEdge(b, a);
        ItemIndex<Object> edges = getAttributeIndex(ATTR_EDGES);
        final int id1 = edges.getID(e1);
        final int id2 = edges.getID(e2);
        if (id1 != -1) {
            ((AttributedEdge) edges.forID(id1)).addFace(f);
            return id1;
        } else if (id2 != -1) {
            ((AttributedEdge) edges.forID(id2)).addFace(f);
            return id2;
        } else {
            e1.addFace(f);
            return edges.index(e1);
        }
    }

    /**
     *
     * @param ray
     * @return
     */
    public IsectData3D intersectsRay(Ray3D ray) {
        TriangleIntersector intersector = new TriangleIntersector();
        Triangle3D tri = intersector.getTriangle();
        Vec3D[] v = null;
        for (AttributedFace f : faces) {
            v = getFaceVertices(f, v);
            tri.set(v[0], v[1], v[2]);
            if (intersector.intersectsRay(ray)) {
                return intersector.getIntersectionData();
            }
        }
        return null;
    }

    /**
     *
     */
    public void rebuildVertexIndex() {
        SpatialIndex newVerts = new SpatialIndex(vertices.getDelta());
        Vec3D[] v = null;
        for (AttributedFace f : faces) {
            v = getFaceVertices(f, v);
            f.a = newVerts.index(v[0]);
            f.b = newVerts.index(v[1]);
            f.c = newVerts.index(v[2]);
        }
        vertices = newVerts;
    }

    /**
     *
     * @param f
     * @return
     */
    public IndexedTriangleMesh removeFace(AttributedFace f) {
        faces.remove(f);
        return this;
    }

    /**
     *
     * @param delta
     * @return
     */
    public IndexedTriangleMesh setVertexDelta(float delta) {
        vertices.setDelta(delta);
        return this;
    }

    /**
     *
     * @return
     */
    public IndexedTriangleMesh smooth() {
        HashMap<Integer, Vec3D> lapIndex = new HashMap<>();
        List<Vec3D> neighbors = null;
        for (int i = 0, numV = getNumVertices(); i < numV; i++) {
            neighbors = getNeighborsForVertexID(i, neighbors);
            if (neighbors != null && neighbors.size() > 0) {
                Vec3D l = new Vec3D();
                for (Vec3D n : neighbors) {
                    l.addSelf(n);
                }
                l.scaleSelf(1f / neighbors.size());
                lapIndex.put(i, l);
            }
        }
        SpatialIndex newVerts = new SpatialIndex(vertices.getDelta());
        for (Iterator<AttributedFace> i = faces.iterator(); i.hasNext();) {
            AttributedFace f = i.next();
            f.a = newVerts.index(lapIndex.get(f.a));
            f.b = newVerts.index(lapIndex.get(f.b));
            f.c = newVerts.index(lapIndex.get(f.c));
            if (f.a == f.b || f.a == f.c || f.b == f.c) {
                i.remove();
            }
        }
        vertices = newVerts;
        computeEdges();
        return this;
    }

    /**
     *
     * @param strategy
     * @return
     */
    public IndexedTriangleMesh subdivide(NewSubdivStrategy strategy) {
        Vec3D[] v = null;
        List<Vec3D[]> splitFaces = new ArrayList<>();
        for (AttributedFace f : new ArrayList<>(faces)) {
            removeFace(f);
            v = getFaceVertices(f, v);
            for (Vec3D[] fverts : strategy.subdivideTriangle(v[0], v[1], v[2],
                    splitFaces)) {
                addFace(fverts[0], fverts[1], fverts[2], null);
            }
            splitFaces.clear();
        }
        rebuildVertexIndex();
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("vertices: %d, faces: %d", getNumVertices(),
                getNumFaces());
    }
}
