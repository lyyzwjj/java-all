package com.wjjzst.ads.second_stage.learn._03_graph;

import com.wjjzst.ads.first_stage.learn.common.printer.BinaryTrees;
import com.wjjzst.ads.first_stage.learn.heap.BinaryHeap;
import com.wjjzst.ads.second_stage.learn._02_union.GenericUnionFind;

import java.util.*;

/**
 * @Author: Wjj
 * @Date: 2020/7/30 10:49 下午
 * @desc:
 */
public class ListGraph<V, E> extends Graph<V, E> {
    private final Map<V, Vertex<V, E>> vertices = new HashMap<>();
    private final Set<Edge<V, E>> edges = new HashSet<>();
    private final Comparator<Edge<V, E>> edgeComparator = (e1, e2) -> weightManager.compare(e1.weight, e2.weight);


    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int vertices() {
        return vertices.size();
    }

    @Override
    public void addVertex(V v) {
        if (!vertices.containsKey(v)) {
            vertices.put(v, new Vertex<>(v));
        }
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.weight = weight;
        // 删了重新来
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);

    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex != null) {
            Iterator<Edge<V, E>> it = vertex.outEdges.iterator();
            while (it.hasNext()) {
                Edge<V, E> edge = it.next();
                // A点 out的edge(from) B点 in的edge(to)
                edge.to.inEdges.remove(edge);
                it.remove();
                edges.remove(edge);
            }
            it = vertex.inEdges.iterator();
            while (it.hasNext()) {
                Edge<V, E> edge = it.next();
                edge.from.outEdges.remove(edge);
                it.remove();
                edges.remove(edge);
            }
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) return;
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) return;
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }

    @Override
    public void bfs(V begin, VertexVisitor<V> visitor) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visitedVertices.add(beginVertex);
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            // System.out.println(vertex.value);
            if (visitor.visit(vertex.value)) return;
            // 由demo图中可知 如果是出来一个才算遍历过  那么第一次队列queue中压入 V0,V2 此时visitedVertices中只有V1
            // 第二次V2 poll出来之时  此时visitedVertices中只有V1和V2 此时会把V0再次压人到queue中
            // visitedVertices.add(vertex);
            // 故一定要在offer进队列的时候就当成已经遍历过了
            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visitedVertices.contains(edge.to)) {
                    queue.offer(edge.to);
                    visitedVertices.add(edge.to);
                }
            }
        }

    }

    /*@Override
    public void bfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visitedVertices.add(beginVertex);
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            System.out.println(vertex.value);
            // 由demo图中可知 如果是出来一个才算遍历过  那么第一次队列queue中压入 V0,V2 此时visitedVertices中只有V1
            // 第二次V2 poll出来之时  此时visitedVertices中只有V1和V2 此时会把V0再次压人到queue中
            // visitedVertices.add(vertex);
            // 故一定要在offer进队列的时候就当成已经遍历过了
            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visitedVertices.contains(edge.to)) {
                    queue.offer(edge.to);
                    visitedVertices.add(edge.to);
                }
            }
        }

    }*/

    @Override
    public void dfs(V begin, VertexVisitor<V> visitor) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();
        stack.push(beginVertex);
        visitedVertices.add(beginVertex);
        // System.out.println(beginVertex.value);
        if (visitor.visit(beginVertex.value)) return;
        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visitedVertices.contains(edge.to)) {
                    stack.push(vertex);
                    stack.push(edge.to);
                    visitedVertices.add(edge.to);
                    // System.out.println(edge.to.value);
                    if (visitor.visit(edge.to.value)) return;
                }
            }
        }
    }

    @Override
    public List<V> topologicalSort() {
        // 装着返回值value的list
        List<V> list = new ArrayList<>();
        // 入度为0的vertex容器
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        // 入度不为0的连带着入度存入一个map中
        Map<Vertex<V, E>, Integer> ins = new HashMap<>();
        // 初始化  将入度为0的点都放入队列
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex);
            } else {
                ins.put(vertex, in);
            }
        });
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            list.add(vertex.value);
            for (Edge<V, E> edge : vertex.outEdges) {
                int toIn = ins.get(edge.to) - 1;
                if (toIn == 0) {
                    queue.offer(edge.to);
                } else {
                    ins.put(edge.to, toIn);
                }
            }
        }
        return list;
    }

    @Override
    public Set<EdgeInfo<V, E>> minimumSpanningTree() {
        // Set<EdgeInfo<V, E>> edgeInfos = prim();
        Set<EdgeInfo<V, E>> edgeInfos = kruskal();
        return edgeInfos;
    }

    public Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        if (!it.hasNext()) return null;
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        Set<Vertex<V, E>> addedVertices = new HashSet<>();
        Vertex<V, E> vertex = it.next();
        addedVertices.add(vertex);
        BinaryHeap<Edge<V, E>> heap = new BinaryHeap<>(vertex.outEdges, edgeComparator);
        int edgeSize = vertices.size() - 1;
        while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {
            Edge<V, E> edge = heap.remove();
            if (!addedVertices.contains(edge.to)) {
                edgeInfos.add(edge.info());
                addedVertices.add(edge.to);
                heap.addAll(edge.to.outEdges);
                BinaryTrees.print(heap);
                System.out.println("\n\n");
            }
        }
        return edgeInfos;
    }

    public Set<EdgeInfo<V, E>> kruskal() {
        int edgeSize;  // 最小生成树的边的数量等于顶点数量减一
        if ((edgeSize = vertices.size() - 1) < 0) {
            return null;
        }
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        BinaryHeap<Edge<V, E>> heap = new BinaryHeap<>(edges, edgeComparator);
        GenericUnionFind<Vertex<V, E>> uf = new GenericUnionFind<>();
        vertices.values().forEach(uf::makeSet);
        while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {
            Edge<V, E> edge = heap.remove();
            if (uf.isSame(edge.from, edge.to)) {
                continue;
            }
            edgeInfos.add(edge.info());
            uf.union(edge.from, edge.to);
        }
        return edgeInfos;
    }

    @Override
    public Map<V, PathInfo<V, E>> shortestPath(V begin) {
        // return dijkstra(begin);
        return bellmanFord(begin);
    }

    public Map<V, PathInfo<V, E>> dijkstra(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return null;
        }
        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
        paths.put(beginVertex, new PathInfo<>(weightManager.zero()));
