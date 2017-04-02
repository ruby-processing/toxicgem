## Toxiclibs for JRubyArt

![travis build](https://travis-ci.org/ruby-processing/toxicgem.svg)
[![Gem Version](https://badge.fury.io/rb/toxiclibs.svg)](https://badge.fury.io/rb/toxiclibs)

### Installing
```bash
gem install toxiclibs
```

### NB: Use version 0.4 for ruby-processing, and 0.9.2 for propane and JRubyArt

NB: Build is only failing because current version of processing is not available at maven central.
This gem provides Karsten Schmidts (aka toxi, @postspectacular) toxiclibs jars for JRubyArt. To compile the gem follow the instructions for [JRubyArt][]. Most parts of toxiclibs API is exposed in the latest version, (but only a few examples are included) in principle it should be possible to make all available!!! For this demonstration I have used up to date source code for version 21, since [toxis final release][] has not yet been released (although [Dan Shiffman][] has released a version for processing-3.0+). There are reported to be number of outstanding bugs with toxiclibs, if they affect you report it [here][] (or better fix them yourself and submit a pull request). Version 0.9.2 features java code updated to use java lambda (jdk8) and was compiled against processing-3.3 core.jar. The audio library was not working and has been dropped since version 0.9.2.

![grayscott image](http://4.bp.blogspot.com/-d4MiL4_0Njk/VFJMv6VUicI/AAAAAAAAEgY/fFAfrXDxNXM/s400/grayscott.png)

### Web Links

[Post Spectacular Home][]

[Toxiclibs Bitbucket][]

[Toxiclibs Documentation][]

[Example Usage][]

### Licensing

I should be clear that the original toxiclibs is the work of Karsten Schmidt see [Copyright][]

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
[Copyright]:https://github.com/ruby-processing/toxiclibs/COPYING.md
[Dan Shiffman]:https://github.com/shiffman/toxiclibs/tree/p3-update
