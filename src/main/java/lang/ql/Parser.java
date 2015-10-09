package lang.ql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static lang.ql.Keyword.*;
import static lang.ql.Token.*;


/**
 * @author jpc
 * 
 */
public class Parser {
    private final Tokenizer tokenizer;
    private Iterator<Token> i;

    public Parser(String expression){
        this.tokenizer = new Tokenizer(expression);
        this.i = tokenizer.iterator();
    }

    public Expression parse() throws QlException {
        i.next();
        return expression();
    }

    private void accept(Token token) throws QlException {
        if (tokenizer.getToken() != token) throw new QlException("syntax error: '" + token + "' expected, found: " + tokenizer.getValue());
        i.next();
    }

    protected Expression expression() throws QlException {
        return orterm();
    }

    protected Expression orterm() throws QlException {
        final List<Expression> args = new ArrayList<Expression>();
        args.add(andterm());
        while (tokenizer.getToken() == OROP){
            accept(OROP);
            args.add(andterm());
        }
        return args.size() == 1 ? args.get(0) : new Call(OR, args);
    }

    protected Expression andterm() throws QlException {
        final List<Expression> args = new ArrayList<Expression>();
        args.add(notterm());
        while (tokenizer.getToken() == ANDOP){
            accept(ANDOP);
            args.add(notterm());
        }
        return args.size() == 1 ? args.get(0) : new Call(AND, args);
    }

    protected Expression notterm() throws QlException {
        if (tokenizer.getToken() == NOTOP){
            accept(NOTOP);
            return new Call(NOT, relterm());
        }
        return relterm();
    }

    protected Expression relterm() throws QlException {
        Expression o = addterm();
        while (tokenizer.getToken() == RELOP){
            final Keyword k = tokenizer.getKeyword();
            accept(RELOP);
            switch(k){
                case EQ:
                case NE:
                case LT:
                case LE:
                case GE:
                case GT:
                case LIKE:
                case NLIKE:
                case IN:
                case NIN:
                case BETWEEN:
                case NBETWEEN:{
                    o = new Call(k, o, addterm());
                    break;
                }
                default:{
                    o = new Call(k, o);
                    break;
                }
            }
        }
        return o;
    }

    protected Expression addterm() throws QlException {
        Expression o = multerm();
        while (tokenizer.getToken() == ADDOP){
            final Keyword k = tokenizer.getKeyword();
            accept(ADDOP);
            o = new Call(k, o, multerm());
        }
        return o;
    }

    protected Expression multerm() throws QlException {
        Expression o = factor();
        while(tokenizer.getToken() == MULOP){
            final Keyword k = tokenizer.getKeyword();
            accept(MULOP);
            o = new Call(k, o, factor());
        }
        return o;
    }


    protected Expression factor() throws QlException {
        if (tokenizer.getToken() == SYMBOL){
            return identifier();
        } else{
            return composite();
        }
    }

    protected Expression identifier() throws QlException {
        final String s = tokenizer.getValue();
        accept(SYMBOL);
        switch(tokenizer.getToken()){
            case LEFT_PARENTHESIS:
                final List<Expression> args = new ArrayList<Expression>();
                accept(LEFT_PARENTHESIS);
                while (tokenizer.getToken() != RIGHT_PARENTHESIS){
                    args.add(addterm());
                    if (tokenizer.getToken() != RIGHT_PARENTHESIS) accept(COMMA);
                }
                accept(RIGHT_PARENTHESIS);
                return new Call(s, args);
            case COLON:
                accept(COLON);
                final Expression id = literal();
                final List<String> fields = new ArrayList<String>();
                while (tokenizer.getToken() == DOT){
                    accept(DOT);
                    fields.add(tokenizer.getValue());
                    accept(SYMBOL);
                }
                return new Reference(s, id, fields);
        }
        throw new QlException("unidentified literal: " + s);
    }

    protected Expression composite() throws QlException {
        switch(tokenizer.getToken()){
            case LEFT_BRACKET:
                accept(LEFT_BRACKET);
                final Expression from = literal();
                accept(COLON);
                final Expression to = literal();
                accept(RIGHT_BRACKET);
                return new Composite(from, to);
            case LEFT_BRACE:
                final List<Expression> elements = new ArrayList<Expression>();
                accept(LEFT_BRACE);
                while (tokenizer.getToken() != RIGHT_BRACE){
                    elements.add(literal());
                    if (tokenizer.getToken() != RIGHT_BRACE) accept(COMMA);
                }
                accept(RIGHT_BRACE);
                return new Composite(elements);
        }
        return literal();
    }

    protected Expression literal() throws QlException {
        final String s = tokenizer.getValue();
        switch(tokenizer.getToken()){
            case BOOLEAN:
                accept(BOOLEAN);
                return new Literal(s.indexOf('t') != -1);
            case NUMBER:
                accept(NUMBER);
                return new Literal(s.indexOf('.') != -1 ? (Number) Double.valueOf(s) : (Number) Long.valueOf(s));
            case STRING:
                accept(STRING);
                return new Literal(s.substring(1, s.length() - 1));
            case ADDOP:
                final Keyword k = tokenizer.getKeyword();
                accept(ADDOP);
                return new Call(k, factor());
            case LEFT_PARENTHESIS:
                accept(LEFT_PARENTHESIS);
                String descriptor = null;
                if (tokenizer.getToken() == DESCRIPTOR){
                    descriptor = tokenizer.getValue();
                    accept(DESCRIPTOR);
                }
                final Expression o = expression();
                accept(RIGHT_PARENTHESIS);
                return new Factor(descriptor, o);
        }
        throw new QlException("unidentified literal: " + s);
    }

    protected List<Expression> argument() throws QlException {
        List<Expression> c = new ArrayList<Expression>();
        accept(LEFT_PARENTHESIS);
        while (tokenizer.getToken() != RIGHT_PARENTHESIS){
            c.add(expression());
            if (tokenizer.getToken() != RIGHT_PARENTHESIS) accept(COMMA);
        }
        accept(RIGHT_PARENTHESIS);
        return c;
    }

}
