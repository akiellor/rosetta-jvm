require 'java'
require 'rubygems'
require 'clamp'

class StartCommand < Clamp::Command
  option '--port', 'PORT', 'server port', :default => 3000 do |s|
    Integer(s)
  end

  def execute
    Java::Rosetta::Application.boot self
  end
end

StartCommand.run