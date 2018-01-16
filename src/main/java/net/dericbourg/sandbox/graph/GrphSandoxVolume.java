package net.dericbourg.sandbox.graph;

import com.google.common.base.Stopwatch;
import grph.Grph;
import grph.algo.search.DijkstraAlgorithm;
import grph.algo.search.SearchResult;
import grph.in_memory.InMemoryGrph;
import grph.path.ArrayListPath;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GrphSandoxVolume {

    private static final int VERTEX_NUMBER = 10_000_000;
    private static final int EDGE_NUMBER = 500_000;
    private static final int DEST1 = 30;
    private static final int DEST2 = 3000;

    public static void main(String[] args) {

        Grph g = new InMemoryGrph();

        System.out.println("Build vertices");
        {
            Stopwatch stopwatch = Stopwatch.createStarted();
            for (int i = 0; i < VERTEX_NUMBER; i++) {
                g.addVertex();
            }
            System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }

        System.out.println("Build paths");
        {
            Stopwatch stopwatch = Stopwatch.createStarted();
            // Random paths
            Random r = new Random();
            for (int i = 0; i < EDGE_NUMBER; i++) {
                int src = r.nextInt(VERTEX_NUMBER);
                int dst = r.nextInt(VERTEX_NUMBER);
                g.addSimpleEdge(src, dst, false);
            }

            // Known path
            for (int i = 0; i < DEST1; i++) {
                g.addSimpleEdge(i, i + 1, false);
            }
            for (int i = 0; i < DEST2; i++) {
                g.addSimpleEdge(i, i + 1, false);
            }
            System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }

        computeTo(g, DEST1);
        computeTo(g, DEST2);
    }

    private static void computeTo(Grph g, int dest) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        {
            DijkstraAlgorithm algorithm = new DijkstraAlgorithm(g.getVertexColorProperty());
            SearchResult pathsFrom0 = algorithm.compute(g, 0);
            ArrayListPath arrayListPath = pathsFrom0.computePathTo(dest);
            arrayListPath.toVertexArray();
        }
        System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}
