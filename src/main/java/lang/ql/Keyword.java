package lang.ql;

/**
 * @author jpc
 */
public enum Keyword {
    LITERAL(""),
    // operators
    OR("|"),
    AND("&"),
    NOT("!"),
    LIKE("~"), NLIKE("!~"), EQ("="), NE("<>"), LT("<"), LE("<="), GE(">="), GT(">"), BETWEEN("@"), NBETWEEN("!@"), IN("$"), NIN("!$"), NULL("#"), NNULL("!#"),
    ADD("+"), SUB("-"),
    MUL("*"), DIV("/"),
    ;

    private final String text;

    Keyword(String text){
        this.text = text;
    }

    public String toString() {
        return text;
    }

    public static Keyword parse(String text){
        for (final Keyword keyword: Keyword.values()) if (keyword.text.equals(text)) return keyword;
        return null;
    }
}
