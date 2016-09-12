---
layout: post
title:  "Calling Toxiclibs from JRubyArt"
date:   2015-11-29 06:26:13
categories: jekyll update
permalink: /namespace/
---

<style>
table{
    border-collapse: collapse;
    border-spacing: 0;
    border:2px solid #0000FF;
}

th{
    border:2px solid #0000FF;
}
</style>

|Description   |  Java / Processing        |  JRubyArt                      |
|----------    |:-------------:            |------:                         |
|2D vector     |Vec2D                      |TVec2D <sup>a</sup>             |
|2D vector     |Vec3D                      |TVec3D <sup>a</sup>             |
|bounding box  |AABB                       |AABB <sup>b</sup>               |
|Other geometry|eg Matrix4x4               |Toxi::Matrix4x4<sup>c</sup>     |
|Volume        |eg VolumetricSpace         |VolumetricSpace<sup>b</sup>     |
|Color         |eg ToneMap                 |Toxi::ToneMap<sup>c</sup>       |
|Graphics Util |not available              |Gfx::MeshToVBO.new(self)<sup>d</sup> |

-----

<sup>a</sup>Renamed to avoid clash with JRubyArt built in Vec2D and Vec3D

<sup>b</sup>Classes unlikely to have namespace clash, see [toxiclibs.rb][library]

<sup>c</sup>Using Toxi prefix to avoid possible namespace clash, see [toxiclibs.rb][library]

<sup>d</sup>Using Gfx prefix to provide access to the `toxi.processing` package, see [toxiclibs.rb][library]. Most common usage would be to call `ToxiclibsSupport` see below:-

{% highlight java %}
ToxiclibsSupport gfx = new ToxiclibsSupport(this);
{% endhighlight %}

in JRubyArt

{% highlight ruby %}
@gfx = Gfx::ToxiclibsSupport.new(self)
{% endhighlight %}


[library]:https://github.com/ruby-processing/toxicgem/blob/master/lib/toxiclibs.rb