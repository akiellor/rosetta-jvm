require 'jython/class'

App = Jython::Class.import 'App', :from => 'util.app'

run App.new