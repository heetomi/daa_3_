import java.util.*;

public class Prim {
    static class EdgeEntry implements Comparable<EdgeEntry> {
        int to;
        long weight;
        int id;

        public EdgeEntry(int to, long weight, int id) {
            this.to = to;
            this.weight = weight;
            this.id = id;
        }

        @Override
        public int compareTo(EdgeEntry other) {
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

        List<List<EdgeEntry>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int i = 0; i < rawEdges.length; i++) {
            int u = rawEdges[i][0];
            int v = rawEdges[i][1];
            long w = rawEdges[i][2];
            adj.get(u).add(new EdgeEntry(v, w, i));
            adj.get(v).add(new EdgeEntry(u, w, i));
        }

        boolean[] visited = new boolean[n];
        long totalCost = 0;
        List<Integer> edgeIds = new ArrayList<>();
        int components = 0;

        PriorityQueue<EdgeEntry> pq = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                visited[i] = true;
                pq.addAll(adj.get(i));

                while (!pq.isEmpty()) {
                    EdgeEntry edge = pq.poll();
                    if (visited[edge.to]) continue;
                    visited[edge.to] = true;
                    totalCost += edge.weight;
                    edgeIds.add(edge.id);
                    pq.addAll(adj.get(edge.to));
                }
            }
        }

        result.put("cost", totalCost);
        result.put("edge_ids", edgeIds);
        result.put("components", components);
        return result;
    }
}