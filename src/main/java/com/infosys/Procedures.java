package com.infosys;

import com.infosys.results.ListResult;
import com.infosys.schema.Labels;
import com.infosys.schema.RelationshipTypes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory; 
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import org.neo4j.graphdb.*;
import org.neo4j.kernel.api.ReadOperations;
import org.neo4j.kernel.api.exceptions.EntityNotFoundException;
import org.neo4j.kernel.impl.api.store.RelationshipIterator;
import org.neo4j.kernel.impl.core.ThreadToStatementContextBridge;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.io.FileWriter;
import java.io.StringWriter; 
import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Map.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Procedures {

    @Context
    public GraphDatabaseAPI db;

	@Context
	public GraphDatabaseAPI dbAPI;
	
    @Context
    public Log log;



    @Procedure(name = "com.infosys.dirty_dependencies", mode = Mode.READ)
    @Description("CALL com.infosys.dirty_dependencies([starting], file, updatedDate)")
    public Stream<StringResult> DirtyDependencies(@Name("starting") List<String> starting) throws InterruptedException {
        ArrayList<ArrayList> paths = new ArrayList<>();
        for (String start : starting) {
            ArrayList<String> path = new ArrayList<>();

            final Node user = db.findNode(Labels.Node, "id", start);
        }


		JsonFactory jsonfactory = new JsonFactory();
		Writer writer = new StringWriter();
		String json = null;
		try (Transaction tx = db.beginTx()) {
		Result result = db.execute("match (l:L3 {id:'L3_86'}) return l.id;");	
		
		try {
		        JsonGenerator jsonGenerator = jsonfactory.createJsonGenerator(writer);
		        jsonGenerator.writeStartObject();
		        jsonGenerator.writeArrayFieldStart("logs");
		        jsonGenerator.writeString("authenticationToken must be at least 16 characters.");
		        jsonGenerator.writeString("Downloading push notification package failed.");
		        jsonGenerator.writeString("Extracting push notification package failed.");
		        jsonGenerator.writeString("Missing file in push notification package.");
		        jsonGenerator.writeString("Missing image in push notification package.");
				while (result.hasNext()) {
            		Map<String,Object> row = result.next();
            	 	for ( Entry<String,Object> column : row.entrySet() ){
            		 	jsonGenerator.writeString(column.getKey() + ":" + column.getValue());
            	 	}
            	}
		        jsonGenerator.writeEndArray();
		        jsonGenerator.writeEndObject();
		        jsonGenerator.close();
		        json = writer.toString();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		tx.success();
		}
		
			return Stream.of(new StringResult(json));
			
//        return paths.stream().map(ListResult::new);
    }


}
