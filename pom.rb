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

  properties( 'source.directory' => 'src',
              'target.release' => '11',
              'project.build.sourceEncoding' => 'UTF-8',
              'polyglot.dump.pom' => 'pom.xml'
            )

  jar 'org.processing:core:3.3.7'


  plugin(:compiler, '3.8.1',
         'release' => '${target.release}')
  plugin(:javadoc, '2.10.4',
         'detectOfflineLinks' => 'false',
         'links' => ['${processing.api}',
                     '${jruby.api}'])
          plugin :jdeps, '3.1.2' do
            execute_goals 'jdkinternals', 'test-jdkinternals'
          end

  build do
    resource do
      excludes '**/**/*.java'
    end
    default_goal 'package'
    source_directory '${source.directory}/main/java'
    final_name 'toxiclibs'
  end
end
