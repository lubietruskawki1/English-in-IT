package com.technetapps.english_in_it;

public class TermAndDef {
    final private String term;
    final private String def;

    public TermAndDef(String term, String def) {
        this.term = term;
        this.def = def;
    }

    public String getDef() { return def; }
    public String getTerm() { return term; }
}
