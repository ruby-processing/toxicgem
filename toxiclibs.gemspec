# coding: utf-8
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'toxiclibs/version'

Gem::Specification.new do |spec|
  spec.name = 'toxiclibs'
  spec.version = Toxiclibs::VERSION
  spec.has_rdoc = true
  spec.extra_rdoc_files = %w{README.md LICENSE.md}
  spec.summary = %q{Experimental gem for some toxiclibs}
  spec.description = %q{A gem wrapper for some toxiclibs jars}
  spec.license = 'GPLv3'
  spec.authors = %w{Karsten/ Schmidt Martin/ Prout}
  spec.email = 'martin_p@lineone.net'
  spec.homepage = 'https://github.com/ruby-processing/toxiclibs'
  spec.files = `git ls-files -z`.split("\x0").reject { |f| f.match(%r{^(test|spec|features)/}) }
  spec.files << 'lib/toxiclibs.jar'
  spec.files << 'lib/args4j-2.0.31.jar' 
  spec.require_paths = ['lib']
  spec.add_dependency 'jruby_art', '~> 1.0' 
  spec.add_development_dependency "rake", "~> 10.0"
  spec.add_development_dependency "minitest", "~> 5.8"
  spec.platform = 'java'
  spec.requirements << 'A decent graphics card'
  spec.requirements << 'java runtime >= 1.8+'
  spec.requirements << 'processing = 3.0.1+'
  spec.requirements << 'maven = 3.3.3'
  spec.requirements << 'jruby_art = 1.0+'
end


