if RUBY_PLATFORM == 'java'  
  require 'toxiclibs.jar'
  
  module Simulation
    include_package 'toxi.sim.automata'
    include_package 'toxi.sim.dla'
    include_package 'toxi.sim.erosion'
    include_package 'toxi.sim.fluids'
    include_package 'toxi.sim.grayscott'
  end
  
  module Physics
    behavior2d = %w(AttractionBehavior2D ConstantForceBehavior2D GravityBehavior2D ParticleBehavior2D)
    behavior2d.each{ |klass| java_import format('toxi.physics2d.behaviors.%s', klass) }
    include_package 'toxi.physics2d.constraints'
    include_package 'toxi.physics2d'
    behavior3d = %w(AttractionBehavior3D ConstantForceBehavior3D GravityBehavior3D ParticleBehavior3D)
    behavior3d.each{ |klass| java_import format('toxi.physics3d.behaviors.%s', klass) }
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
    utils.each{ |klass| java_import format('toxi.util.%s', klass) }
    include_package 'toxi.geom.mesh.subdiv'
    include_package 'toxi.geom.nurbs'
    include_package 'toxi.math' 
    geom = %w(AABB Axis3D Circle Cone Vec2D Vec3D Vec4D Rect Shape2D Shape3D Sphere )
    geom.each{ |klass| java_import format('toxi.geom.%s', klass) }
  end
  
  module Gfx
    include_package 'toxi.processing'
  end
  
  # to disambiguate our own Vec2D and Vec3D we give new name to toxi versions  
  TVec2D = Toxi::Vec2D
  TVec3D = Toxi::Vec3D
  AABB = Toxi::AABB
  mesh = %w(Mesh3D TriangleMesh Face WEFace WETriangleMesh Vertex WEVertex LaplacianSmooth)
  mesh.each{ |klass| java_import format('toxi.geom.mesh.%s', klass) }
end
