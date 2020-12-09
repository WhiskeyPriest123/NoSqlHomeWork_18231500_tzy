//https://github.com/ghostorsoul/louvain.git java实现

//https://neo4j.com/docs/graph-data-science/current/algorithms/louvain/  算法地址
public class LouvainCyher {
//    CREATE
//            (nAlice:User {name: 'Alice', seed: 42}),
//            (nBridget:User {name: 'Bridget', seed: 42}),
//            (nCharles:User {name: 'Charles', seed: 42}),
//            (nDoug:User {name: 'Doug'}),
//            (nMark:User {name: 'Mark'}),
//            (nMichael:User {name: 'Michael'}),
//
//            (nAlice)-[:LINK {weight: 1}]->(nBridget),
//            (nAlice)-[:LINK {weight: 1}]->(nCharles),
//            (nCharles)-[:LINK {weight: 1}]->(nBridget),
//
//            (nAlice)-[:LINK {weight: 5}]->(nDoug),
//
//            (nMark)-[:LINK {weight: 1}]->(nDoug),
//            (nMark)-[:LINK {weight: 1}]->(nMichael),
//            (nMichael)-[:LINK {weight: 1}]->(nMark);



//    CALL gds.graph.create(
//            'myGraph',
//            'User',
//    {
//        LINK: {
//            orientation: 'UNDIRECTED'
//        }
//    },
//    {
//        nodeProperties: 'seed',
//                relationshipProperties: 'weight'
//    }
//)









//
//
//    CREATE (a:Node {name: 'a'})
//    CREATE (b:Node {name: 'b'})
//    CREATE (c:Node {name: 'c'})
//    CREATE (d:Node {name: 'd'})
//    CREATE (e:Node {name: 'e'})
//    CREATE (f:Node {name: 'f'})
//    CREATE (g:Node {name: 'g'})
//    CREATE (h:Node {name: 'h'})
//    CREATE (i:Node {name: 'i'})
//    CREATE (j:Node {name: 'j'})
//    CREATE (k:Node {name: 'k'})
//    CREATE (l:Node {name: 'l'})
//    CREATE (m:Node {name: 'm'})
//    CREATE (n:Node {name: 'n'})
//    CREATE (x:Node {name: 'x'})
//
//    CREATE (a)-[:TYPE]->(b)
//    CREATE (a)-[:TYPE]->(d)
//    CREATE (a)-[:TYPE]->(f)
//    CREATE (b)-[:TYPE]->(d)
//    CREATE (b)-[:TYPE]->(x)
//    CREATE (b)-[:TYPE]->(g)
//    CREATE (b)-[:TYPE]->(e)
//    CREATE (c)-[:TYPE]->(x)
//    CREATE (c)-[:TYPE]->(f)
//    CREATE (d)-[:TYPE]->(k)
//    CREATE (e)-[:TYPE]->(x)
//    CREATE (e)-[:TYPE]->(f)
//    CREATE (e)-[:TYPE]->(h)
//    CREATE (f)-[:TYPE]->(g)
//    CREATE (g)-[:TYPE]->(h)
//    CREATE (h)-[:TYPE]->(i)
//    CREATE (h)-[:TYPE]->(j)
//    CREATE (i)-[:TYPE]->(k)
//    CREATE (j)-[:TYPE]->(k)
//    CREATE (j)-[:TYPE]->(m)
//    CREATE (j)-[:TYPE]->(n)
//    CREATE (k)-[:TYPE]->(m)
//    CREATE (k)-[:TYPE]->(l)
//    CREATE (l)-[:TYPE]->(n)
//    CREATE (m)-[:TYPE]->(n);



//    CALL gds.louvain.stream({
//        nodeProjection: 'Node',
//                relationshipProjection: {
//            TYPE: {
//                type: 'TYPE',
//                        orientation: 'undirected',
//                        aggregation: 'NONE'
//            }
//        },
//        includeIntermediateCommunities: true
//    }) YIELD nodeId, communityId, intermediateCommunityIds
//    RETURN gds.util.asNode(nodeId).name AS name, communityId, intermediateCommunityIds
//    ORDER BY name ASC
}
