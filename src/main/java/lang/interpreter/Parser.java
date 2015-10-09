/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jul 29, 2002
 * Time: 9:32:48 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.interpreter;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Stack;

/**
 * Parsing rules:
 * expression = '(' expression* ')' | QUOTE expression | QUASIQUOTE expression | ... | DOT | IDENTIFIER | BOOLEAN | EOF
 */

public class Parser {
    private Tokenizer ct;
    private int lookAhead;
    private HashMap symbols;

    public Parser() {
        symbols = new HashMap();
        for (int i = 0; i < Symbol.BUILTIN.length; i++) {
            symbols.put(Symbol.BUILTIN[i].stringValue(), Symbol.BUILTIN[i]);
        }
    }

    public Symbol recSymbol(String s){
		Symbol sym = (Symbol)symbols.get(s);
		if(sym == null){
			sym = new Symbol(s);
			symbols.put(s , sym);
		}
		return sym;
	}

    private void accept(Tokenizer ct, int t){
        if (lookAhead != t) throw new RuntimeException("Syntax error: token type " + t + " expected (found token '" + ct.sval + "' -type " + lookAhead + "- instead).");
        lookAhead = ct.nextToken();
        // System.out.println("Token=" + ct.sval + "(type=" + lookAhead + ")");
    }

    public Expression parse(Tokenizer ct){
        lookAhead = ct.nextToken();
        // System.out.println("Token=" + ct.sval + "(type=" + lookAhead + ")");
        return expression(ct);
    }

    public Expression expression(Tokenizer ct) {
        Expression e = null;
        switch(lookAhead) {
            case Tokenizer.TT_LEFTPARENTHESIS:
                accept(ct, Tokenizer.TT_LEFTPARENTHESIS);
                Stack st = new Stack();
                int dot = 0;
                int i = 0;
                while(lookAhead != Tokenizer.TT_RIGHTPARENTHESIS) {
                    if (lookAhead == Tokenizer.TT_DOT) {
                        accept(ct, Tokenizer.TT_DOT);
                        dot = i;
                    }
                    st.push(expression(ct));
                    i++;
                }
                accept(ct, Tokenizer.TT_RIGHTPARENTHESIS);
                e = Nil.NIL;
                while(!st.empty()) {
                    Expression p = (Expression)st.pop();
                    if (i != dot) {
                        e = p.cons(e);
                    } else {
                        if (e instanceof Pair) {
                            e = p.cons(((Pair)e).first());
                        }
                    }
                    i--;
                }
                return e;
            case Tokenizer.TT_QUOTE:
                accept(ct, Tokenizer.TT_QUOTE);
                return Symbol.QUOTE.cons(expression(ct).cons(Nil.NIL));
            case Tokenizer.TT_QUASIQUOTE:
                accept(ct, Tokenizer.TT_QUASIQUOTE);
                return Symbol.QUASIQUOTE.cons(expression(ct).cons(Nil.NIL));
            case Tokenizer.TT_UNQUOTE:
                accept(ct, Tokenizer.TT_UNQUOTE);
                return Symbol.UNQUOTE.cons(expression(ct).cons(Nil.NIL));
            case Tokenizer.TT_UNQUOTESPLICING:
                accept(ct, Tokenizer.TT_UNQUOTESPLICING);
                return Symbol.UNQUOTESPLICING.cons(expression(ct).cons(Nil.NIL));
           case Tokenizer.TT_IDENTIFIER:
                e = recSymbol(ct.sval);
                accept(ct, Tokenizer.TT_IDENTIFIER);
                return e;
            case Tokenizer.TT_BOOLEAN:
                e = ct.sval.indexOf('t') != -1 ? Bool.TRUE : Bool.FALSE;
                accept(ct, Tokenizer.TT_BOOLEAN);
                return e;
            case Tokenizer.TT_NATURAL:
                e = new Number(Long.valueOf(ct.sval).longValue());
                accept(ct, Tokenizer.TT_NATURAL);
                return e;
            case Tokenizer.TT_EOF:
                accept(ct, Tokenizer.TT_EOF);
                throw new RuntimeException("Unbalanced parenthesis");
        }
        throw new RuntimeException("Parse error: " + ct.sval);
    }

    public static void main(String[] args) {
        Parser p = new Parser();
        Expression e = p.parse(new Tokenizer(new StringReader("(+ 3 9)")));
        System.out.println(e.write());
    }

}
