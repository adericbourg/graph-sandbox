package net.dericbourg.sandbox.graph

import net.dericbourg.sandbox.graph.DijkstraWithPQ.{buildPath, dijkstra}

import scala.util.Random
import scalax.collection.Graph
import scalax.collection.GraphPredef._

object DijkstraWithPQVolume extends App {

  val NodeNumber = 20000

  val graph = Graph.fill(500000) {
    val fromNode = Random.nextInt(NodeNumber).toString
    val toNode = Random.nextInt(NodeNumber).toString

    fromNode ~> toNode
  }

  println(graph.nodes.size)
  println(graph.edges.size)

  val from = graph.nodes.head.value
  val to = graph.nodes.toVector(Random.nextInt(graph.nodes.size))

  val (distances, previous) = dijkstra(graph, from)
  val pathTo = buildPath(previous, from, to)
  println(pathTo.mkString(s"$from -> ", " -> ", " *"))

}
