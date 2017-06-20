package net.dericbourg.sandbox.graph

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalax.collection.Graph
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._

object DijkstraWithPQ extends App {

  /*

            H
           /
          F-G
         /
    A-B-C
       \
        D-E

     */
  val g = Graph(
    "A" ~> "B",
    "B" ~> "C",
    "B" ~> "D",
    "D" ~> "E",
    "C" ~> "F",
    "F" ~> "G",
    "F" ~> "H"
  )

  println("Nodes")
  g.nodes.foreach(println)

  println("Edges")
  g.edges.foreach(println)

  def dijkstra(graph: Graph[String, DiEdge], source: String): (Map[String, Double], Map[String, Option[String]]) = {
    val dist: mutable.Map[String, Double] = mutable.Map(source -> 0.0)
    val prev: mutable.Map[String, Option[String]] = mutable.Map()

    implicit val minDistanceOrdering = Ordering.by((node: String) => dist(node)).reverse
    val vertices: mutable.PriorityQueue[String] = mutable.PriorityQueue()

    graph.nodes.foreach { node =>
      if (node != source) {
        dist(node) = Double.PositiveInfinity
        prev(node) = None
      }
      vertices += node
    }

    do {
      val min = vertices.dequeue()
      graph.get(min).diSuccessors.foreach { successor =>
        val alt = dist(min) + 1 // 1 == dist(min, successor)
        if (alt < dist(successor)) {
          dist(successor) = alt
          prev(successor) = Some(min)
        }
      }
    } while (vertices.nonEmpty)

    (dist.toMap, prev.toMap)
  }

  val (distances, previous) = dijkstra(g, "A")
  println(previous)

  def buildPath(prev: Map[String, Option[String]], from: String, to: String): Seq[String] = {
    var current = to
    val path = ListBuffer[String]()

    do {
      path += current
      current = prev(current).get // Not working for impossible paths
      println(path)
    } while (current != from) // Infinite-loop prone, yay \o/

    path.reverse.toList
  }

  val pathToG = buildPath(previous, "A", "G")
  println(pathToG.mkString("A -> ", " -> ", " *"))
}
