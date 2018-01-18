package net.dericbourg.sandbox.graph

import grph.Grph
import grph.algo.search.{DijkstraAlgorithm, SearchResult}
import grph.in_memory.InMemoryGrph
import grph.path.ArrayListPath

import scala.collection.mutable.ListBuffer

/*
 FIXME: Does not handle that kind of line
 Eg: https://www.ratp.fr/plans-lignes/metro/7b

         X -> X
         ^    |
         |    v
 X - X - X <- X
 */
object GrphRealLineSample extends App {

  val g: Grph = new InMemoryGrph()

  // Create vertices from stations
  Station.getAll.foreach(station => g.addVertex(station.id))

  // Create connections from line maps
  val fullMap: Seq[Seq[Station]] = Line1.LineMap ++ Line2.LineMap
  fullMap.foreach { branch: Seq[Station] =>
    branch.sliding(2).foreach {
      case from :: to :: Nil => g.addSimpleEdge(from.id, to.id, false)
      case other             => println(s"Panic: $other")
    }
  }

  // Create line connections
  Connections.Values.foreach { case (from, to) =>
    g.addSimpleEdge(from.id, to.id, false)
  }

  // Path from 1.1 to 2.7
  val algorithm: DijkstraAlgorithm = new DijkstraAlgorithm(g.getVertexColorProperty)
  val pathsFrom0: SearchResult = algorithm.compute(g, Line1.Station11.id)
  val arrayListPath: ArrayListPath = pathsFrom0.computePathTo(Line2.Station27.id)
  for (vertex <- arrayListPath.toVertexArray) {
    println(vertex)
  }
}

case class Line(name: String, connections: Seq[Seq[Station]])

case class Station(id: Int, name: String) {
  Station.register(this)
}

object Station {
  private val stations = ListBuffer()

  private[Station] def register(station: Station) = {
    this.synchronized {
      stations :+ station
    }
  }

  def getAll: Seq[Station] = stations.toList
}


object Line1 {
  /*
                          to line 2
                         /
                        1.5 - 1.6
                       /
   1.1 - 1.2 - 1.3 - 1.4
                       \
                        1.7 - 1.8 - 1.9
   */
  val Station11 = Station(11, "1.1")
  val Station12 = Station(12, "1.2")
  val Station13 = Station(13, "1.3")
  val Station14 = Station(14, "1.4")
  val Station15 = Station(15, "1.5")
  val Station16 = Station(16, "1.6")
  val Station17 = Station(17, "1.7")
  val Station18 = Station(18, "1.8")
  val Station19 = Station(19, "1.9")

  val LineMap = Seq(
    Seq(Station11, Station12, Station13, Station14),
    Seq(Station14, Station15, Station16),
    Seq(Station14, Station17, Station18, Station19)
  )
}

object Line2 {
  /*
   2.1 - 2.2 - 2.3 - 2.4 - 2.5 - 2.6 - 2.7 - 2.8 - 2.9
                             \
                              to line 1
   */
  val Station21 = Station(21, "2.1")
  val Station22 = Station(22, "2.2")
  val Station23 = Station(23, "2.3")
  val Station24 = Station(24, "2.4")
  val Station25 = Station(25, "2.5")
  val Station26 = Station(26, "2.6")
  val Station27 = Station(27, "2.7")
  val Station28 = Station(28, "2.8")
  val Station29 = Station(29, "2.9")

  val LineMap = Seq(
    Seq(Station21, Station22, Station23, Station24, Station25, Station26, Station27, Station28, Station29)
  )
}

object Connections {
  val Values = Seq(
    (Line1.Station15, Line2.Station25)
  )
}
