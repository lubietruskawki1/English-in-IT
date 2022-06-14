package learning_sets;

/**
 * A class for a user-made learning set.
 */
public class Set {
    final private String name;
    final private int terms_number;

    public Set(String name, int terms_number) {
        this.name = name;
        this.terms_number = terms_number;
    }

    public int getTerms_number() { return terms_number; }
    public String getName() { return name; }
}
