/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 14-May-02
 * Time: 17:16:00
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

public class Parser {
    private Tokenizer ct;
    private int lookAhead;

    public Parser(Tokenizer ct)	{
        this.ct = ct;
    }

    private void accept(int t){
        if (lookAhead != t) throw new RuntimeException("Syntax error: token type " + t + " expected (found token '" + ct.sval + "' -type " + lookAhead + "- instead).");
        lookAhead = ct.nextToken();
    }

    public void parse(Machine cm){
        lookAhead = ct.nextToken();
        this.readLine(cm);
    }

    private void readLine(Machine cm){
        switch(lookAhead){
            case Tokenizer.TT_EOF:
            case Tokenizer.TT_EOL:
                break;
            case Tokenizer.TT_EXCLAMATION:
                accept(Tokenizer.TT_EXCLAMATION);
                readBranch(cm);
                cm.addImplicit(Machine.DISP);
                break;
            default:
                readAssignment(cm);
                break;
        }
        if (lookAhead == Tokenizer.TT_EOF) accept(Tokenizer.TT_EOF);
        else accept(Tokenizer.TT_EOL);
    }

    private void readAssignment(Machine cm){
        cm.addImmediate(Machine.LVALUE, ct.sval);
        this.accept(Tokenizer.TT_SYMBOL);
        this.accept(Tokenizer.TT_ASSIGN);
        readBranch(cm);
        cm.addImplicit(Machine.ASSIGN);
    }

    private void readBranch(Machine cm){
        readTest(cm);
        boolean loop = true;
        while(loop){
            switch(lookAhead){
                case Tokenizer.TT_QUESTION:
                    accept(Tokenizer.TT_QUESTION);
                    String l1 = cm.nextLabel();
                    cm.addImmediate(Machine.BEQ, l1);
                    readTest(cm);
                    String l2 = cm.nextLabel();
                    cm.addImmediate(Machine.BRA, l2);
                    accept(Tokenizer.TT_COLON);
                    cm.addLabel(l1);
                    readTest(cm);
                    cm.addLabel(l2);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private void readTest(Machine cm){
        readExpression(cm);
        boolean loop = true;
        while(loop){
            switch(lookAhead){
                case Tokenizer.TT_LT:
                    accept(Tokenizer.TT_LT);
                    readExpression(cm);
                    cm.addImplicit(Machine.LT);
                    break;
                case Tokenizer.TT_LE:
                    accept(Tokenizer.TT_LE);
                    readExpression(cm);
                    cm.addImplicit(Machine.LE);
                    break;
                case Tokenizer.TT_EQ:
                    accept(Tokenizer.TT_EQ);
                    readExpression(cm);
                    cm.addImplicit(Machine.EQ);
                    break;
                case Tokenizer.TT_GE:
                    accept(Tokenizer.TT_GE);
                    readExpression(cm);
                    cm.addImplicit(Machine.GE);
                    break;
                case Tokenizer.TT_GT:
                    accept(Tokenizer.TT_GT);
                    readExpression(cm);
                    cm.addImplicit(Machine.GT);
                    break;
                case Tokenizer.TT_NE:
                    accept(Tokenizer.TT_NE);
                    readExpression(cm);
                    cm.addImplicit(Machine.NE);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private void readExpression(Machine cm){
        readTerm(cm);
        boolean loop = true;
        while(loop){
            switch(lookAhead){
                case Tokenizer.TT_ADD:
                    accept(Tokenizer.TT_ADD);
                    readTerm(cm);
                    cm.addImplicit(Machine.ADD);
                    break;
                case Tokenizer.TT_SUB:
                    accept(Tokenizer.TT_SUB);
                    readTerm(cm);
                    cm.addImplicit(Machine.SUB);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private void readTerm(Machine cm){
        readFact(cm);
        boolean loop = true;
        while(loop){
            switch(lookAhead){
                case Tokenizer.TT_MUL:
                    accept(Tokenizer.TT_MUL);
                    readFact(cm);
                    cm.addImplicit(Machine.MUL);
                    break;
                case Tokenizer.TT_DIV:
                    accept(Tokenizer.TT_DIV);
                    readFact(cm);
                    cm.addImplicit(Machine.DIV);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private void readFact(Machine cm){
        switch(lookAhead){
            case Tokenizer.TT_LEFTPARENTHESIS:
                accept(Tokenizer.TT_LEFTPARENTHESIS);
                readBranch(cm);
                accept(Tokenizer.TT_RIGHTPARENTHESIS);
                break;
            case Tokenizer.TT_SYMBOL:
                cm.addImmediate(Machine.RVALUE, ct.sval);
                accept(Tokenizer.TT_SYMBOL);
                break;
            case Tokenizer.TT_ADD:
                accept(Tokenizer.TT_ADD);
                readBranch(cm);
                break;
            case Tokenizer.TT_SUB:
                accept(Tokenizer.TT_SUB);
                readBranch(cm);
                cm.addImplicit(Machine.NEG);
                break;
            case Tokenizer.TT_INTEGER:
                cm.addImmediate(Machine.INTEGER, ct.sval);
                accept(Tokenizer.TT_INTEGER);
                break;
            case Tokenizer.TT_FLOATINGPOINT:
                cm.addImmediate(Machine.FLOATINGPOINT, ct.sval);
                accept(Tokenizer.TT_FLOATINGPOINT);
                break;
            default:
                throw new RuntimeException("Unexpected token type " + lookAhead);
        }
    }

}
