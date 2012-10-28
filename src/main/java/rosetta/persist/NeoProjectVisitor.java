package rosetta.persist;

import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.monads.Option;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import rosetta.Relationships;
import rosetta.domain.Language;

public class NeoProjectVisitor implements ProjectVisitor {
    private final GraphDatabaseService database;
    private final Node root;
    private Node project;

    public NeoProjectVisitor(GraphDatabaseService database){
        this.database = database;
        this.root = database.getNodeById(0);
    }

    @Override public void visit(Option<Long> id) {
        project = id.map(new Mapper<Long, Node>() {
            @Override public Node map(Long input) {
                return database.getNodeById(input);
            }
        }).getOrElse(database.createNode());
        root.createRelationshipTo(project, Relationships.PROJECT);
    }

    @Override public void visit(String name) {
        project.setProperty("name", name);
    }

    @Override public void visit(Language language) {
        NeoLanguageVisitor visitor = new NeoLanguageVisitor(database);
        language.accept(visitor);
        project.createRelationshipTo(visitor.getNode(), Relationships.IMPLEMENTED_IN);
    }
}
