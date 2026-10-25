import java.util.*;

public class DSU {
    private int[] parent;
    private int[] size;
    private int components;
    private int maxComponentSize;

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        components = n;
        maxComponentSize = n > 0 ? 1 : 0;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        if (parent[i] == i)
            return i;
        return parent[i] = find(parent[i]);
    }

    public void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                int temp = rootI; rootI = rootJ; rootJ = temp;
            }
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
            maxComponentSize = Math.max(maxComponentSize, size[rootI]);
            components--;
        }
    }

    public static Map<String, Object> solve(int n, int[][] operations) {
        List<int[]> states = new ArrayList<>();
        if (n == 0) {
            Map<String, Object> res = new LinkedHashMap<>();
            res.put("states", states);
            return res;
        }

        DSU dsu = new DSU(n);
        for (int[] op : operations) {
            dsu.union(op[0], op[1]);
            states.add(new int[]{dsu.components, dsu.maxComponentSize});
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("states", states);
        return result;
    }
}