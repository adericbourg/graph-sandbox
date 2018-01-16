name := "graph-sandbox"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies += "org.scala-graph" %% "graph-core" % "1.11.5"

// Heuristic search
libraryDependencies += "es.usc.citius.hipster" % "hipster-all" % "1.0.1"

// High perf
resolvers += "lhogie.i3s.maven.repo" at "http://www.i3s.unice.fr/~hogie/maven_repository/"
libraryDependencies += "grph" % "grph" % "2.1.2"
