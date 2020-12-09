package com.bjtu.Louvain.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 逆邻接表
 *
 * @author Aldebran
 * @since 11/09/2020
 */
class InverseAdjacencyList extends ArrayList<List<Link>> {

    void addLink(Link link) {
        int dst = link.dst;
        List<Link> links = get(dst);
        if (links == null) {
            links = new ArrayList<>();
            set(dst, links);
        }
        links.add(link);
    }
}
