require 'toxiclibs'

attr_reader :gfx, :mesh, :spherical

def setup  
  sketch_title 'Spherical Harmonics Mesh Builder'
  ArcBall.init(self)
  @mesh = randomize_mesh
  @gfx = Gfx::MeshToVBO.new(self)
  no_stroke
  @spherical = gfx.mesh_to_shape(mesh, true)
end

def draw
  background(0)  
  lights
  shininess(16)
  directionalLight(255, 255, 255, 0, -1, 1)
  specular(255)
  fill(255)
  noStroke  
  shape(spherical)
end

def keyPressed
  if (key == 'r')
    @mesh = randomize_mesh
    no_stroke
    @spherical = gfx.mesh_to_colored_shape(mesh, true)
  end
end

def randomize_mesh
  m = (0..8).map { rand(0..8) }
  b = SurfaceMeshBuilder.new(SphericalHarmonics.new(m.to_java(:float)))
  b.create_mesh(nil, 80, 60)
end

def settings
  size(1024, 576, P3D)
end
