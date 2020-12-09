package com.bjtu.Louvain.algo;

import com.bjtu.Louvain.entity.Graph;
import com.bjtu.Louvain.entity.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * louvain划分算法
 * 1.效率上还有优化的空间，（逆）邻接表的操作
 * 2.模块度优化
 *
 * @author Aldebran
 * @since 11/09/2020
 */
public class LouvainCalculator {

    /**
     * 图结构
     */
    private Graph graph;

    /**
     * 缓存总权重
     */
    private double totalW;

    /**
     * 节点社区编号
     */
    private int[] nodeCommunityNo;

    /**
     * 缓存节点度中心性（带权重版，无向）
     */
    private double[] nodeBothWeight;

    /**
     * 黏合系数 多层情况下，越小越容易黏合
     */
    private double stickingK = 1.0;

    /**
     * 构造器，需要图作为数据依据以及生成缓存
     *
     * @param graph 图
     */
    public LouvainCalculator(Graph graph) {
        System.out.println("graph info: " + graph.getAdjacencyList());
        System.out.println("graph nodes: " + graph.getNodes());
        // 初始化操作
        totalW = graph.totalWeight();
        nodeCommunityNo = new int[graph.nodesNum()];
        for (int i = 0; i < nodeCommunityNo.length; i++) {
            nodeCommunityNo[i] = i;
        }
        nodeBothWeight = new double[graph.nodesNum()];
        for (int i = 0; i < nodeBothWeight.length; i++) {
            nodeBothWeight[i] = graph.getBothWeight(i);
        }
        this.graph = graph;
    }

    /**
     * 黏合系数 多层情况下，越小越容易黏合
     *
     * @param stickingK 系数
     */
    public void setStickingK(double stickingK) {
        this.stickingK = stickingK;
    }

    /**
     * 判断两个点是否属于同一个社区
     *
     * @param id1 节点1 id
     * @param id2 节点2 id
     * @return 属于同一社区返回1，不属于则返回0
     */
    private int c(int id1, int id2) {
        return nodeCommunityNo[id1] == nodeCommunityNo[id2] ? 1 : 0;
    }

    /**
     * 计算当前模块度（全局模块度）
     *
     * @return ModuleQ值
     */
    private double getModuleQ() {
        System.out.println("comm status: " + Arrays.toString(nodeCommunityNo));
        System.out.println("total weight: " + totalW);
        double q = 0.0;
        Set<Integer> nodeIds = graph.getNodes();
        for (int id1 : nodeIds) {
            for (int id2 : nodeIds) {
                if (id1 == id2) {
                    continue;
                }
                double A = 0.0;
                for (Link link : graph.getLinksBetweenTwoNodes(id1, id2)) {
                    A += link.weight;
                }
                q += c(id1, id2) * (A - nodeBothWeight[id1] * nodeBothWeight[id2] / totalW * stickingK);
                System.out.printf("id1: %s,id2: %s, A: %s, c: %s, k1: %s, k2: %s%n", id1, id2, A, c(id1, id2),
                        nodeBothWeight[id1], nodeBothWeight[id2]);
            }
        }
        return q / totalW;
    }

