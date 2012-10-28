package rosetta.persist;

import org.javafunk.funk.monads.Option;
import rosetta.domain.Language;

public interface ProjectVisitor {
    void visit(Option<Long> id);

    void visit(String name);

    void visit(Language language);
}