//        for (Edge<V, E> edge : beginVertex.outEdges) {
//            PathInfo<V, E> path = new PathInfo<>();
//            path.weight = edge.weight;
//            path.edgeInfos.add(edge.info());
//            paths.put(edge.to, path);
//        }
        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath(paths);
            Vertex<V, E> minVertex = minEntry.getKey();
            PathInfo<V, E> minPathInfo = minEntry.getValue();
            selectedPaths.put(minVertex.value, minPathInfo);
            paths.remove(minVertex);
            for (Edge<V, E> edge : minVertex.outEdges) {
                if (selectedPaths.containsKey(edge.to.value)) {
                    continue;
                }
                dijkstraRelax(edge, minPathInfo, paths);
            }
        }
        // 删除首个节点
        selectedPaths.remove(begin);
        return selectedPaths;
    }

    private void dijkstraRelax(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        PathInfo<V, E> oldPath = paths.get(edge.to);
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        if (oldPath != null) {
            // newWeight = weightManager.add(fromPath.weight, edge.weight);
            if (weightManager.compare(newWeight, oldPath.weight) >= 0) {
                return;
            }
            oldPath.edgeInfos.clear();
        } else {
            // newWeight = edge.weight;
            oldPath = new PathInfo<>();
            paths.put(edge.to, oldPath);
        }
        oldPath.weight = newWeight;
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());
    }

    public Map<V, PathInfo<V, E>> bellmanFord(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return null;
        }
        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        selectedPaths.put(begin, new PathInfo<>(weightManager.zero()));
        int count = vertices.size() - 1;
        // 对所有的边进行一次松弛 (n-1)次松弛
        for (int i = 0; i < count; i++) {
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> pathInfo = selectedPaths.get(edge.from.value);
                if (pathInfo == null) {
                    continue;
                }
                bellmanFordRelax(edge, pathInfo, selectedPaths);
            }
        }
        // n-1次对所有边松弛后再松弛一遍
        for (Edge<V, E> edge : edges) {
            PathInfo<V, E> pathInfo = selectedPaths.get(edge.from.value);
            if (pathInfo == null) {
                continue;
            }
            if (bellmanFordRelax(edge, pathInfo, selectedPaths)) {
                System.out.println("有负权环,找不到最短路径");
                return null;
            }
        }
        // 删除首个节点
        selectedPaths.remove(begin);
        return selectedPaths;
    }

    private boolean bellmanFordRelax(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<V, PathInfo<V, E>> paths) {
        PathInfo<V, E> oldPath = paths.get(edge.to.value);
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        if (oldPath != null) {
            // newWeight = weightManager.add(fromPath.weight, edge.weight);
            if (weightManager.compare(newWeight, oldPath.weight) >= 0) {
                return false;
            }
            oldPath.edgeInfos.clear();
        } else {
            // newWeight = edge.weight;
            oldPath = new PathInfo<>();
            paths.put(edge.to.value, oldPath);
        }
        oldPath.weight = newWeight;
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());
        return true;
    }

    private Map.Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Iterator<Map.Entry<Vertex<V, E>, PathInfo<V, E>>> it = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = it.next();
        while (it.hasNext()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> entry = it.next();
            if (weightManager.compare(entry.getValue().weight, minEntry.getValue().weight) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }


    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
        Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();
        for (Edge<V, E> edge : edges) {
            Map<V, PathInfo<V, E>> map = paths.computeIfAbsent(edge.from.value, k -> new HashMap<>());
            PathInfo<V, E> pathInfo = new PathInfo<>(edge.weight);
            pathInfo.edgeInfos.add(edge.info());
            map.put(edge.to.value, pathInfo);
        }
        vertices.forEach((V v2, Vertex<V, E> vertex2) ->
                vertices.forEach((V v1, Vertex<V, E> vertex1) ->
                        vertices.forEach((V v3, Vertex<V, E> vertex3) -> {
                            if (v1.equals(v2) || v2.equals(v3) || v1.equals(v3)) {
                                return;
                            }
                            PathInfo<V, E> path12 = getPathInfo(v1, v2, paths);
                            if (path12 == null) {
                                return;
                            }
                            PathInfo<V, E> path23 = getPathInfo(v2, v3, paths);
                            if (path23 == null) {
                                return;
                            }
                            PathInfo<V, E> path13 = getPathInfo(v1, v3, paths);
                            E newPathWeight = weightManager.add(path12.weight, path23.weight);
                            if (path13 != null) {
                                if (weightManager.compare(newPathWeight, path13.weight) >= 0) {
                                    return;
                                }
                                path13.edgeInfos.clear();
                            } else {
                                path13 = new PathInfo<>();
                                // 应为 path12 有了  所以 paths.get(v1) 一定存在
                                paths.get(v1).put(v3, path13);
                                // Map<V, PathInfo<V, E>> map = new HashMap<>();
                                // map.put(v3, path13);
                                // paths.put(v1, map);
                            }
                            path13.weight = newPathWeight;
                            path13.edgeInfos.addAll(path12.edgeInfos);
                            path13.edgeInfos.addAll(path23.edgeInfos);
                        })));
        return paths;
    }

    private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> paths) {
        Map<V, PathInfo<V, E>> map = paths.get(from);
        return map == null ? null : map.get(to);
    }

    /*public Set<EdgeInfo<V, E>> myKruskal() {
        if (edges.size() == 0) {
            return null;
        }
        BinaryHeap<Edge<V, E>> heap = new BinaryHeap<>(edges, edgeComparator);
        GenericUnionFind<Vertex<V, E>> uf = new GenericUnionFind<>();
        vertices.values().forEach(uf::makeSet);
        Set<EdgeInfo<V, E>> visitedEdges = new HashSet<>();
        while (!heap.isEmpty() && visitedEdges.size() < vertices.size()) {
            Edge<V, E> remove = heap.remove();
            Vertex<V, E> from = remove.from;
            Vertex<V, E> to = remove.to;
            if (uf.isSame(from, to)) {
                continue;
            }
            // uf.makeSet(to); //此处不是讲to加进并查集  之前的并查集已经有了这个元素了
            uf.union(from, to); // 现在只需要将两个点union到同一个集里面
            visitedEdges.add(remove.info());
        }
        return visitedEdges;
    }*/
    /*@Override
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();
        stack.push(beginVertex);
        visitedVertices.add(beginVertex);
        System.out.println(beginVertex.value);
        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visitedVertices.contains(edge.to)) {
                    stack.push(vertex);
                    stack.push(edge.to);
                    visitedVertices.add(edge.to);
                    System.out.println(edge.to.value);
                }
            }
        }
    }*/
    /*@Override
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        dfs(beginVertex, new HashSet<>());
    }*/

    /*private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
        System.out.println(vertex.value);
        visitedVertices.add(vertex);
        for (Edge<V, E> edge : vertex.outEdges) {
            if (!visitedVertices.contains(edge.to)) {
                dfs(edge.to, visitedVertices);
            }
        }
    }*/


    public void print() {
        System.out.println("vertices==================================================");
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            System.out.println("in---------------------");
            System.out.println(vertex.inEdges);
            System.out.println("out---------------------");
            System.out.println(vertex.outEdges);
        });
        System.out.println("\n" + "edges==================================================");
        edges.forEach(System.out::println);
    }

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Vertex<V, E> vertex = (Vertex<V, E>) obj;

            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }

    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge<V, E> edge = (Edge<V, E>) o;

            if (!Objects.equals(from, edge.from)) return false;
            return Objects.equals(to, edge.to);
        }


        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Edge {from=" + from + ", to=" + to + ", weight=" + weight + '}';
        }

        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }
    }
}
