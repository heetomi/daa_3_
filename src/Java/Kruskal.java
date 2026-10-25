import java.util.*;

public class Kruskal {
    static class Edge implements Comparable<Edge> {
        int u, v;
        long weight;
        int id;

        public Edge(int u, int v, long weight, int id) {
            this.u = u;
            this.v = v;
            this.weight = weight;
            this.id = id;
        }

        @Override
        public int compareTo(Edge other) {
            if (this.weight != other.weight) {
                return Long.compare(this.weight, other.weight);
            }
            return Integer.compare(this.id, other.id);
        }
    }

    public static Map<String, Object> solve(int n, int[][] rawEdges) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (n == 0) {
            result.put("cost", 0L);
            result.put("edge_ids", new ArrayList<>());
            result.put("components", 0);
            return result;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < rawEdges.length; i++) {
            edges.add(new Edge(rawEdges[i][0], rawEdges[i][1], rawEdges[i][2], i));
        }
        Collections.sort(edges);

        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int[] componentsCount = {n};

        // ДСУ лямбды
        java.util.function.Function<Integer, Integer> find = new java.util.function.Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) {
                if (parent[i] == i) return i;
                return parent[i] = this.apply(parent[i]);
            }
        };

        long totalCost = 0;
        List<Integer> edgeIds = new ArrayList<>();

        for (Edge e : edges) {
            int rootU = find.apply(e.u);
            int rootV = find.apply(e.v);
            if (rootU != rootV) {
                parent[rootU] = rootV;
                totalCost += e.weight;
                edgeIds.add(e.id);
                componentsCount[0]--;
            }
        }

        result.put("cost", totalCost);
        result.put("edge_ids", edgeIds);
        result.put("components", componentsCount[0]);
        return result;
    }
}