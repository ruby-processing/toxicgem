require 'toxiclibs.jar'

module Simulation
  include_package 'toxi.sim.grayscott'
  include_package 'toxi.math'
  include_package 'toxi.color'
end

module Physics
  include_package 'toxi.physics2d.behaviors'
  include_package 'toxi.physics2d'
  include_package 'toxi.geom'
  include_package 'toxi.math'
end

module Volume
  include_package 'toxi.volume'
end

module Gfx
  include_package 'toxi.processing'
end
