import java.util.*;

public class DAG {
    static class EdgeInfo {
        int to, id;
        long weight;
        public EdgeInfo(int to, long weight, int id) {
            this.to = to;
            this.weight = weight;
            this.id = id;
        }
    }

    private static boolean topoDfs(int u, List<List<EdgeInfo>> adj, int[] state, Deque<Integer> order) {
        state[u] = 1;
        for (EdgeInfo edge : adj.get(u)) {
            int v = edge.to;
            if (state[v] == 1) return false;
            if (state[v] == 0) {
                if (!topoDfs(v, adj, state, order)) return false;
            }
        }
        state[u] = 2;
        order.push(u);
        return true;
    }

    public static Map<String, Object> solve(int n, int source, int[][] rawEdges) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (n == 0) {
            result.put("acyclic", true);
            result.put("distance", new ArrayList<>());
            result.put("parent_edge", new ArrayList<>());
            return result;
        }

        List<List<EdgeInfo>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int i = 0; i < rawEdges.length; i++) {
            adj.get(rawEdges[i][0]).add(new EdgeInfo(rawEdges[i][1], rawEdges[i][2], i));
        }

        int[] state = new int[n];
        Deque<Integer> topoOrder = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (state[i] == 0) {
                if (!topoDfs(i, adj, state, topoOrder)) {
                    result.put("acyclic", false);
                    result.put("distance", new ArrayList<>());
                    result.put("parent_edge", new ArrayList<>());
                    return result;
                }
            }
        }

        Long[] distance = new Long[n];
        long[] parentEdge = new long[n];
        Arrays.fill(parentEdge, -1);

        distance[source] = 0L;

        List<Integer> sorted = new ArrayList<>(topoOrder);

        for (int u : sorted) {
            if (distance[u] != null) {
                for (EdgeInfo edge : adj.get(u)) {
                    long newDist = distance[u] + edge.weight;
                    if (distance[edge.to] == null || newDist < distance[edge.to]) {
                        distance[edge.to] = newDist;
                        parentEdge[edge.to] = edge.id;
                    }
                }
            }
        }

        List<Long> distList = Arrays.asList(distance);
        List<Long> parentList = new ArrayList<>();
        for (long p : parentEdge) parentList.add(p);

        result.put("acyclic", true);
        result.put("distance", distList);
        result.put("parent_edge", parentList);
        return result;
    }

    // Маршрутный хелпер (Route Helper)
    public static List<Integer> reconstructRoute(int source, int target, int[][] edges, long[] parentEdges) {
        List<Integer> path = new ArrayList<>();
        if (source == target) {
            path.add(source);
            return path;
        }
        int curr = target;
        while (curr != source) {
            long pEdge = parentEdges[curr];
            if (pEdge == -1) return new ArrayList<>(); // Пути нет
            path.add(curr);
            curr = edges[(int) pEdge][0];
        }
        path.add(source);
        Collections.reverse(path);
        return path;
    }
}