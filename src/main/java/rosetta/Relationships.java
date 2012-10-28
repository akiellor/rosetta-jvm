package rosetta;

import org.neo4j.graphdb.RelationshipType;

public enum Relationships implements RelationshipType {
    PROJECT, LANGUAGE, IMPLEMENTED_IN
}
