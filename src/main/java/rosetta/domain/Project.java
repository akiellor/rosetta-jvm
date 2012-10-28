package rosetta.domain;

import org.javafunk.funk.monads.Option;
import rosetta.persist.ProjectVisitor;

public class Project {
    private Option<Long> id = Option.none();
    private final String name;
    private final Language language;

    public Project(String name, Language language) {
        this.name = name;
        this.language = language;
    }

    public void accept(ProjectVisitor visitor) {
        visitor.visit(id);
        visitor.visit(name);
        visitor.visit(language);
    }
}
