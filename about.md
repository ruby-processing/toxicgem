---
layout: page
title: About
permalink: /about/
---
### The java library ###

[Toxiclibs][toxiclibs] is an independent, open source library collection for computational design tasks with Java & Processing developed by Karsten “toxi” Schmidt. The classes are purposefully kept fairly generic in order to maximize re-use in different contexts ranging from generative design, animation, interaction/interface design, data visualization to architecture and digital fabrication, use as teaching tool and more.


After over 3 years of ongoing (almost daily) development the collection consists of:

18 packages bundled into 7 libraries

The libraries have been designed to have no further dependencies in order to maximize reuse and flexibility. Even though these libraries have been mainly developed for use in Processing related projects, there is no explicit dependency on the monolithic PApplet or any other classes of the Processing toolkit. This is intentional & should be considered as a feature.

### The toxiclibs gem ###

The toxiclibs gem is a derived work based on the un-released version-22 of toxis libraries (code updated to java 7 syntax up to version 0.4, and therafter updated to use java 8 syntax). The current version will not work with versions of processing prior to processing-3.0 release.

### Packages ###

* <s>toxi.audio</s>
   * <s>JOAL based 3D spatial audio sound manager</s> work in progress
* toxi.color
  * float based color type with RGB, HSV, CMYK accessors & operators
  * named colors & hues
  * color lists, ranges, themes
  * color theory strategies
  * super flexible color sorting with many presets
* toxi.geom
  * 2d/3d vector maths
  * AABB, sphere, plane, triangle, ray
  * octree
  * splines
  * 4×4 matrix
  * intersection tests
  * Mesh container
  * OBJ and STL exporters (able to support massive files, with optional STL colour support)
* toxi.math
   * common interpolation methods (demo included in download)
   * unit translators (between dpi, points, mm, pixels, useful for PDF generation)
   * some (collected) faster & convenient implementations of assorted common math functions (similar to processing, only without dependency on PApplet)
   * sin/cos lookup tables
   * Perlin & Simplex noise
   * wave generators
* toxi.physics
   * basic 2d & 3D particle physics engine with Verlet integration
* toxi.image.util
   * grayscale image filters
* toxi.util.datatypes
* toxi.volume
  * voxels, marching cubes, isosurface etc
* toxi.sim  
  * wolfram, erosion, fluids etc

[toxiclibs]:http://toxiclibs.org/