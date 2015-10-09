package lang.assembler;

import java.util.regex.Pattern;

/**
 * Created by jpc on 9/7/15.
 */
public enum TokenInfo {
    WHITESPACE("\\s+"),
    EQUAL("="),
    COMMA(","),
    SHARP("#"),
    PARENTHESIS_OPEN("\\("),
    PARENTHESIS_CLOSE("\\)"),
    PLUS_MINUS("[+-]"),
    MULTIPLY_DIVIDE("[*/]"),
    COMMENT(";.*"),
    CHAR("\".\""),
    STRING("\".+\""),
    NUMBER_HEXADECIMAL_INTEGER("\\$[0-9A-F]+"),
    NUMBER_DECIMAL_INTEGER("[0-9]+"),
    FUNCTION("sin|cos|exp|ln|sqrt"),
    PSEUDO("\\.[a-zA-Z][a-zA-Z0-9_]*"),
    SYMBOL("[a-zA-Z][a-zA-Z0-9_]*")
    ;

    private Pattern pattern;

    TokenInfo(String regex) {
        this.pattern = Pattern.compile("^" + regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
