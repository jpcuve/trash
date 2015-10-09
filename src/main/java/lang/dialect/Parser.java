package lang.dialect;

import lang.dialect.construct.*;
import lang.dialect.terminal.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static lang.dialect.Keyword.*;
import static lang.dialect.Token.*;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 12:27:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parser {
    private final Object tag;
    private final Tokenizer tokenizer;
    private Iterator<Token> i;

    public Parser(Object tag, String expression){
        this.tag = tag;
        this.tokenizer = new Tokenizer(new StringReader(expression));
        this.i = tokenizer.iterator();
    }

    public Parser(Object tag, Reader reader) {
        this.tag = tag;
        this.tokenizer = new Tokenizer(reader);
        this.i = tokenizer.iterator();
    }

    public Expression parse() throws DialectException {
        i.next();
        return statement();
    }

    private void accept(Token token) throws DialectException {
        if (tokenizer.getToken() != token) throw new DialectException("syntax error: '" + token + "' expected, found: " + tokenizer.getValue());
        i.next();
    }

    private void accept(Token token, Keyword k) throws DialectException {
        if (!(tokenizer.getToken() == token && tokenizer.getKeyword() == k)) throw new DialectException("syntax error: '" + k + "' expected, found: " + tokenizer.getValue());
        i.next();
    }

    protected Expression statement() throws DialectException {
        while (tokenizer.getToken() == SEMICOLON) accept(SEMICOLON);
        switch(tokenizer.getToken()){
            case LEFT_BRACE:
                List<Expression> c = new ArrayList<Expression>();
                accept(LEFT_BRACE);
                while (tokenizer.getToken() != RIGHT_BRACE) c.add(statement());
                accept(RIGHT_BRACE);
                return new Sequence(tag, c);
            case KEYWORD:
                switch(tokenizer.getKeyword()){
                    case DO:
                        accept(KEYWORD, DO);
                        accept(LEFT_PARENTHESIS);
                        Parameter doInit = assignment();
                        accept(RIGHT_PARENTHESIS);
                        return new Do(tag, doInit, statement());
                    case FOR:
                        accept(KEYWORD, FOR);
                        accept(LEFT_PARENTHESIS);
                        Parameter forInit = assignment();
                        accept(SEMICOLON);
                        Expression forTest = tokenizer.getToken() == SEMICOLON ? Logical.TRUE: expression();
                        accept(SEMICOLON);
                        Parameter forNext = assignment();
                        accept(RIGHT_PARENTHESIS);
                        return new For(tag, forInit, forTest, forNext, statement());
                    case IF:
                        accept(KEYWORD, IF);
                        accept(LEFT_PARENTHESIS);
                        Expression ifTest = expression();
                        accept(RIGHT_PARENTHESIS);
                        Expression ifTrue = statement();
                        Expression ifFalse= Literal.UNDEFINED;
                        if (tokenizer.getToken() == KEYWORD && tokenizer.getKeyword() == ELSE){
                            accept(KEYWORD, ELSE);
                            ifFalse = statement();
                        }
                        return new IfThenElse(tag, ifTest, ifTrue, ifFalse);
                    case REPEAT:
                        accept(KEYWORD, REPEAT);
                        Expression repeatBody = statement();
                        accept(KEYWORD, UNTIL);
                        accept(LEFT_PARENTHESIS);
                        Expression repeatTest = expression();
                        accept(RIGHT_PARENTHESIS);
                        accept(SEMICOLON);
                        return new Repeat(tag, repeatBody, repeatTest);
                    case SWITCH:
                        accept(KEYWORD, SWITCH);
                        accept(LEFT_PARENTHESIS);
                        Expression switchKey = expression();
                        accept(RIGHT_PARENTHESIS);
                        SwitchCaseOtherwise s = new SwitchCaseOtherwise(tag, switchKey);
                        accept(LEFT_BRACE);
                        while (tokenizer.getToken() == KEYWORD && tokenizer.getKeyword() == CASE){
                            accept(KEYWORD, CASE);
                            List<Expression> switchValues = new ArrayList<Expression>();
                            switchValues.add(literal());
                            while(tokenizer.getToken() != COLON){
                                accept(COMMA);
                                switchValues.add(literal());
                            }
                            accept(COLON);
                            s.addCase(switchValues, statement());
                        }
                        if (tokenizer.getToken() == KEYWORD && tokenizer.getKeyword() == OTHERWISE){
                            accept(KEYWORD, OTHERWISE);
                            accept(COLON);
                            s.addCase(statement());
                        }
                        accept(RIGHT_BRACE);
                        return s;
                    case TRY:
                        accept(KEYWORD, TRY);
                        Expression tryBody = statement();
                        accept(KEYWORD, CATCH);
                        accept(LEFT_PARENTHESIS);
                        Symbol fault = new Symbol(tokenizer.getValue());
                        accept(SYMBOL);
                        accept(RIGHT_PARENTHESIS);
                        return new TryCatch(tag, tryBody, fault, statement());
                    case WHILE:
                        accept(KEYWORD, WHILE);
                        accept(LEFT_PARENTHESIS);
                        Expression whileTest = expression();
                        accept(RIGHT_PARENTHESIS);
                        return new While(tag, whileTest, statement());

                }
            default:
                Expression e = expression();
                accept(SEMICOLON);
                return e;
        }
    }

    protected Parameter assignment() throws DialectException {
        List<Symbol> symbols = new ArrayList<Symbol>();
        List<Expression> values = new ArrayList<Expression>();
        while (tokenizer.getToken() == SYMBOL){
            symbols.add(new Symbol(tokenizer.getValue()));
            accept(SYMBOL);
            if (tokenizer.getToken() == ASSIGN){
                accept(ASSIGN);
                values.add(expression());
            } else{
                values.add(Literal.UNDEFINED);
            }
            if (tokenizer.getToken() == COMMA) accept(COMMA);
        }
        return new Parameter(tag, symbols, values);
    }


    protected Expression expression() throws DialectException {
        Expression o = relterm();
        while (tokenizer.getToken() == LOGOP){
            String op = tokenizer.getValue();
            accept(LOGOP);
            o = new Call(tag, op, o, relterm());
        }
        return o;
    }

    protected Expression relterm() throws DialectException {
        Expression o = addterm();
        while (tokenizer.getToken() == RELOP){
            String op = tokenizer.getValue();
            accept(RELOP);
            o = new Call(tag, op, o, addterm());
        }
        return o;
    }

    protected Expression addterm() throws DialectException {
        Expression o = multerm();
        while (tokenizer.getToken() == ADDOP){
            String op = tokenizer.getValue();
            accept(ADDOP);
            o = new Call(tag, op, o, multerm());
        }
        return o;
    }

    protected Expression multerm() throws DialectException {
        Expression o = binterm();
        while(tokenizer.getToken() == MULOP){
            String op = tokenizer.getValue();
            accept(MULOP);
            o = new Call(tag, op, o, binterm());
        }
        return o;
    }

    protected Expression binterm() throws DialectException {
        Expression o = monterm();
        while(tokenizer.getToken() == BINOP){
            String op = tokenizer.getValue();
            accept(BINOP);
            o = new Call(tag, op, o, monterm());
        }
        return o;
    }

    protected Expression monterm() throws DialectException {
        if (tokenizer.getToken() == MONOP){
            String op = tokenizer.getValue();
            accept(MONOP);
            return new Call(tag, op, factor());
        }
        return factor();
    }

    protected Expression factor() throws DialectException {
        if (tokenizer.getToken() == SYMBOL){
            return identifier();
        } else{
            return literal();
        }
    }

    protected Expression literal() throws DialectException {
        String s = tokenizer.getValue();
        switch(tokenizer.getToken()){
            case UNDEFINED:
                accept(UNDEFINED);
                return Literal.UNDEFINED;
            case NULL:
                accept(NULL);
                switch(tokenizer.getToken()){
                    case LEFT_PARENTHESIS:
                        List<Expression> l = new ArrayList<Expression>();
                        accept(LEFT_PARENTHESIS);
                        while(tokenizer.getToken() != RIGHT_PARENTHESIS){
                            l.add(literal());
                            if (tokenizer.getToken() != RIGHT_PARENTHESIS) accept(COMMA);
                        }
                        accept(RIGHT_PARENTHESIS);
                        return new Array(l);
                    case LEFT_BRACKET:
                        Map<String, Expression> m = new HashMap<String, Expression>();
                        accept(LEFT_BRACKET);
                        while (tokenizer.getToken() != RIGHT_BRACKET){
                            String key = tokenizer.getValue();
                            accept(SYMBOL);
                            accept(ASSIGN);
                            m.put(key, literal());
                            if (tokenizer.getToken() != RIGHT_BRACKET) accept(COMMA);
                        }
                        accept(RIGHT_BRACKET);
                        return new Context(m);
                    default:
                        return Literal.NULL;
                }
            case FUNCTOR:
                accept(FUNCTOR);
                switch(tokenizer.getToken()){
                    case LEFT_PARENTHESIS:
                        return new Functor(parametersFromArguments(argument()), statement());
                    case NULL:
                        accept(NULL);
                        if (tokenizer.getToken() == LEFT_PARENTHESIS){
                            accept(LEFT_PARENTHESIS);
                            accept(RIGHT_PARENTHESIS);
                            return new New(tag, New.ARRAY);
                        } else{
                            accept(LEFT_BRACKET);
                            accept(RIGHT_BRACKET);
                            return new New(tag, New.CONTEXT);
                        }
                    default:
                        StringBuilder sb = new StringBuilder(tokenizer.getValue());
                        accept(SYMBOL);
                        while (tokenizer.getToken() == DOT){
                            sb.append('.');
                            accept(DOT);
                            sb.append(tokenizer.getValue());
                            accept(SYMBOL);
                        }
                        return new New(tag, New.CUSTOM, sb.toString());
                }
            case BOOLEAN:
                accept(BOOLEAN);
                return s.indexOf('t') != -1 ? Logical.TRUE : Logical.FALSE;
            case INTEGER:
                accept(INTEGER);
                return new Numeral(s, true);
            case FLOATINGPOINT:
                accept(FLOATINGPOINT);
                return new Numeral(s, false);
            case STRING:
                accept(STRING);
                return new Textual(s.substring(1, s.length() - 1));
            case ERROR:
                accept(ERROR);
                return new Fault(new Exception(s.substring(1, s.length() - 1)));
            case LEFT_BRACKET:
                return Literal.UNDEFINED;
            case ADDOP:
                accept(ADDOP);
                String numeral = tokenizer.getValue();
                switch(tokenizer.getToken()){
                    case INTEGER:
                        accept(INTEGER);
                        return new Numeral(s + numeral, true);
                    case FLOATINGPOINT:
                        accept(FLOATINGPOINT);
                        return new Numeral(s + numeral, false);
                    default:
                        return new Call(tag, s, factor());
                }
            case LEFT_PARENTHESIS:
                accept(LEFT_PARENTHESIS);
                Expression o = expression();
                accept(RIGHT_PARENTHESIS);
                return new Factor(tag, o);
        }
        throw new DialectException("unidentified literal: " + s);
    }

    protected Expression identifier() throws DialectException {
        Expression selector = new Symbol(tokenizer.getValue());
        accept(SYMBOL);
        Expression s = new Fetch(tag, selector, Context.ENVIRONMENT);
        if (tokenizer.getToken() == LEFT_PARENTHESIS) s = new Call(tag, (Fetch)s, argument());
        while (tokenizer.getToken() == DOT || tokenizer.getToken() == LEFT_BRACKET){
            if (tokenizer.getToken() == DOT){
                accept(DOT);
                selector = new Symbol(tokenizer.getValue());
                accept(SYMBOL);
            } else{
                accept(LEFT_BRACKET);
                selector = expression();
                accept(RIGHT_BRACKET);
            }
            s = new Fetch(tag, selector, s);
            if (tokenizer.getToken() == LEFT_PARENTHESIS) s = new Call(tag, (Fetch)s, argument());
        }
        if (tokenizer.getToken() == ASSIGN){
            accept(ASSIGN);
            if (s instanceof Fetch){
                Fetch v = (Fetch)s;
                s = new Assignment(tag, v, expression());
            } else{
                Call f = (Call)s;
                s = new Assignment(tag, f.getVariable(), new Functor(parametersFromArguments(f.getArguments()), statement()));
            }
       }
       return s;
    }

    protected List<Expression> argument() throws DialectException {
        List<Expression> c = new ArrayList<Expression>();
        accept(LEFT_PARENTHESIS);
        while (tokenizer.getToken() != RIGHT_PARENTHESIS){
            c.add(expression());
            if (tokenizer.getToken() != RIGHT_PARENTHESIS) accept(COMMA);
        }
        accept(RIGHT_PARENTHESIS);
        return c;
    }

    private List<Symbol> parametersFromArguments(List<Expression> arguments) throws DialectException {
        List<Symbol> parameters = new ArrayList<Symbol>(arguments.size());
        for (Expression variable : arguments){
            if (!(variable instanceof Fetch)) throw new DialectException("function parameter cannot be an assigment: " + variable);
            Expression symbol = ((Fetch)variable).getSelector();
            if (!symbol.isSymbol()) throw new DialectException("function parameter not a symbol: " + symbol);
            parameters.add((Symbol)symbol);
        }
        return parameters;
    }

}
