package net.dericbourg.sandbox.graph

import grph.Grph
import grph.algo.search.{DijkstraAlgorithm, SearchResult}
import grph.in_memory.InMemoryGrph
import grph.path.ArrayListPath

import scala.collection.mutable.ListBuffer


object GrphRealLineSample extends App {

  val g: Grph = new InMemoryGrph()

  // Create vertices from stations
  Station.getAll.foreach(station => g.addVertex(station.id))

  // Create connections from line maps
  val fullMap: Seq[Connection] = Line1.LineMap ++ Line2.LineMap ++ Line3.LineMap
  fullMap.foreach { connection =>
    g.addSimpleEdge(connection.from.id, connection.to.id, connection.directed)
  }

  // Create line connections
  Connections.Values.foreach { case (from, to) =>
    g.addSimpleEdge(from.id, to.id, false)
  }

  def getPath(from: Station, to: Station): Unit = {
    println(s"From ${from.name} to ${to.name}")
    val algorithm: DijkstraAlgorithm = new DijkstraAlgorithm(g.getVertexColorProperty)
    val pathsFrom0: SearchResult = algorithm.compute(g, from.id)
    val arrayListPath: ArrayListPath = pathsFrom0.computePathTo(to.id)
    for (vertex <- arrayListPath.toVertexArray) {
      println(vertex)
    }
  }

  getPath(Line1.Station11, Line2.Station27)
  getPath(Line3.Station33, Line3.Station31)
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

  import Connections._

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

  val LineMap: Seq[Connection] = Seq(
    Seq(Station11, Station12, Station13, Station14),
    Seq(Station14, Station15, Station16),
    Seq(Station14, Station17, Station18, Station19)
  ).asConnections()
}

object Line2 {

  import Connections._

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

  val LineMap: Seq[Connection] = Seq(
    Seq(Station21, Station22, Station23, Station24, Station25, Station26, Station27, Station28, Station29)
  ).asConnections()
}

object Line3 {

  import Connections._

  /*
     3.1 - 3.2 -> 3.3 -> 3.4
            ^             |
            |             |
            ---------------
   */

  val Station31 = Station(31, "3.1")
  val Station32 = Station(32, "3.2")
  val Station33 = Station(33, "3.3")
  val Station34 = Station(34, "3.4")

  val LineMap: Seq[Connection] = Seq(
    Seq(Station31, Station32),
    Seq(Station32, Station31),
    Seq(Station32, Station33, Station34, Station32),
  ).asConnections(directed = true)
}

case class Connection(from: Station, to: Station, directed: Boolean = false)

object Connections {
  val Values = Seq(
    (Line1.Station15, Line2.Station25)
  )

  implicit class ConnectionBuilder(lineSequence: Seq[Seq[Station]]) {
    def asConnections(directed: Boolean = false): Seq[Connection] = {
      lineSequence.flatMap { branch: Seq[Station] =>
        branch.sliding(2).map {
          case from :: to :: Nil => Connection(from, to, directed)
        }
      }
    }
  }

}
