package rosetta.persist;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import rosetta.domain.Project;

public class Database {
    private final GraphDatabaseService database;

    public Database(GraphDatabaseService database){
        this.database = database;
    }

    public Project save(final Project project){
        return withTransaction(new Operation<Project>() {
            @Override public Project execute() {
                NeoProjectVisitor visitor = new NeoProjectVisitor(database);
                project.accept(visitor);
                return project;
            }
        });
    }

    private <T> T withTransaction(Operation<T> operation){
        Transaction transaction = database.beginTx();
        T result = operation.execute();
        transaction.success();
        return result;
    }
}
