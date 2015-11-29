require 'toxiclibs'

attr_reader :points, :bounds, :gfx

def settings
  size(400,400)
end

def setup
  sketch_title 'Test Rect'
  @points = []
  @bounds = Toxi::Rect.new(200, 200, 0, 0)
  @gfx = Gfx::ToxiclibsSupport.new(self)
end

def draw
  background(255)
  no_fill
  stroke(0)
  gfx.rect(bounds)
  fill(255, 0, 0)
  no_stroke
  points.each { |p| gfx.circle(p, 5) }
end

def mouse_pressed
  p = TVec2D.new(mouse_x, mouse_y)
  points << p
  bounds.grow_to_contain_point(p)
end
