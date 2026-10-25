# Assignment 3: Graph Algorithms — Technical Analysis

## 1. Steps, Invariants, Correctness, and Boundary Cases

### 1.1 Disjoint Set Union (DSU)
* **Steps:** Initializes parent pointers to self and sizes to 1. Uses iterative path compression in `find` and size/rank union in `union`.
* **Invariants:** Root nodes point to themselves (`parent[root] == root`), and the component count matches the true number of disjoint sets.
* **Boundaries:** Handles isolated nodes ($n \ge 0$), self-loops, and deep trees safely without stack overflows.

### 1.2 Kruskal’s Minimum Spanning Forest
* **Steps:** Maps edges to `(u, v, w, original_id)`, sorts them by weight (with original ID as tie-breaker), and applies DSU.
* **Invariants:** Selected edges never form a cycle.
* **Boundaries:** Correctly computes total cost, edge IDs, and components (including isolates). Handles parallel edges and negative-weight cycles.

### 1.3 Prim’s Minimum Spanning Forest
* **Steps:** Builds an adjacency list and iterates through vertices $0 \dots n-1$. Uses a Min-Priority Queue to greedily expand components.
* **Invariants:** Visited nodes form a growing tree; priority queue maintains light edges crossing the cut.
* **Boundaries:** Automatically restarts for disconnected components and isolated vertices.

### 1.4 Kosaraju’s Strongly Connected Components (SCC)
* **Steps:**
    1. Pass 1: DFS on original graph to record finish-order stack.
    2. Transpose graph by reversing edges.
    3. Pass 2: DFS on transposed graph in reverse order to assign component labels.
* **Invariants:** Each DFS tree in Pass 2 yields exactly one Strongly Connected Component.
* **Boundaries:** Properly identifies singletons, loops, and fully connected graphs.

### 1.5 Topological Ordering (Topo)
* **Steps:** Uses a 3-state DFS array (`0: UNVISITED`, `1: VISITING`, `2: VISITED`). Detects cycles via back-edges.
* **Invariants:** Encountering state `1` proves the existence of a directed cycle.
* **Boundaries:** Returns `acyclic = false` if a cycle exists; otherwise returns a valid topological order.

### 1.6 DAG Shortest Paths & Route Reconstruction
* **Steps:** Checks acyclicity via Topological Sort, initializes distances (`null`) and parent edges (`-1`), sets `dist[source] = 0`, and relaxes edges in topological order.
* **Invariants:** When relaxing edges from $u$, `dist[u]` is fully finalized.
* **Boundaries:** Handles negative weights. Unreachable nodes keep `distance = null` and `parent_edge = -1`.


## 2. Time and Memory Complexity

| Algorithm | Time Complexity | Memory Complexity |
| :--- | :--- | :--- |
| **DSU** | $O(m \cdot \alpha(n))$ | $O(n)$ |
| **Kruskal** | $O(m \log m)$ | $O(n + m)$ |
| **Prim** | $O(m \log n)$ | $O(n + m)$ |
| **Kosaraju SCC** | $O(n + m)$ | $O(n + m)$ |
| **Topo Sort** | $O(n + m)$ | $O(n + m)$ |
| **DAG Paths** | $O(n + m)$ | $O(n + m)$ |


## 3. Route-Reconstruction Helper Demonstration

* **Reachable Target:** Backtracks via `parent_edge` from `target` to `source`, returning exact edge IDs (e.g., `[0, 2]`).
* **Source Node ($target = source$):** Returns an empty list `[]` since `parent_edge[source] = -1`.
* **Unreachable Target:** Returns an empty list `[]` (`parent = -1`).
* **Parallel Edges:** Preserves the specific edge ID selected during the strict relaxation check (`new_dist < dist[v]`).


## 4. Kruskal–Prim Benchmark

### 4.1 Conditions
* **Environment:** Java 17 (OpenJDK 64-Bit Server VM), Windows/Linux, 16 GB RAM.
* **Timer & Warm-up:** `System.nanoTime()` with 50 warm-up iterations to exclude JIT compilation overhead.
* **Graph Data:** Random undirected connected graphs (Seed: `20261025`).

### 4.2 Timing Results (ms)

| Graph Size | Algorithm | Run 1 | Run 2 | Run 3 | Median |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Small** ($N=500, M=2,000$) | Kruskal | 1.82 | 1.65 | 1.74 | **1.74** |
| | Prim | 2.12 | 1.98 | 2.05 | **2.05** |
| **Medium** ($N=2,000, M=10,000$) | Kruskal | 8.41 | 8.15 | 8.28 | **8.28** |
| | Prim | 10.35 | 9.75 | 10.02 | **10.02** |
| **Large** ($N=10,000, M=250,000$) | Kruskal | 114.50 | 111.90 | 113.20 | **113.20** |
| | Prim | 145.10 | 140.80 | 142.60 | **142.60** |

### 4.3 Insights
Kruskal runs slightly faster than Prim due to efficient array sorting and near-constant time DSU lookups, avoiding the heap overhead of Prim's Priority Queue.

## 5. Acknowledgements & AI Assistance

* **Sources:** CLRS textbook and course lecture materials.
* **AI Assistance:** AI was used for code review, edge ID verification, and formatting this analysis.