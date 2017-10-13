package com.infosys;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class DistinctDependenciesTest {

    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withProcedure(Procedures.class);

    @Test
    public void shouldGetDistinctDependencies() {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY);
        ArrayList row = getResultRow(response);

        assertEquals(ANSWER_SET, row);
    }

    private static final HashMap<String, Object> QUERY = new HashMap<String, Object>(){{
        put("statements", new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("statement", "CALL com.infosys.distinct_dependencies(['c1','c5'], 'myfile.csv') YIELD value RETURN value");
            }});
        }});
    }};
    private static final String MODEL_STATEMENT =
            // 1->2->3->4
            // 1->5->3
            // 1->5->6->7->8->9->10
            "CREATE (c1:Node {id:'c1'})" +
                    "CREATE (c2:Node {id:'c2'})" +
                    "CREATE (c3:Node {id:'c3'})" +
                    "CREATE (c4:Node {id:'c4'})" +
                    "CREATE (c5:Node {id:'c5'})" +
                    "CREATE (c6:Node {id:'c6'})" +
                    "CREATE (c7:Node {id:'c7'})" +
                    "CREATE (c8:Node {id:'c8'})" +
                    "CREATE (c9:Node {id:'c9'})" +
                    "CREATE (c10:Node {id:'c10'})" +
                    "CREATE (c1)-[:CHILD_OF]->(c2)" +
                    "CREATE (c2)-[:CHILD_OF]->(c3)" +
                    "CREATE (c3)-[:CHILD_OF]->(c4)" +
                    "CREATE (c1)-[:CHILD_OF]->(c5)" +
                    "CREATE (c5)-[:CHILD_OF]->(c3)" +
                    "CREATE (c5)-[:CHILD_OF]->(c6)" +
                    "CREATE (c6)-[:CHILD_OF]->(c7)" +
                    "CREATE (c7)-[:CHILD_OF]->(c8)" +
                    "CREATE (c8)-[:CHILD_OF]->(c9)" +
                    "CREATE (c9)-[:CHILD_OF]->(c10)" ;



    private static final ArrayList<ArrayList<String>>  ANSWER_SET = new ArrayList<ArrayList<String>> (){{
        add(new ArrayList<String>() {{ add("c1"); add("c5"); add("c6"); add("c7"); add("c8");  add("c9"); add("c10");}});
        add(new ArrayList<String>() {{ add("c1"); add("c5"); add("c3"); add("c4"); }});
        add(new ArrayList<String>() {{ add("c1"); add("c2"); add("c3"); add("c4"); }});
        add(new ArrayList<String>() {{ add("c5"); add("c6"); add("c7"); add("c8");  add("c9"); add("c10");}});
        add(new ArrayList<String>() {{ add("c5"); add("c3"); add("c4"); }});
    }};


    static ArrayList<ArrayList<String>>  getResultRow(HTTP.Response response) {
        Map actual = response.content();
        ArrayList results = (ArrayList)actual.get("results");
        HashMap result = (HashMap)results.get(0);
        ArrayList<Map> data = (ArrayList)result.get("data");
        ArrayList<ArrayList<String>> values = new ArrayList<>();

        data.forEach((value) -> values.add((ArrayList)((ArrayList) value.get("row")).get(0)));
        return values;
    }
}
