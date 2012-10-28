package rosetta.domain;

import org.javafunk.funk.monads.Option;
import rosetta.persist.LanguageVisitor;

public class Language {
    private Option<Long> id = Option.none();
    private final String name;

    public Language(String name) {
        this.name = name;
    }

    public void accept(LanguageVisitor visitor) {
        visitor.visit(id);
        visitor.visit(name);
    }
}
