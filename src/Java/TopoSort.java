import java.util.*;

public class TopoSort {
    private static boolean dfs(int u, List<List<Integer>> adj, int[] state, Deque<Integer> order) {
        state[u] = 1; // visiting
        for (int v : adj.get(u)) {
            if (state[v] == 1) return false; // cycle
            if (state[v] == 0) {
                if (!dfs(v, adj, state, order)) return false;
            }
        }
        state[u] = 2; // visited
        order.push(u);
        return true;
    }

    public static Map<String, Object> solve(int n, int[][] edges) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (n == 0) {
            result.put("acyclic", true);
            result.put("order", new ArrayList<>());
            return result;
        }

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
        }

        int[] state = new int[n]; // 0: unvisited, 1: visiting, 2: visited
        Deque<Integer> order = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (state[i] == 0) {
                if (!dfs(i, adj, state, order)) {
                    result.put("acyclic", false);
                    result.put("order", new ArrayList<>());
                    return result;
                }
            }
        }

        List<Integer> orderList = new ArrayList<>(order);
        result.put("acyclic", true);
        result.put("order", orderList);
        return result;
    }
}