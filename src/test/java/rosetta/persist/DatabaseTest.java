package rosetta.persist;

import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import rosetta.Relationships;
import rosetta.domain.Language;
import rosetta.domain.Project;

import static org.javafunk.matchbox.Matchers.hasOnlyItemsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DatabaseTest {
    private Database database;
    private GraphDatabaseService neo;

    @Before
    public void setUp() {
        neo = new GraphDatabaseFactory().newEmbeddedDatabase("./.db");
        database = new Database(neo);
    }

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
        world.createRelationshipTo(rails, Relationships.PROJECT);

        Node django = database.createNode();
        django.setProperty("name", "django");
        django.createRelationshipTo(python, Relationships.IMPLEMENTED_IN);
        world.createRelationshipTo(django, Relationships.PROJECT);

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

    @Test
    public void shouldPersistDomainObjects() {
        database.save(new Project("rails", new Language("ruby")));
        database.save(new Project("django", new Language("python")));

        TraversalDescription td = Traversal.description()
                .evaluator(Evaluators.all())
                .evaluator(Evaluators.includeWhereLastRelationshipTypeIs(Relationships.IMPLEMENTED_IN))
                .depthFirst();

        Traverser traverse = td.traverse(neo.getNodeById(0));

        Iterable<String> languages = Lazily.map(traverse, new Mapper<Path, String>() {
            @Override public String map(Path input) {
                return String.valueOf(input.endNode().getProperty("name"));
            }
        });

        assertThat(languages, hasOnlyItemsInAnyOrder("ruby", "python"));
    }
}
