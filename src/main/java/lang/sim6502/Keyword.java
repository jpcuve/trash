package lang.sim6502;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 2, 2005
 * Time: 8:10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Keyword {
    // operators
    AND("&&"), OR("||"), XOR("^^"),
    EQ("=="), NE("!="), LT("<"), LE("<="), GE(">="), GT(">"),
    ADD("+"), SUB("-"), MUL("*"), DIV("/"), ASS("="),
    LNOT("!"), NOT("~"), IMM("#"),
    // keywords
    LO("lo"), HI("hi");

    private final String text;

    Keyword(String text){
        this.text = text;
    }

    public String toString() {
        return text;
    }

    public static Keyword parse(String text){
        String s = text.toLowerCase();
        if (s.length() > 0){
            switch(s.charAt(0)){
                case '&': return "&&".equals(s) ? AND : null;
                case '|': return "||".equals(s) ? OR : null;
                case '^': return "^^".equals(s) ? XOR : null;
                case '=': return "==".equals(s) ? EQ : null;
                case '!': return s.length() > 1 ? ("!=".equals(s) ? NE : null) : ("!".equals(s) ? LNOT : null);
                case '<': return s.length() > 1 ? ("<=".equals(s) ? LE : null) : ("<".equals(s) ? LT : null);
                case '>': return s.length() > 1 ? (">=".equals(s) ? GE : null) : ("<".equals(s) ? GT : null);
                case '~': return s.length() == 1 ? NOT : null;
                case 'l': return "lo".equals(s) ? LO : null;
                case 'h': return "hi".equals(s) ? HI : null;
            }
        }
        return null;
    }
}
