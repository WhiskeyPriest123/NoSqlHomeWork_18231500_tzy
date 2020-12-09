package com.bjtu.Louvain.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图定义
 * 1.没使用邻接矩阵原因是内存消耗量较大，更适用于稀疏图
 * 2.逆邻接表可以快速查询入边
 *
 * @author Aldebran
 * @since 11/09/2020
 */
public class Graph {

    /**
     * 逆邻接表
     */
    private InverseAdjacencyList inverseAdjacencyList;

    /**
     * 邻接表
     */
    private AdjacencyList adjacencyList;

    /**
     * 节点id
     */
    private HashSet<Integer> nodeIds;

    /**
     * 构造方法
     */
    public Graph() {
        init();
    }

    /**
     * 构造指定边
     *
     * @param links 边集合
     */
    public Graph(Collection<Link> links) {
        init(links);
    }

    private void init() {
        nodeIds = new HashSet<>();
        adjacencyList = new AdjacencyList();
        inverseAdjacencyList = new InverseAdjacencyList();
    }

    private void extendAdjacencyList(int index) {
        int currentIndex = adjacencyList.size() - 1;
        while (currentIndex++ < index) {
            adjacencyList.add(null);
        }
    }

    private void extendInverseAdjacencyList(int index) {
        int currentIndex = inverseAdjacencyList.size() - 1;
        while (currentIndex++ < index) {
            inverseAdjacencyList.add(null);
        }
    }

    private void init(Collection<Link> links) {
        init();
        addLinks(links);
    }

    /**
     * 添加边
     *
     * @param links 边集合
     */
    public void addLinks(Collection<Link> links) {
        AtomicInteger max = new AtomicInteger(-1);
        links.forEach(link -> {
            int src = link.src;
            int dst = link.dst;
            nodeIds.add(src);
            nodeIds.add(dst);
            int cMax = Integer.max(src, dst);
            if (cMax > max.get()) {
                max.set(cMax);
            }
            extendAdjacencyList(src);
            extendAdjacencyList(dst);
            extendInverseAdjacencyList(dst);
            extendInverseAdjacencyList(src);
            Link newLink = link.clone();
            adjacencyList.addLink(newLink);
            inverseAdjacencyList.addLink(newLink);
        });
        if (max.get() > -1) {
            for (int i = 0; i <= max.get(); i++) {
                nodeIds.add(i);
            }
        }
    }

    /**
     * 添加孤立点
     *
     * @param ids 点集合
     */
    public void addAcNodes(Collection<Integer> ids) {
        nodeIds.addAll(ids);
        AtomicInteger max = new AtomicInteger(0);
        ids.forEach(id -> {
            if (id > max.get()) {
                max.set(id);
            }
        });
        extendAdjacencyList(max.get());
        extendInverseAdjacencyList(max.get());
    }

    /**
     * 获得所有点（隔离保护）
     *
     * @return 点集合
     */
    public Set<Integer> getNodes() {
        return (HashSet<Integer>) nodeIds.clone();
    }

    /**
     * 获得一个点的所有出边
     *
     * @param id 点id
     * @return 边集合
     */
    public Set<Link> getOutLinks(int id) {
        List<Link> qLinks = adjacencyList.get(id);
        Set<Link> links = new HashSet<>();
        if (qLinks != null) {
            links.addAll(qLinks);
        }
        return links;
    }

    /**
     * 获得一个点的所有入边
     *
     * @param id 点id
     * @return 边集合
     */
    public Set<Link> getInLinks(int id) {
        List<Link> qLinks = inverseAdjacencyList.get(id);
        Set<Link> links = new HashSet<>();
        if (qLinks != null) {
            links.addAll(qLinks);
        }
        return links;
    }

    /**
     * 获得一个点的所有边
     *
     * @param id 点id
     * @return 边集合
     */
    public Set<Link> getBothWayLinks(int id) {
        Set<Link> links = getInLinks(id);
        links.addAll(getOutLinks(id));
        return links;
    }

    public Set<Integer> getOutNodes(int id) {
        Set<Integer> outNodes = new HashSet<>();
        getOutLinks(id).stream().map(link -> link.dst).forEach(outNodes::add);
        return outNodes;
    }

    public Set<Integer> getInNodes(int id) {
        Set<Integer> inNodes = new HashSet<>();
        getInLinks(id).stream().map(link -> link.src).forEach(inNodes::add);
        return inNodes;
    }

    public Set<Integer> getBothNodes(int id) {
        Set<Integer> bothNodes = getInNodes(id);
        bothNodes.addAll(getOutNodes(id));
        return bothNodes;
    }

    /**
     * 求出度
     *
     * @param id 点id
     * @return 求出度
     */
    public double getOutWeight(int id) {
        double sum = 0.0;
        for (Link link : getOutLinks(id)) {
            sum += link.weight;
        }
        return sum;
    }

    /**
     * 求入度
     *
     * @param id 点id
     * @return 求入度
     */
    public double getInWeight(int id) {
        double sum = 0.0;
        for (Link link : getInLinks(id)) {
            sum += link.weight;
        }
        return sum;
    }

    public double getBothWeight(int id) {
        return getInWeight(id) + getOutWeight(id);
    }

    public double totalWeight() {
        double w = 0.0;
        for (List<Link> links : adjacencyList) {
            if (links != null) {
                for (Link link : links) {
                    w += link.weight;
                }
            }
        }
        return w;
    }


    public int nodesNum() {
        return nodeIds.size();
    }

    /**
     * 求点之间的边
     *
     * @param id1 id1
     * @param id2 id2
     * @return 边集合
     */
    public Set<Link> getLinksBetweenTwoNodes(int id1, int id2) {
        Set<Link> links = new HashSet<>();
        getOutLinks(id1).forEach(link -> {
            if (link.dst == id2) {
                links.add(link);
            }
        });
        getInLinks(id1).forEach(link -> {
            if (link.src == id2) {
                links.add(link);
            }
        });
        return links;
    }

    /**
     * 求点之间的有向边
     *
     * @param id1 id1
     * @param id2 id2
     * @return 边集合
     */
    public Link getLinkFromOneToAnother(int id1, int id2) {
        Link resultLink = null;
        for (Link link : getOutLinks(id1)) {
            if (link.dst == id2) {
                resultLink = link;
                break;
            }
        }
        return resultLink;
    }

    /**
     * 为了调试而编写，不应该暴露
     *
     * @return
     */
    public List<Link> getAdjacencyList() {
        return (List<Link>) adjacencyList.clone();
    }

}
