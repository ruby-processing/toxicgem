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
  Toxiclibs java libraries wrapped in a rubygem. Compiled and tested with JRubyArt-1.2.5 and processing-3.2.1
  EOS
  spec.licenses = %w{MIT LGPL-3.0}
  spec.authors = %w{Karsten\ Schmidt Martin\ Prout}
  spec.email = 'mamba2928@yahoo.co.uk'
  spec.homepage = 'http://ruby-processing.github.io/toxicgem/'
  spec.files = `git ls-files -z`.split("\x0").reject { |f| f.match(%r{^(test|spec|features)/}) }
  spec.files << 'lib/toxiclibs.jar'
  spec.files << 'lib/args4j-2.0.31.jar' 
  spec.files << 'lib/joal-2.3.1.jar' 
  spec.require_paths = ['lib']
  spec.add_dependency 'jruby_art', '~> 1.2'
  spec.add_development_dependency 'rake', '~> 11.2', '>= 11.2.2'
end
