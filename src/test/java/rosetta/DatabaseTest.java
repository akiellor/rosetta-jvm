package rosetta;

import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.Mapper;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import static org.javafunk.matchbox.Matchers.hasOnlyItemsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DatabaseTest {
    @Test
    public void play() {
        GraphDatabaseService database = new GraphDatabaseFactory().newEmbeddedDatabase("./.db");

        Transaction transaction = database.beginTx();
        Node world = database.createNode();

        Node ruby = database.createNode();
        ruby.setProperty("name", "ruby");

        Node python = database.createNode();
        python.setProperty("name", "python");

        Node rails = database.createNode();
        rails.setProperty("name", "rails");
        rails.createRelationshipTo(ruby, Relationships.IMPLEMENTED_IN);
        world.createRelationshipTo(rails, Relationships.OPEN_SOURCE);

        Node django = database.createNode();
        django.setProperty("name", "django");
        django.createRelationshipTo(python, Relationships.IMPLEMENTED_IN);
        world.createRelationshipTo(django, Relationships.OPEN_SOURCE);

        transaction.success();

        TraversalDescription td = Traversal.description()
                .evaluator(Evaluators.all())
                .evaluator(Evaluators.includeWhereLastRelationshipTypeIs(Relationships.IMPLEMENTED_IN))
                .depthFirst();

        Traverser traverse = td.traverse(world);

        Iterable<String> languages = Lazily.map(traverse, new Mapper<Path, String>() {
            @Override public String map(Path input) {
                return String.valueOf(input.endNode().getProperty("name"));
            }
        });

        assertThat(languages, hasOnlyItemsInAnyOrder("ruby", "python"));
    }
}
