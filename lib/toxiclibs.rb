require 'toxiclibs.jar'

module Simulation
  include_package 'toxi.sim.automata'
  include_package 'toxi.sim.dla'
  include_package 'toxi.sim.erosion'
  include_package 'toxi.sim.fluids'
  include_package 'toxi.sim.grayscott'
end

module Physics
  include_package 'toxi.physics2d.behaviors'
  include_package 'toxi.physics2d.constraints'
  include_package 'toxi.physics2d'
  include_package 'toxi.physics3d.behaviors'
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
  include_package 'toxi.util.events'
  include_package 'toxi.geom'
  include_package 'toxi.geom.mesh'
  include_package 'toxi.geom.mesh.subdiv'
  include_package 'toxi.geom.nurbs'
  include_package 'toxi.math'  
end

module Gfx
  include_package 'toxi.processing'
end
