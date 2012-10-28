package rosetta.persist;

import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.monads.Option;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import rosetta.Relationships;

public class NeoLanguageVisitor implements LanguageVisitor{
    private final GraphDatabaseService database;
    private final Node root;
    private Node node;

    public NeoLanguageVisitor(GraphDatabaseService database) {
        this.database = database;
        this.root = database.getNodeById(0);
    }

    @Override public void visit(Option<Long> id) {
        node = id.map(new Mapper<Long, Node>() {
            @Override public Node map(Long input) {
                return database.getNodeById(input);
            }
        }).getOrElse(database.createNode());
        root.createRelationshipTo(node, Relationships.LANGUAGE);
    }

    @Override public void visit(String name) {
        node.setProperty("name", name);
    }

    public Node getNode() {
        return node;
    }
}
