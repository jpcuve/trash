/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Aug 23, 2002
 * Time: 3:57:00 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
statement ::= expression '.'? '!' | '!' declaration '!'
expression ::= message | message ('.' message)+ | '^' expression
message ::= factor many | factor many (';' many)+
many ::= (keyword factor binary)+ | binary
binary ::= (binaryselector factor unary)+ | unary
unary ::= identifier+ | �
factor ::=  identifier | identifier ':=' expression '.'? | literal | '(' expression '.'? ')' | '[' block ']'
literal ::= character | string | symbol | number | array
symbol ::= '#' identifier | '#' string  | '#' binaryselector
block ::= parameter? local? expression '.'?
parameter ::= (':' identifier)+ '|'
local ::= '|' identifier* '|'
declaration ::= expression '!' (method '!')+
method ::= identifier expression | binaryselector identifier expression | (keyword identifier)+ expression
*/

public class Parser {
    private int lookAhead;
    private Tokenizer ct;

    public Parser(Tokenizer ct)	{
        this.ct = ct;
    }

    private void accept(int t){
        if (lookAhead != t) throw new RuntimeException("Syntax error: token type " + t + " expected (found token '" + ct.sval + "' -type " + lookAhead + "- instead).");
        lookAhead = ct.nextToken();
    }


    public void parse(Segment s){
        lookAhead = ct.nextToken();
        this.statement(s);
    }