    /**
     * 单层louvain社区划分算法
     *
     * @return 社区划分结果
     */
    public CommunityInfo findCommunitiesSingleLevel() {
        while (true) {
            int[] copy = nodeCommunityNo.clone();
            double Q = getModuleQ();
            if (Double.isNaN(Q)) {
                break;
            }
            System.out.println(String.format("initQ: %s", Q));
            for (int id1 : graph.getNodes()) {
                System.out.println("deal id: " + id1);
                int bestCommunityNo = -1;
                double deltaQ = 0.0;
                int id1OriginNo = nodeCommunityNo[id1];
                for (int id2 : graph.getBothNodes(id1)) {
                    if (c(id1, id2) == 1) {
                        continue;
                    }
                    nodeCommunityNo[id1] = nodeCommunityNo[id2];
                    double currentQ = getModuleQ();
                    if (Double.isNaN(currentQ)) {
                        continue;
                    }
                    System.out.println(String.format("currentQ: %s", currentQ));
                    if (currentQ - Q > 0.00001 && currentQ - Q > deltaQ) {
                        deltaQ = currentQ - Q;
                        bestCommunityNo = nodeCommunityNo[id2];
                    }
                }
                if (bestCommunityNo == -1) {
                    nodeCommunityNo[id1] = id1OriginNo;
                    System.out.println(String.format("no best communityNo. id: %s", id1));
                } else {
                    nodeCommunityNo[id1] = bestCommunityNo;
                    System.out.println(String.format("set best communityNo. id: %s, comm: %s", id1, bestCommunityNo));
                    Q = getModuleQ();
                }
            }
            if (Arrays.equals(nodeCommunityNo, copy)) {
                break;
            }
        }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nodeCommunityNo.length; i++) {
            int commNum = nodeCommunityNo[i];
            List<Integer> integers = map.get(commNum);
            if (integers == null) {
                integers = new ArrayList<>();
                map.put(commNum, integers);
            }
            integers.add(i);
        }
        CommunityInfo communityInfo = new CommunityInfo();
        communityInfo.communitiesNo = map.size();
        communityInfo.communityNodeIds = new int[map.size()][];
        AtomicInteger nextCommNum = new AtomicInteger(0);
        map.forEach((commNum, ids) -> {
            communityInfo.communityNodeIds[nextCommNum.get()] = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                communityInfo.communityNodeIds[nextCommNum.get()][i] = ids.get(i);
            }
            nextCommNum.incrementAndGet();
        });
        communityInfo.nodeCommunityNo = new int[nodeCommunityNo.length];
        for (int c = 0; c < communityInfo.communityNodeIds.length; c++) {
            for (int nodeId : communityInfo.communityNodeIds[c]) {
                communityInfo.nodeCommunityNo[nodeId] = c;
            }
        }
        return communityInfo;
    }

    /**
     * 多层louvain社区划分算法
     *
     * @param level 层数
     * @return 社区划分结果
     */
    public CommunityInfo findCommunitiesMultiLevel(int level) {
        CommunityInfo[] levelResult = new CommunityInfo[level + 1];
        Graph currentGraph = graph;
        while (level > 0) {
            System.out.println("current level: " + level);
            CommunityInfo communityInfo;
            if (currentGraph == this.graph) {
                communityInfo = findCommunitiesSingleLevel();
            } else {
                communityInfo = new LouvainCalculator(currentGraph).findCommunitiesSingleLevel();
            }
            levelResult[level] = communityInfo;
            System.out.println(String.format("levelResult: %s, level: %s", communityInfo, level));
            Graph newGraph = new Graph();
            for (int c1 = 0; c1 < communityInfo.communitiesNo; c1++) {
                boolean ac = true;
                for (int c2 = 0; c2 < communityInfo.communitiesNo; c2++) {
                    if (c1 == c2) {
                        continue;
                    }
                    int[] c1NodeIds = communityInfo.communityNodeIds[c1];
                    int[] c2NodeIds = communityInfo.communityNodeIds[c2];
                    List<Link> links = new ArrayList<>();
                    for (int c1OneNode : c1NodeIds) {
                        for (int c2OneNode : c2NodeIds) {
                            Link tempLink = currentGraph.getLinkFromOneToAnother(c1OneNode, c2OneNode);
                            if (tempLink != null) {
                                links.add(tempLink);
                            }
                        }
                    }
                    if (!links.isEmpty()) {
                        Link newLink = new Link(c1, c2, 0.0);
                        links.forEach(link -> newLink.weight += link.weight);
                        newGraph.addLinks(Arrays.asList(newLink));
                        ac = false;
                    }
                }
                if (ac) {
                    newGraph.addAcNodes(Arrays.asList(c1));
                }
            }
            currentGraph = newGraph;
            level--;
        }
        CommunityInfo finalComm = new CommunityInfo();
        Map<Integer, Integer> commNoFinalCommNoMap = new HashMap<>();
        int cCommNum = 0;
        for (int i = 0; i < levelResult[levelResult.length - 1].nodeCommunityNo.length; i++) {
            for (int j = levelResult.length - 2; j >= 1; j--) {
                int c = levelResult[levelResult.length - 1].nodeCommunityNo[i];
                int newC = levelResult[j].nodeCommunityNo[c];
                levelResult[levelResult.length - 1].nodeCommunityNo[i] = newC;
            }
            int c = levelResult[levelResult.length - 1].nodeCommunityNo[i];
            if (!commNoFinalCommNoMap.containsKey(c)) {
                commNoFinalCommNoMap.put(c, cCommNum++);
            }
            levelResult[levelResult.length - 1].nodeCommunityNo[i] = commNoFinalCommNoMap.get(c);
        }
        finalComm.nodeCommunityNo = levelResult[levelResult.length - 1].nodeCommunityNo;
        finalComm.communitiesNo = cCommNum;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < cCommNum; i++) {
            map.put(i, new ArrayList<>());
        }
        for (int id = 0; id < finalComm.nodeCommunityNo.length; id++) {
            map.get(finalComm.nodeCommunityNo[id]).add(id);
        }
        finalComm.communityNodeIds = new int[cCommNum][];
        map.forEach((cId, nodeIds) -> {
            int[] ids = new int[nodeIds.size()];
            for (int i = 0; i < nodeIds.size(); i++) {
                ids[i] = nodeIds.get(i);
            }
            finalComm.communityNodeIds[cId] = ids;
        });

        return finalComm;
    }

}
