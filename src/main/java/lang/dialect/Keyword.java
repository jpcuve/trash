package lang.dialect;

public enum Keyword {
    LITERAL(""),
    // operators
    AND("&&"), OR("||"), XOR("^^"),
    EQ("=="), NE("!="), LT("<"), LE("<="), GE(">="), GT(">"),
    ADD("+"), SUB("-"), MUL("*"), DIV("/"),
    LNOT("!"), NOT("~"),
    // keywords
    IF("if"), ELSE("else"), DO("do"), WHILE("while"), REPEAT("repeat"), UNTIL("until"), FOR("for"),
    SWITCH("switch"), CASE("case"), OTHERWISE("otherwise"), TRY("try"), CATCH("catch");

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
                case 'c':
                    if (s.length() > 2){
                        switch(s.charAt(2)){
                            case 's': return "case".equals(s) ? CASE : null;
                            case 't': return "catch".equals(s) ? CATCH : null;
                        }
                    }
                    return null;
                case 'd': return "do".equals(s) ? DO : null;
                case 'e': return "else".equals(s) ? ELSE : null;
                case 'f': return "for".equals(s) ? FOR : null;
                case 'i': return "if".equals(s) ? IF : null;
                case 'o': return "otherwise".equals(s) ? OTHERWISE : null;
                case 'r': return "repeat".equals(s) ? REPEAT : null;
                case 's': return "switch".equals(s) ? SWITCH : null;
                case 't': return "try".equals(s) ? TRY : null;
                case 'u': return "until".equals(s) ? UNTIL : null;
                case 'w': return "while".equals(s) ? WHILE : null;
            }
        }
        return null;
    }
}
