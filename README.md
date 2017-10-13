# infosys_example_procedure
Demo POC

This project requires Neo4j 3.2.x



Stored Procedure
------------

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    mvn clean package

This will produce a jar-file, `target/procedures-0.1-SNAPSHOT.jar`,
that can be copied to the `plugin` directory of your Neo4j instance.

    cp target/procedures-0.1-SNAPSHOT.jar neo4j-enterprise-3.2.6/plugins/.


Edit your Neo4j/conf/neo4j.conf file by adding this line:

    dbms.security.procedures.unrestricted=com.infosys.*

Call it as follows:

CALL com.infosys.dirty_dependencies(["L3_86"]) 
YIELD value
RETURN value
