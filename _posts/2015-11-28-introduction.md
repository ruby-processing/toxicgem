---
layout: post
title:  "Introducing the Toxiclibs Gem"
date:   2015-11-28 07:52:09 +0000
categories: toxiclibs update
---
### Toxiclibs for JRubyArt

### Installing

```bash
gem install toxiclibs
```

_NB: Use version 0.4 for ruby-processing_

Here I have created create a gem to use Karsten Schmidts (aka toxi, @postspectacular) toxiclibs jars in JRubyArt. To compile the gem follow the instructions for [JRubyArt][]. Most parts of toxiclibs API is exposed in the latest version, (but only a few examples are included) in principle it should be possible to make all available!!! For this demonstration I have used up to date source code for version 21, since [toxis final release][] may never materialise (he's probably got more interesting things to do). There are reported to be number of outstanding bugs with toxiclibs, if they affect you report it [here][] (or better fix them yourself and submit a pull request). Version 0.5.0 features java code updated to use java lambda (jdk8) and was compiled against processing-3.0.1 core.jar.  Added features are export Mesh to PShape and export Mesh to [povray mesh2](http://www.povray.org/documentation/3.7.0/r3_4.html#r3_4_5_2_4), `ColorList` can return a ruby string `to_ruby_string` of the form `%w(#ffffff #ff0000)` ie a web palette.

![grayscott image](https://4.bp.blogspot.com/-d4MiL4_0Njk/VFJMv6VUicI/AAAAAAAAEgY/fFAfrXDxNXM/s400/grayscott.png)

### Web Links

[Post Spectacular Home][]

[Toxiclibs Bitbucket][]

[Toxiclibs Documentation][]

[Example Usage][]

### Licensing

I should be clear that the original toxiclibs is the work of Karsten Schmidt:-

Copyright (c) 2010 Karsten Schmidt

This demo & library is free software you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation either
version 2.1 of the License, or (at your option) any later version.

[toxis final release]:http://hg.postspectacular.com/toxiclibs/issue/54/update-toxiclibs-for-processing-21
[JRubyArt]:https://github.com/ruby-processing/JRubyArt
[Post Spectacular Home]:http://postspectacular.com/
[Toxiclibs Bitbucket]:http://hg.postspectacular.com/
[Toxiclibs Documentation]:http://toxiclibs.org/
[Example Usage]:https://github.com/ruby-processing/toxicgem/tree/master/examples
[here]:https://github.com/ruby-processing/toxiclibs/issues
