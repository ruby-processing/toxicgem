project 'toxiclibs' do

  model_version '4.0.0'
  id 'ruby-processing:toxiclibs:2.0.0'
  packaging 'jar'

  description 'toxiclibs-library for JRubyArt'

  developer 'monkstone' do
    name 'Martin Prout'
    email 'mamba2928@yahoo.co.uk'
    roles 'developer'
  end

  issue_management 'https://github.com/ruby-processing/toxiclibs/issues', 'Github'

  source_control( :url => 'https://github.com/ruby-processing/toxiclibs',
                  :connection => 'scm:git:git://github.com/ruby-processing/toxiclibs.git',
                  :developer_connection => 'scm:git:git@github.com:ruby-processing/toxiclibs.git' )

  properties( 'source.directory' => 'src',
              'polyglot.dump.pom' => 'pom.xml',
              'project.build.sourceEncoding' => 'UTF-8',
              'target.release' => '11',
              'polyglot.dump.pom' => 'pom.xml',
              'maven.deploy.skip' => 'true'
            )

  jar 'org.processing:core:4.0.0'

  overrides do
    plugin( :compiler, '3.8.1',
            'release' =>  '11' )
    plugin :javadoc, '2.10.4'
    plugin :jar, '3.2.0'
    plugin :jdeps, '3.1.2' do
      execute_goals 'jdkinternals', 'test-jdkinternals'
    end

  end


  build do
    default_goal 'package'
    source_directory '${source.directory}/main/java'
    final_name 'toxiclibs'
  end

end
