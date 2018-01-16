package net.dericbourg.sandbox.graph;

import grph.Grph;
import grph.algo.search.DijkstraAlgorithm;
import grph.algo.search.SearchResult;
import grph.in_memory.InMemoryGrph;
import grph.path.ArrayListPath;

public class GrphSandox {
    public static void main(String[] args) {
        /*

                7
               /
              5-6
             /
        0-1-2
           \
            3-4

        */
        Grph g = new InMemoryGrph();

        for (int i = 0; i < 8; i++) {
            g.addVertex();
        }

        g.addSimpleEdge(0, 1, false);
        g.addSimpleEdge(1, 2, false);
        g.addSimpleEdge(1, 3, false);
        g.addSimpleEdge(3, 4, false);
        g.addSimpleEdge(2, 5, false);
        g.addSimpleEdge(5, 6, false);
        g.addSimpleEdge(5, 7, false);

        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(g.getVertexColorProperty());
        SearchResult pathsFrom0 = algorithm.compute(g, 0);
        ArrayListPath arrayListPath = pathsFrom0.computePathTo(4);
        for (long vertex : arrayListPath.toVertexArray()) {
            System.out.println(vertex);
        }
    }
}
