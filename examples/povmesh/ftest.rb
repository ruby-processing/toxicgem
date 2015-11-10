require 'toxiclibs'

attr_reader :gfx, :mesh0, :mesh1, :mesh2

def settings
  size(200, 200, P3D)
  smooth 4
end

def setup
  sketch_title('FTest')
  @gfx = Gfx::ToxiclibsSupport.new(self)
  # define a rounded cube using the SuperEllipsoid surface function
  vert = AABB.fromMinMax(TVec3D.new(-1.0, -3.5, -1.0), TVec3D.new(1.0, 3.5, 1.0))
  box = AABB.fromMinMax(TVec3D.new(1.0, -1.5, -1.0), TVec3D.new(3.0, -3.5, 1.0))
  box2 = AABB.fromMinMax(TVec3D.new(1.0, 2.0, -1.0), TVec3D.new(3.0, 0.0, 1.0))
  @mesh0 = box.to_mesh
  @mesh1 = vert.to_mesh
  @mesh2 = box2.to_mesh
  mesh0.add_mesh(mesh1)
  mesh0.add_mesh(mesh2)
  mesh0.compute_face_normals
  mesh0.compute_vertex_normals
  fileID = 'FTest'
  pm = Gfx::POVMesh.new(self)
  file = java.io.File.new(sketchPath(fileID + '.inc'))
  pm.begin_save(file)
  pm.set_texture(Gfx::Textures::CHROME)
  pm.saveAsPOV(mesh0.faceOutwards, false)
  #        pm.set_texture(Textures::RED)
  #        pm.saveAsPOV(mesh1, false)
  #        pm.set_texture(Textures::WHITE)
  #        pm.saveAsPOV(mesh2, false)
  pm.end_save
  #   exit
end

def draw
  background 50, 50, 200
  lights
  translate(width / 2, height / 2)
  scale(10)
  rotateY(20.radians)
  gfx.choose_stroke_fill(false, Toxi::TColor::WHITE, Toxi::TColor::RED)
  gfx.mesh(mesh0)
end
