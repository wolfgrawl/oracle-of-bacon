package com.serli.oracle.of.bacon.repository;

import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Neo4JRepository {
    private final Driver driver;

    public Neo4JRepository() {
        this.driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
    }

    //Adrien Cadoret et Paul Defois m'ont expliqué le concept
    public List<?> getConnectionsToKevinBacon(String actorName) {
        ArrayList<Object> results = new ArrayList<>();
        String request = "MATCH p=shortestPath((bacon:Actor {name:\"Bacon, Kevin (I)\"})-[*]-(actor:Actor {name:\""+actorName+"\"}))  RETURN DISTINCT p";

        try (Session session = driver.session())
        {
            StatementResult myStatementResult = session.run(request);

            List<Record> myRecords = statementResult.list();

            if(!myRecords.isEmpty()) {

                Path path = ((PathValue) myRecords.get(0).values().get(0)).asPath();

                Iterable<Node> myNodes = path.nodes();
                for(Node n: myNodes)
                    results.add(n);

                Iterable<Relationship> myRelationships = path.relationships();
                for(Relationship r: myRelationships)
                    results.add(r);

            }
        }
        catch (Exception e){
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    public static abstract class GraphItem {
        public final long id;

        private GraphItem(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GraphItem graphItem = (GraphItem) o;

            return id == graphItem.id;
        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }
    }

    private static class GraphNode extends GraphItem {
        public final String type;
        public final String value;

        public GraphNode(long id, String value, String type) {
            super(id);
            this.value = value;
            this.type = type;
        }
    }

    private static class GraphEdge extends GraphItem {
        public final long source;
        public final long target;
        public final String value;

        public GraphEdge(long id, long source, long target, String value) {
            super(id);
            this.source = source;
            this.target = target;
            this.value = value;
        }
    }
}
