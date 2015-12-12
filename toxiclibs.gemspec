# coding: utf-8
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'toxiclibs/version'

Gem::Specification.new do |spec|
  spec.name = 'toxiclibs'
  spec.version = Toxiclibs::VERSION
  spec.has_rdoc = true
  spec.extra_rdoc_files = %w{README.md LICENSE.md}
  spec.summary = %q{Updated and extended toxiclibs libraries for JRubyArt}
  spec.description =<<-EOS
  Toxiclibs java libraries wrapped in a rubygem. Updated to use joal-2.3.1
  java lambda expressions (available since jdk8). Also new since version 
  0.5.0 are 3D Mesh to PShape and 3D mesh to Povray mesh2 utilities.
  EOS
  spec.licenses = %w{MIT LGPL-3.0}
  spec.authors = %w{Karsten\ Schmidt Martin\ Prout}
  spec.email = 'martin_p@lineone.net'
  spec.homepage = 'http://ruby-processing.github.io/toxicgem/'
  spec.files = `git ls-files -z`.split("\x0").reject { |f| f.match(%r{^(test|spec|features)/}) }
  spec.files << 'lib/toxiclibs.jar'
  spec.files << 'lib/args4j-2.0.31.jar' 
  spec.files << 'lib/joal.2.3.1.jar' 
  spec.require_paths = ['lib']
  spec.add_dependency 'jruby_art', '~> 1.0', '>= 1.0.1' 
  spec.add_development_dependency 'rake', '~> 10.4', '>= 10.4.2'
  spec.development_dependency 'maven', '~> 3.3', '>= 3.3.3'
  spec.platform = 'java'
  spec.requirements << 'A decent graphics card'
  spec.requirements << 'java runtime >= 1.8+'
  spec.requirements << 'processing = 3.0.1+'
end
