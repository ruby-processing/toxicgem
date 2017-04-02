if RUBY_PLATFORM == 'java'
  require 'toxiclibs.jar'

  def import_class_list(list, string)
    list.each { |klass| java_import format(string, klass) }
  end

  module Simulation
    include_package 'toxi.sim.automata'
    include_package 'toxi.sim.dla'
    erosion = %w(ErosionFunction TalusAngleErosion ThermalErosion)
    erosion_format = 'toxi.sim.erosion.%s'
    import_class_list(erosion, erosion_format)
    fluids = %w(FluidSolver2D FluidSolver3D)
    fluids_format = 'toxi.sim.fluids.%s'
    import_class_list(fluids, fluids_format)
    java_import 'toxi.sim.grayscott.GrayScott'
  end

  module Physics
    behavior2d = %w(AttractionBehavior2D ConstantForceBehavior2D GravityBehavior2D ParticleBehavior2D)
    behavior2d_format = 'toxi.physics2d.behaviors.%s'
    import_class_list(behavior2d, behavior2d_format)
    include_package 'toxi.physics2d.constraints'
    include_package 'toxi.physics2d'
    behavior3d = %w(AttractionBehavior3D ConstantForceBehavior3D GravityBehavior3D ParticleBehavior3D)
    behavior3d_format = 'toxi.physics3d.behaviors.%s'
    import_class_list(behavior3d, behavior3d_format)
    include_package 'toxi.physics3d.constraints'
    include_package 'toxi.physics3d'
  end

  module Volume
    include_package 'toxi.volume'
  end

  module Toxi
    include_package 'toxi.color'
    include_package 'toxi.color.theory'
    include_package 'toxi.util.datatypes'
    java_import 'toxi.util.events.EventDispatcher'
    utils = %w{DateUtils FileSequenceDescriptor FileUtils}
    utils_format = 'toxi.util.%s'
    import_class_list(utils, utils_format)
    subdiv = %w(CentroidSubdiv DisplacementSubdivision DualDisplacementSubdivision
                DualSubdivision EdgeLengthComparator FaceCountComparator
                MidpointDisplacementSubdivision MidpointSubdiv MidpointSubdivision
                NewSubdivStrategy NormalDisplacementSubdivision SubdivisionStrategy
                TriSubdivision)
    subdiv_format = 'toxi.geom.mesh.subdiv.%s'
    import_class_list(subdiv, subdiv_format)
    nurbs = %w(BasicNurbsCurve BasicNurbsSurface ControlNet CurveCreator CurveUtils
               InterpolationException KnotVector NurbsCreator NurbsCurve NurbsMeshCreator
               NurbsSurface)
    nurbs_format = 'toxi.geom.nurbs.%s'
    import_class_list(nurbs, nurbs_format)
    include_package 'toxi.math'
    noise = %w(SimplexNoise PerlinNoise)
    noise_format = 'toxi.math.noise.%s'
    import_class_list(noise, noise_format)
    geom = %w(AABB Axis3D AxisAlignedCylinder BernsteinPolynomial BezierCurve2D
              BezierCurve3D BooleanShapeBuilder BoxIntersector Circle CircleIntersector
              Cone ConvexPolygonClipper CoordinateExtractor Ellipse GMatrix GVector
              GlobalGridTesselator GridTesselator Intersector2D Intersector3D IsectData2D
              IsectData3D Line2D Line3D LineStrip2D LineStrip3D LocalGridTesselator Matrix3d
              Matrix4f Matrix4x4 MatrixSizeException OctreeVisitor Origin3D Plane
              PlaneIntersector PointCloud3D PointOctree PointQuadtree Polygon2D
              PolygonClipper2D PolygonTesselator QuadtreeVisitor Quaternion Ray2D Ray3D
              Ray3DIntersector ReadonlyVec2D ReadonlyVec3D ReadonlyVec4D Rect Reflector3D
              Shape2D Shape3D SingularMatrixException SpatialBins SpatialIndex Sphere
              SphereIntersectorReflector Spline2D Spline3D SutherlandHodgemanClipper
              Triangle2D Triangle3D TriangleIntersector Vec2D Vec3D Vec4D VecMathUtil
              XAxisCylinder YAxisCylinder ZAxisCylinder)
    geom_format = 'toxi.geom.%s'
    import_class_list(geom, geom_format)
  end

  module Gfx
    include_package 'toxi.processing'
  end

  # to disambiguate our own Vec2D and Vec3D we give new name to toxi versions
  TVec2D = Toxi::Vec2D
  TVec3D = Toxi::Vec3D
  AABB = Toxi::AABB
  # alias Toxi::TColor
  TColor = Toxi::TColor
  volume = %w(AdditiveBrush ArrayIsoSurface BoxBrush BrushMode HashIsoSurface
              IsoSurface MarchingCubesIndex MeshLatticeBuilder MeshVoxelizer
              MultiplyBrush PeakBrush ReplaceBrush RoundBrush VolumetricBrush
              VolumetricHashMap VolumetricSpace VolumetricSpaceArray)
  volume_format = 'toxi.volume.%s'
  import_class_list(volume, volume_format)
  mesh = %w(BezierPatch BoxSelector DefaultSTLColorModel DefaultSelector Face
            LaplacianSmooth MaterialiseSTLColorModel Mesh3D MeshIntersector OBJWriter
            PLYWriter PlaneSelector STLColorModel STLReader STLWriter SphereFunction
            SphericalHarmonics SuperEllipsoid SurfaceFunction SurfaceMeshBuilder
            Terrain TriangleMesh Vertex VertexSelector WEFace WEMeshFilterStrategy
            WETriangleMesh WEVertex WingedEdge)
  mesh_format = 'toxi.geom.mesh.%s'
  import_class_list(mesh, mesh_format)
end
