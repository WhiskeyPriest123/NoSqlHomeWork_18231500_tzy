package com.bjtu.Louvain.algo;

import java.util.Arrays;

/**
 * 社区信息
 *
 * @author Aldebran
 * @since 11/09/2020
 */
public class CommunityInfo {

    /**
     * 节点编号
     */
    public int communitiesNo;

    /**
     * i代表节点编号，value代表社区编号
     */
    public int[] nodeCommunityNo;

    /**
     * i代表社区编号，j代表节点编号
     */
    public int[][] communityNodeIds;

    @Override
    public String toString() {
        return "CommunityInfo{" +
                "communitiesNo=" + communitiesNo +
                ", nodeCommunityNo=" + Arrays.toString(nodeCommunityNo) +
                ", communityNodeIds=" + Arrays.deepToString(communityNodeIds) +
                '}';
    }

}
