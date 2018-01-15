package net.dericbourg.sandbox.graph;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

public class HipsterPath {

    public static Algorithm.SearchResult getResult() {
        HipsterDirectedGraph<String, Double> graph =
                GraphBuilder.<String, Double>create()
                        .connect("A").to("B").withEdge(4d)
                        .connect("A").to("C").withEdge(2d)
                        .connect("B").to("C").withEdge(5d)
                        .connect("B").to("D").withEdge(10d)
                        .connect("C").to("E").withEdge(3d)
                        .connect("D").to("F").withEdge(11d)
                        .connect("E").to("D").withEdge(4d)
                        .createDirectedGraph();

        SearchProblem p = GraphSearchProblem
                .startingFrom("A")
                .in(graph)
                .takeCostsFromEdges()
                .build();

        return Hipster.createDijkstra(p).search("F");
    }

}
