package rosetta.persist;

import org.javafunk.funk.monads.Option;

public interface LanguageVisitor {
    void visit(Option<Long> id);

    void visit(String name);
}
