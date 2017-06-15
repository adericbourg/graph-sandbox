package net.dericbourg.sandbox.graph

import scala.collection.mutable
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

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

  def dijkstra(graph: Graph[String, DiEdge], source: String): Unit = {
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

    println("Distances")
    println(dist)
  }

  dijkstra(g, "A")
}
