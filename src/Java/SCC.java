import java.util.*;

public class SCC {
    private static void dfs1(int u, List<List<Integer>> adj, boolean[] vis, Deque<Integer> stack) {
        vis[u] = true;
        for (int v : adj.get(u)) {
            if (!vis[v]) dfs1(v, adj, vis, stack);
        }
        stack.push(u);
    }

    private static void dfs2(int u, List<List<Integer>> revAdj, boolean[] vis, int label, int[] component) {
        vis[u] = true;
        component[u] = label;
        for (int v : revAdj.get(u)) {
            if (!vis[v]) dfs2(v, revAdj, vis, label, component);
        }
    }

    public static Map<String, Object> solve(int n, int[][] edges) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (n == 0) {
            result.put("component", new ArrayList<>());
            result.put("condensation", new ArrayList<>());
            return result;
        }

        List<List<Integer>> adj = new ArrayList<>();
        List<List<Integer>> revAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            revAdj.add(new ArrayList<>());
        }

        for (int[] e : edges) {
            int u = e[0], v = e[1];
            adj.get(u).add(v);
            revAdj.get(v).add(u);
        }

        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (!vis[i]) dfs1(i, adj, vis, stack);
        }

        Arrays.fill(vis, false);
        int[] component = new int[n];
        int label = 0;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!vis[u]) {
                dfs2(u, revAdj, vis, label, component);
                label++;
            }
        }

        Set<String> condSet = new HashSet<>();
        for (int[] e : edges) {
            int uLabel = component[e[0]];
            int vLabel = component[e[1]];
            if (uLabel != vLabel) {
                condSet.add(uLabel + "," + vLabel);
            }
        }

        List<int[]> condensation = new ArrayList<>();
        for (String s : condSet) {
            String[] parts = s.split(",");
            condensation.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        List<Integer> compList = new ArrayList<>();
        for (int c : component) compList.add(c);

        result.put("component", compList);
        result.put("condensation", condensation);
        return result;
    }
}