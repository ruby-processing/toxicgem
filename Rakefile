require 'rubygems'
require 'rake'
require 'rake/clean'
require 'rubygems/package_task'
require 'rdoc/task'
require 'rake/testtask'
require 'rspec/core/rake_task'
require_relative 'lib/toxiclibs/version'

spec = Gem::Specification.new do |s|
  s.name = 'toxiclibs'
  s.version = Toxiclibs::VERSION
  s.has_rdoc = true
  s.extra_rdoc_files = ['README.md', 'LICENSE.md']
  s.summary = 'Experimental gem for some toxiclibs'
  s.description = 'A gem wrapper for some toxiclibs jars'
  s.license = 'GPLv3'
  s.authors = ['Karsten Schmidt', 'Martin Prout']
  s.email = 'martin_p@lineone.net'
  s.homepage = 'https://github.com/ruby-processing/toxiclibs'
  s.files = %w(LICENSE.md README.md Rakefile) + FileList['lib/**/*.rb', 'example/**/*.rb']
  s.files << 'lib/toxiclibs.jar' 
  s.require_path = 'lib'
  s.add_development_dependency "rake", "~> 10.3"
  s.add_development_dependency "rake-compiler", "~> 0.9"
  s.requirements << 'A decent graphics card'
  s.requirements << 'java runtime >= 1.7+'
  s.requirements << 'jruby_art = 0.2+'
end

# -*- ruby -*-

require 'java'
require 'rake/javaextensiontask'

# -*- encoding: utf-8 -*-
require 'psych'

def copy_jars(name, dest)
  conf = '~/.jruby_art/config.yml'
  begin
    path = File.expand_path(conf)
    rp_config = (Psych.load_file(path))
    source= "#{rp_config["PROCESSING_ROOT"]}/core/library/"    
  rescue
    raise "WARNING: you must set PROCESSING_ROOT in #{conf} compile"
  end
	body = proc {
	  Dir["#{source}/core.jar"].each do |f|
	    puts "Copying #{f} To #{dest}"
	    FileUtils.cp f, dest
	  end
	}
	Rake::Task.define_task(name, &body)	
end

copy_jars(:processing_jars, 'lib')

Rake::JavaExtensionTask.new('toxi') do |ext|
  jar = 'lib/core.jar'
  ext.classpath = File.expand_path(jar)
  ext.name = 'toxiclibs'
  ext.debug = true
  ext.lib_dir = 'lib'
  ext.source_version='1.7'
  ext.target_version='1.7'
end

Gem::PackageTask.new(spec) do |p|
  p.gem_spec = spec
  p.need_tar = true
  p.need_zip = true
end

Rake::RDocTask.new do |rdoc|
  files = ['README.md', 'LICENSE.md'] + FileList['lib/**/*.rb']
  
  rdoc.rdoc_files.add(files)
  rdoc.main = "README.md" # page to start on
  rdoc.title = "Toxiclibs Docs"
  rdoc.rdoc_dir = 'doc/rdoc' # rdoc output folder
  rdoc.options << '--line-numbers'
end

Rake::TestTask.new do |t|
  t.test_files = FileList['test/**/*.rb']
end

RSpec::Core::RakeTask.new do |spec|
  spec.pattern = 'spec/*_spec.rb'
  spec.rspec_opts = [Dir["lib"].to_a.join(':')]
end


