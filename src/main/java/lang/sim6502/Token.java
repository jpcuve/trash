package lang.sim6502;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 12, 2004
 * Time: 8:30:58 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Token {
    IGNORED,
    WHITESPACE,
    COMMENT,
    EOL,
    EOF,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    COMMA,
    COLON,
    LOGOP,
    RELOP,
    ADDOP,
    MULOP,
    BINOP,
    MONOP,
    SYMBOL,
    KEYWORD,
    INTEGER
}
