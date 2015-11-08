require_relative 'lib/toxiclibs/version'

def create_manifest
  title =  'Implementation-Title: toxiclibs (java extension for toxicgem)    '        
  version =  format('Implementation-Version: %s', Toxiclibs::VERSION)
  file = File.open('MANIFEST.MF', 'w') do |f|
    f.puts(title)
    f.puts(version)
  end
end

task default: [:init, :compile, :gem]

desc 'Create Manifest'
task :init do
  create_manifest
end

desc 'Build gem'
task :gem do
  sh "gem build toxiclibs.gemspec" 
end

desc 'Compile'
task :compile do
  sh "mvn package"
  sh "mvn dependency:copy"
  sh "mv target/toxiclibs.jar lib"
end

desc 'clean'
task :clean do
  Dir['./**/*.%w{jar gem}'].each do |path|
    puts "Deleting #{path} ..."
    File.delete(path)
  end
  FileUtils.rm_rf('./target')
end
