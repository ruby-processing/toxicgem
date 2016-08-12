---
layout: post
title:  "Getting Started"
date:   2015-09-29 06:26:13
categories: jekyll update
---

### Toxiclibs geometry introducing Gfx, TVec2D and Toxi::Rect  

See [Calling Toxiclibs form JRubyArt][namespace] for why we use `Gfx`, and `Toxi` prefix, and why we use `TVec2D` and not `Vec2D`.  

`Gfx::ToxiclibsSupport` is used to render the `rect` and `circle`, it provides convenience functions that can render toxiclibs objects (in this case to screen), but it is not absolutely required to use toxiclibs. `Gfx::ToxiclibsSupport` uses the underlying JRubyArt app, and must be intialized with `self`, do this in setup.
The `Rect` class is basically a 2D bounding box that can grow to contain a point as in this sketch. `TVec2D` is very similar to the JRubyArt built in `Vec2D`, but is a pure java class and a core class in toxiclibs.

{% highlight ruby %}
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
{% endhighlight %}

[namespace]:{{site.github.url}}/namespace
