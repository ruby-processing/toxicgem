project 'toxiclibs' do

  model_version '4.0.0'
  id 'ruby-processing:toxiclibs:0.7.0'
  packaging 'jar'

  description 'toxiclibs-library for JRubyArt'
  
  organization 'ruby-processing', 'https://ruby-processing.github.io'

  developer 'monkstone' do
    name 'Martin Prout'
    email 'martin_p@lineone.net'
    roles 'developer' 
  end

  issue_management 'https://github.com/ruby-processing/toxiclibs/issues', 'Github'

  source_control( :url => 'https://github.com/ruby-processing/toxiclibs',
                  :connection => 'scm:git:git://github.com/ruby-processing/toxiclibs.git',
                  :developer_connection => 'scm:git:git@github.com:ruby-processing/toxiclibs.git' )

  properties( 'maven.compiler.source' => '1.8',
              'project.build.sourceEncoding' => 'UTF-8',
              'maven.compiler.target' => '1.8',
              'polyglot.dump.pom' => 'pom.xml'
            )

  jar 'org.jogamp.joal:joal-main:2.3.1'
  jar 'args4j:args4j:2.0.31'
  jar 'org.processing:core:3.0.1'

  plugin( :compiler, '3.1',
          'source' =>  '1.8',
          'target' =>  '1.8' )
  plugin( :jar, '2.4',
          'archive' => {
            'manifestFile' =>  'MANIFEST.MF'
          } )
  plugin :resources, '2.6'
  plugin :dependency, '2.10' do
    execute_goals( :id => 'default-cli',
                   'artifactItems' => [ { 'groupId' =>  'args4j',
                                          'artifactId' =>  'args4j',
                                          'version' =>  '2.0.31',
                                          'type' =>  'jar',
                                          'outputDirectory' =>  'lib' },
                                        { 'groupId' =>  'org.jogamp.joal',
                                          'artifactId' =>  'joal',
                                          'version' =>  '2.3.1',
                                          'type' =>  'jar',
                                          'outputDirectory' =>  'lib' } ] )
  end


  build do
    default_goal 'package'
    source_directory 'src'
    final_name 'toxiclibs'
  end
end