    private void statement(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_EXCLAMATION:
                accept(Tokenizer.TT_EXCLAMATION);
                declaration(s);
                break;
            default:
                expression(s);
                break;
        }
        accept(Tokenizer.TT_EXCLAMATION);
    }

    private void declaration(Segment s) {
        expression(s); // must evaluate to response class
        accept(Tokenizer.TT_EXCLAMATION);
        do{
            Segment body = new Segment();
            method(body);
            Method m = new Method(body);
            accept(Tokenizer.TT_EXCLAMATION);
        } while(lookAhead != Tokenizer.TT_EXCLAMATION);
    }

    private void method(Segment s) {

    }

    private void expression(Segment s) {
        if (lookAhead == Tokenizer.TT_RETURN) {
            accept(Tokenizer.TT_RETURN);
            expression(s);
            s.addImplicit(Instruction.ARET);
        } else {
            message(s);
            boolean loop = true;
            while (loop) {
                switch(lookAhead) {
                    case Tokenizer.TT_DOT:
                        accept(Tokenizer.TT_DOT);
                        if (lookAhead != Tokenizer.TT_EXCLAMATION) {
                            message(s);
                        } else {
                            loop = false;
                        }
                        break;
                    default:
                        loop = false;
                        break;
                }
            }
        }
    }

    // message ::= factor many | factor many (';' many)+
    private void message(Segment s) {
        factor(s);
        s.addImplicit(Instruction.STORE);
        many(s);
        boolean loop = true;
        while(loop) {
            switch(lookAhead) {
                case Tokenizer.TT_SEMICOLON:
                    s.addImplicit(Instruction.RECALL);
                    accept(Tokenizer.TT_SEMICOLON);
                    // System.out.println("cascade");
                    many(s);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    // many ::= (keyword factor binary)+ | binary
    private void many(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_KEYWORD:
                boolean loop = true;
                int i = 0;
                StringBuffer sb = new StringBuffer();
                while (loop) {
                    switch(lookAhead) {
                        case Tokenizer.TT_KEYWORD:
                            // System.out.println("push keyword: " + ct.sval);
                            sb.append(ct.sval);
                            accept(Tokenizer.TT_KEYWORD);
                            factor(s);
                            binary(s);
                            i++;
                            break;
                        default:
                            loop = false;
                            break;
                    }
                }
                if (i > 0) {
                    // System.out.println("call many with " + expirationDate + " parameter(s)");
                    // sb.append("~" + expirationDate);
                    s.addImmediate(Instruction.INVOKE, sb.toString());
                }
                break;
            default:
                binary(s);
                break;
        }
    }

    // binary ::= (binaryselector factor unary)+ | unary
    private void binary(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_BINARYSELECTOR:
                boolean loop = true;
                while (loop) {
                    switch(lookAhead) {
                        case Tokenizer.TT_BINARYSELECTOR:
                            // System.out.println("push binary selector: " + ct.sval);
                            String bf = ct.sval + ":";
                            accept(Tokenizer.TT_BINARYSELECTOR);
                            factor(s);
                            unary(s);
                            s.addImmediate(Instruction.INVOKE, bf);
                            // System.out.println("call binary");
                            break;
                        default:
                            loop = false;
                            break;
                    }
                }
                break;
            default:
                unary(s);
                break;
        }
    }

    private void unary(Segment s) {
        boolean loop = true;
        while (loop) {
            switch(lookAhead) {
                case Tokenizer.TT_IDENTIFIER:
                    // System.out.println("push identifier: " + ct.sval);
                    String sf = ct.sval;
                    accept(Tokenizer.TT_IDENTIFIER);
                    // System.out.println("call unary");
                    s.addImmediate(Instruction.INVOKE, sf);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private void factor(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_IDENTIFIER:
                // System.out.println("push identifier: " + ct.sval);
                String var = ct.sval;
                accept(Tokenizer.TT_IDENTIFIER);
                if (lookAhead == Tokenizer.TT_ASSIGN) {
                    accept(Tokenizer.TT_ASSIGN);
                    s.addImmediate(Instruction.LVALUE, var);
                    expression(s);
                    if (lookAhead == Tokenizer.TT_DOT) {
                        accept(Tokenizer.TT_DOT);
                    }
                    // System.out.println("assign");
                    s.addImplicit(Instruction.ASSIGN);
                }
                s.addImmediate(Instruction.RVALUE, var);
                break;
            case Tokenizer.TT_LEFTPARENTHESIS:
                accept(Tokenizer.TT_LEFTPARENTHESIS);
                expression(s);
                if (lookAhead == Tokenizer.TT_DOT) {
                    accept(Tokenizer.TT_DOT);
                }
                accept(Tokenizer.TT_RIGHTPARENTHESIS);
                break;
            case Tokenizer.TT_LEFTBRACKET:
                accept(Tokenizer.TT_LEFTBRACKET);
                block(s);
                accept(Tokenizer.TT_RIGHTBRACKET);
                break;
            default:
                literal(s);
                break;
        }
    }

    private void literal(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_CHARACTER:
                // System.out.println("push char: " + ct.sval.charAt(1));
                s.addImmediate(Instruction.CONST, new Character(ct.sval.charAt(1)));
                accept(Tokenizer.TT_CHARACTER);
                break;
            case Tokenizer.TT_STRING:
                int l = ct.sval.length();
                // System.out.println("push string: " + ct.sval.substring(1, ct.sval.length() - 1));
                s.addImmediate(Instruction.CONST, ct.sval.substring(1, ct.sval.length() - 1));
                accept(Tokenizer.TT_STRING);
                break;
            case Tokenizer.TT_SHARP:
                accept(Tokenizer.TT_SHARP);
                symbol(s);
                break;
            case Tokenizer.TT_INTEGER:
                // System.out.println("push int: " + ct.sval);
                s.addImmediate(Instruction.CONST, new BigInteger(ct.sval));
                accept(Tokenizer.TT_INTEGER);
                break;
            case Tokenizer.TT_FLOATINGPOINT:
                // System.out.println("push float: " + ct.sval);
                s.addImmediate(Instruction.CONST, new BigDecimal(ct.sval));
                accept(Tokenizer.TT_FLOATINGPOINT);
                break;
            case Tokenizer.TT_LEFTARRAY:
                accept(Tokenizer.TT_LEFTARRAY);
                // System.out.println("push array: (");
                l = 0;
                while (lookAhead != Tokenizer.TT_RIGHTPARENTHESIS) {
                    literal(s);
                    l++;
                }
                accept(Tokenizer.TT_RIGHTPARENTHESIS);
                s.addImmediate(Instruction.ARRAY, new BigInteger(new Integer(l).toString()));
                // System.out.println(")");
                break;
            default:
                throw new RuntimeException("Unexpected token");
        }
    }

    private void symbol(Segment s) {
        switch(lookAhead) {
            case Tokenizer.TT_IDENTIFIER:
                System.out.println("push symbol: #" + ct.sval);
                accept(Tokenizer.TT_IDENTIFIER);
                break;
            case Tokenizer.TT_STRING:
                System.out.println("push symbol: #" + ct.sval);
                accept(Tokenizer.TT_STRING);
                break;
            case Tokenizer.TT_BINARYSELECTOR:
                System.out.println("push symbol: #" + ct.sval);
                accept(Tokenizer.TT_BINARYSELECTOR);
                break;
            default:
                throw new RuntimeException("Invalid symbol");
        }
    }

    private void block(Segment s) {
        Segment body = new Segment();
        parameter(body);
        local(body);
        expression(body);
        if (lookAhead == Tokenizer.TT_DOT) {
            accept(Tokenizer.TT_DOT);
        }
        Closure c = new Closure(body);
        s.addImmediate(Instruction.CONST, c);
    }

    private void parameter(Segment s) {
        if (lookAhead == Tokenizer.TT_COLON) {
            int i = 0;
            while(lookAhead == Tokenizer.TT_COLON) {
                accept(Tokenizer.TT_COLON);
                // System.out.println("argument: " + ct.sval);
                s.addImmediate(Instruction.LVALUE, ct.sval);
                accept(Tokenizer.TT_IDENTIFIER);
                s.addImmediate(Instruction.CONST, new BigInteger(new Integer(i).toString()));
                s.addImplicit(Instruction.LOCAL);
                s.addImplicit(Instruction.ASSIGN);
                i++;
            }
            accept(Tokenizer.TT_OR);
        }

    }

    private void local(Segment s) {
        if (lookAhead == Tokenizer.TT_OR) {
            accept(Tokenizer.TT_OR);
            while(lookAhead == Tokenizer.TT_IDENTIFIER) {
                // System.out.println("local: " + ct.sval);
                s.addImmediate(Instruction.LVALUE, ct.sval);
                accept(Tokenizer.TT_IDENTIFIER);
                s.addImplicit(Instruction.NIL);
                s.addImplicit(Instruction.ASSIGN);
            }
            accept(Tokenizer.TT_OR);
        }
    }

}
