package net.dericbourg.sandbox.graph

import scala.collection.mutable

object PriorityQueues extends App {

  implicit val ordering = Ordering.by((stuff: Stuff) => stuff.a + stuff.b).reverse

  val pq = mutable.PriorityQueue(
    Stuff(0, 0),
    Stuff(1, 1),
    Stuff(1, 0),
    Stuff(0, 1)
  )

  pq.foreach(println)

}

case class Stuff(a: Int, b: Int)