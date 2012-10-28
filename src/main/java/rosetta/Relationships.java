package rosetta;

import org.neo4j.graphdb.RelationshipType;

public enum Relationships implements RelationshipType {
    OPEN_SOURCE, IMPLEMENTED_IN
}
