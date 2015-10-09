package lang.sim6502;

import lang.sim6502.construct.Block;
import lang.sim6502.construct.Call;
import lang.sim6502.construct.Factor;
import lang.sim6502.construct.Line;
import lang.sim6502.terminal.Numeral;
import lang.sim6502.terminal.Symbol;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static lang.sim6502.Token.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 4:23:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parser {
    protected Tokenizer ct;
    protected Token lookAhead;

    public Parser(String expression){
        this.ct = new Tokenizer(new StringReader(expression));
    }

    public Parser(Reader reader) {
        this.ct = new Tokenizer(reader);
    }

    public Expression parse() throws IOException, ParseException {
        this.lookAhead = ct.nextToken();
        System.out.println("token=" + lookAhead);
        return block();
    }

    protected void accept(Token t) throws IOException, ParseException {
        if (lookAhead != t) throw new ParseException("syntax error: '" + t + "' expected, found: " + ct.sval, 0);
        lookAhead = ct.nextToken();
        System.out.println("token=" + lookAhead);
    }

    private void accept(Token t, Keyword k) throws IOException, ParseException {
        if (!(lookAhead == t && ct.ival == k)) throw new ParseException("syntax error: '" + k + "' expected, found: " + ct.sval, 0);
        lookAhead = ct.nextToken();
    }


    protected Expression block() throws IOException, ParseException {
        List<Expression> lines = new ArrayList<Expression>();
        while (lookAhead != EOF){
            Expression line = line();
            lines.add(line);
            System.out.println("line: " + line);
        }
        accept(EOF);
        return new Block(lines);
    }

    protected Expression line() throws IOException, ParseException {
        Line line = new Line();
        if (lookAhead == SYMBOL){
            line.setLabel(new Symbol(ct.sval));
            accept(SYMBOL);
        }
        if (lookAhead == WHITESPACE) accept(WHITESPACE);
        if (lookAhead == SYMBOL){
            line.setOpcode(new Symbol(ct.sval));
            accept(SYMBOL);
        }
        if (lookAhead == WHITESPACE) accept(WHITESPACE);
        if (lookAhead != EOL && lookAhead != EOF && lookAhead != COMMENT){
            line.setArgument(expression());
            if (lookAhead == WHITESPACE) accept(WHITESPACE);
        }
        if (lookAhead == COMMENT){
            line.setComment(ct.sval);
            accept(COMMENT);
        }
        if (lookAhead == EOL) accept(EOL);
        return line;
    }

    /*
    protected Expression line() throws IOException, ParseException {
        Line line = new Line();
        if (lookAhead == WHITESPACE) accept(WHITESPACE);
        if (lookAhead == SYMBOL){
            line.setOpcode(new Symbol(ct.sval));
            accept(SYMBOL);
            if (lookAhead == COLON){
                line.setSticker(line.getOpcode());
                accept(COLON);
                if (lookAhead == WHITESPACE) accept(WHITESPACE);
                line.setOpcode(new Symbol(ct.sval));
                accept(SYMBOL);
            }
        }
        if (lookAhead == WHITESPACE) accept(WHITESPACE);
        if (lookAhead != EOL && lookAhead != EOF && lookAhead != COMMENT){
            line.setArgument(expression());
            if (lookAhead == WHITESPACE) accept(WHITESPACE);
        }
        if (lookAhead == COMMENT){
            line.setComment(ct.sval);
            accept(COMMENT);
        }
        if (lookAhead == EOL) accept(EOL);
        return line;
    }
    */

    protected Expression expression() throws IOException, ParseException {
        Expression o = addterm();
        while (lookAhead == COMMA){
            String op = ct.sval;
            accept(COMMA);
            o = new Call(op, o, relterm());
        }
        return o;
    }

    protected Expression relterm() throws IOException, ParseException {
        Expression o = addterm();
        while (lookAhead == RELOP){
            String op = ct.sval;
            accept(RELOP);
            o = new Call(op, o, addterm());
        }
        return o;
    }

    protected Expression addterm() throws IOException, ParseException {
        Expression o = multerm();
        while (lookAhead == ADDOP){
            String op = ct.sval;
            accept(ADDOP);
            o = new Call(op, o, multerm());
        }
        return o;
    }

    protected Expression multerm() throws IOException, ParseException {
        Expression o = binterm();
        while(lookAhead == MULOP){
            String op = ct.sval;
            accept(MULOP);
            o = new Call(op, o, binterm());
        }
        return o;
    }

    protected Expression binterm() throws IOException, ParseException {
        Expression o = monterm();
        while(lookAhead == BINOP){
            String op = ct.sval;
            accept(BINOP);
            o = new Call(op, o, monterm());
        }
        return o;
    }

    protected Expression monterm() throws IOException, ParseException {
        if (lookAhead == MONOP){
            String op = ct.sval;
            accept(MONOP);
            return new Call(op, factor());
        }
        return factor();
    }

    protected Expression factor() throws IOException, ParseException {
        String literal = ct.sval;
        switch(lookAhead){
            case SYMBOL:
                accept(SYMBOL);
                return new Symbol(literal);
            case INTEGER:
                accept(INTEGER);
                return new Numeral(literal);
            case LEFT_PARENTHESIS:
                accept(LEFT_PARENTHESIS);
                Expression expression = expression();
                accept(RIGHT_PARENTHESIS);
                return new Factor(expression);
            default:
                throw new ParseException("unexpected token: " + literal, 0);
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Parser parser = new Parser(new FileReader("test.s"));
        System.out.println(parser.parse());
    }

}
