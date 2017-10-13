package com.infosys;

import com.infosys.results.StringResult;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Import {
    @Context
    public GraphDatabaseAPI db;

    @Context
    public Log log;

    @Procedure(name = "com.infosys.import.csv", mode = Mode.WRITE)
    @Description("CALL com.infosys.import.csv(file)")
    public Stream<StringResult> importCSV(@Name("file") String file) throws InterruptedException {
        long start = System.nanoTime();

        Thread t1 = new Thread(new ImportRunnable(file, db, log));
        t1.start();
        t1.join();

        return Stream.of(new StringResult(file + " imported in " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds"));
    }
}
