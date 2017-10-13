package com.infosys;

import com.infosys.schema.Labels;
import com.infosys.schema.RelationshipTypes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ImportRunnable implements Runnable {
    private static final int TRANSACTION_LIMIT = 1000;
    private String file;
    private GraphDatabaseAPI db;
    private Log log;

    public ImportRunnable(String file, GraphDatabaseAPI db, Log log) {
        this.file = file;
        this.db = db;
        this.log = log;
    }

    @Override
    public void run() {
        Reader in;
        Iterable<CSVRecord> records = null;
        try {
            in = new FileReader("/" + file);
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("Import - File not found: " + file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Import - IO Exception: " + file);
        }

        Transaction tx = db.beginTx();
        try {
            int count = 0;
            Node inputNode;
            Node outputNode;

            assert records != null;
            for (CSVRecord record : records) {
                count++;

                String input = record.get("input");
                inputNode = db.findNode(Labels.Node, "id", input);
                if (inputNode == null) {
                    inputNode = db.createNode(Labels.Node);
                    inputNode.setProperty("id", input);
                }

                String output = record.get("output");
                outputNode = db.findNode(Labels.Node, "id", output);
                if (outputNode == null) {
                    outputNode = db.createNode(Labels.Node);
                    outputNode.setProperty("id", output);
                }

                inputNode.createRelationshipTo(outputNode, RelationshipTypes.CHILD_OF);

                if (count % TRANSACTION_LIMIT == 0) {
                    tx.success();
                    tx.close();
                    tx = db.beginTx();
                }
            }

            tx.success();
        } finally {
            tx.close();
        }

    }
}