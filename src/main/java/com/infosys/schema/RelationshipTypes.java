package com.infosys.schema;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    CHILD_OF,
	NEEDS
}
