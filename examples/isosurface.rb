#
# This example implements a custom VolumetricSpace using an implicit function
# to calculate each voxel. This is slower than the default array or HashMap
# based implementations, but also has much less memory requirements and so might
# be an interesting and more viable approach for very highres voxel spaces
# (e.g. >32 million voxels). This implementation here also demonstrates how to
# achieve an upper boundary on the iso value (in addition to the one given and
# acting as lower threshold when computing the iso surface)
#
# Usage:
# move mouse to rotate camera
# w: toggle wireframe on/off
# -/=: zoom in/out
# l: apply laplacian mesh smooth
#
#

#
# Copyright (c) 2010 Karsten Schmidt & ruby-processing version Martin Prout 2012
# This sketch relies on a custom toxiclibscore library for PovRAY export
#
# This library is free software you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation either
# version 2.1 of the License, or (at your option) any later version.
#
# http://creativecommons.org/licenses/LGPL/2.1/
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
#

require 'toxiclibs'
load_library 'vecmath'

RES = 64
ISO = 0.2
MAX_ISO = 0.66
attr_reader :mesh, :gfx, :curr_zoom, :is_wire_frame

def setup
  size(720, 720, P3D)
  ArcBall.init(self)
  @gfx = Gfx::ToxiclibsSupport.new(self)
  vol = EvaluatingVolume.new(Toxi::Vec3D.new(400, 400, 400), RES, RES, RES, MAX_ISO)
  surface = Volume::HashIsoSurface.new(vol)
  @mesh = Volume::WETriangleMesh.new
  surface.compute_surface_mesh(mesh, ISO)
  @is_wire_frame = false
end

def draw
  background(0)
  if is_wire_frame
    no_fill
    stroke(255)
  else
    fill(255)
    no_stroke
    lights
  end
  @gfx.mesh(mesh, true)
end

def key_pressed
  case key
  when 'w', 'W'
    @is_wire_frame = !is_wire_frame
  when 'l', 'L'
    Volume::LaplacianSmooth.new.filter(mesh, 1)
  when 's', 'S'
    save_frame('implicit.png')
  end
end

# Creating a volumetric space class
#
class EvaluatingVolume < Volume::VolumetricSpace
  include Processing::Proxy
  attr_reader :upper_bound
  FREQ = PI * 3.8

  def initialize(scal_vec, resX, resY, resZ, upper_limit)
    super(scal_vec, resX, resY, resZ)
    @upper_bound = upper_limit
  end

  def clear
    # nothing to do here
  end

  def getVoxelAt(i)
    getVoxel(i % resX, (i % sliceRes) / resX, i / sliceRes)
  end

  def getVoxel(x, y, z)  # can't overload so we renamed
    val = 0
    if x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1
      xx = x * 1.0 / resX - 0.5  # NB: careful about integer division !!!
      yy = y * 1.0 / resY - 0.5
      zz = z * 1.0 / resZ - 0.5
      val = Math.sin(xx * FREQ) + Math.cos(yy * FREQ) + Math.sin(zz * FREQ)
      # val = Math.sin(xx * FREQ) * (xx * FREQ) + Math.sin(yy * FREQ) * (yy * FREQ) + Math.sin(zz * FREQ) * (zz * FREQ)
      val = 0 if val > upper_bound
    end
    val
  end
end
