# The Nature of Code
# <http://www.shiffman.net/teaching/nature>
# Spring 2010
# Toxiclibs example: http://toxiclibs.org/

# Force directed graph
# Heavily based on: http://code.google.com/p/fidgen/
# A cluster is a grouping of Nodes
class Cluster
  include Processing::Proxy
  attr_reader :nodes, :diameter, :physics

  # We initialize a Cluster with a number of nodes, a diameter, and centerpoint
  def initialize(p, n, d, center)
    # Set the diameter
    @diameter, @physics = d, p
    # Create the nodes
    @nodes = (0..n).map { Node.new(center.add(TVec2D.randomVector)) }
    # Connect all the nodes with a Spring
    nodes[0..nodes.size - 2].each_with_index do |ni, i|
      nodes[i + 1..nodes.size - 1].each do |nj|
        # A Spring needs two particles, a resting length, and a strength
        physics.addSpring(Physics::VerletSpring2D.new(ni, nj, diameter, 0.01))
      end
    end
  end

  def display
    # Show all the nodes
    nodes.each(&:display)
  end

  # Draw all the internal connections
  def show_connections
    stroke(0, 150)
    stroke_weight(2)
    nodes[0..nodes.size - 2].each_with_index do |pi, i|
      nodes[i + 1..nodes.size - 1].each do |pj|
        line(pi.x, pi.y, pj.x, pj.y)
      end
    end
  end
end
