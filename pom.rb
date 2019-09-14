project 'toxiclibs' do

  model_version '4.0.0'
  id 'ruby-processing:toxiclibs:2.0.0'
  packaging 'jar'

  description 'toxiclibs-library for JRubyArt'

  organization 'ruby-processing', 'https://ruby-processing.github.io'

  developer 'monkstone' do
    name 'Martin Prout'
    email 'mamba2928@yahoo.co.uk'
    roles 'developer'
  end

  license 'LGPL 2', 'http://www.gnu.org/licenses/lgpl-2.1-standalone.html'

  issue_management 'https://github.com/ruby-processing/toxiclibs/issues', 'Github'

  source_control( :url => 'https://github.com/ruby-processing/toxiclibs',
                  :connection => 'scm:git:git://github.com/ruby-processing/toxiclibs.git',
                  :developer_connection => 'scm:git:git@github.com:ruby-processing/toxiclibs.git' )

  properties( 'target.release' => '11',
              'project.build.sourceEncoding' => 'UTF-8',
              'maven.compiler.target' => '1.8',
              'polyglot.dump.pom' => 'pom.xml'
            )

  jar 'args4j:args4j:2.0.31'
  jar 'org.processing:core:3.3.7'
  jar 'javax.xml.bind:jaxb-api:2.3.0'

  plugin( :compiler, '3.8.1',
          'release' =>  '${target.release}' )
  plugin( :jar, '3.1.1',
          'archive' => {
            'manifestFile' =>  'MANIFEST.MF'
          } )
  plugin :resources, '3.1.0'
  plugin :dependency, '3.1.1' do
    execute_goals( :id => 'default-cli',
                   'artifactItems' => [ { 'groupId' =>  'args4j',
                                          'artifactId' =>  'args4j',
                                          'version' =>  '2.0.31',
                                          'type' =>  'jar',
                                          'outputDirectory' =>  'lib' } ] )
  end


  build do
    default_goal 'package'
    source_directory 'src'
    final_name 'toxiclibs'
  end
end
