package lang.dialect.terminal.lib;

import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.terminal.*;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Library;
import lang.dialect.terminal.Literal;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:49:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoreLibrary extends Library {
    public CoreLibrary() {
        super("core");
        addPrimitive(new Primitive("isUndefined", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0) == Literal.UNDEFINED ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isNull", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0) == Literal.NULL ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isLogical", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isLogical() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isNumeral", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isNumeral() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isInteger", 1){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return args.get(0).isNumeral() && args.get(0).toNumeral().isInteger() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isFloat", 1){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return args.get(0).isNumeral() && !args.get(0).toNumeral().isInteger() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isTextual", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isTextual() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isFault", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isFault() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isSymbol", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isSymbol() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isArray", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isArray() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("isContext", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).isContext() ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("+", 1, 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return args.size() == 1 ? monadics('+', args) : add(args.get(0), args.get(1));
            }
        });
        addPrimitive(new Primitive("-", 1, 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return args.size() == 1 ? monadics('-', args) : dyadics('-', args);
            }
        });
        addPrimitive(new Primitive("*", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return dyadics('*', args);
            }
        });
        addPrimitive(new Primitive("/", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return dyadics('/', args);
            }
        });
        addPrimitive(new Primitive("&", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return dyadics('&', args);
            }
        });
        addPrimitive(new Primitive("|", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return dyadics('|', args);
            }
        });
        addPrimitive(new Primitive("^", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return dyadics('*', args);
            }
        });
        addPrimitive(new Primitive("<>", 2){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).equals(args.get(1)) ? Logical.FALSE : Logical.TRUE;
            }
        });
        addPrimitive(new Primitive("==", 2){
            public Expression eval(List<Expression> args, Context environment) {
                return args.get(0).equals(args.get(1)) ? Logical.TRUE : Logical.FALSE;
            }
        });
        addPrimitive(new Primitive("<", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return comparisons('<', args);
            }
        });
        addPrimitive(new Primitive("<=", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return comparisons('L', args);
            }
        });
        addPrimitive(new Primitive(">=", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return comparisons('G', args);
            }
        });
        addPrimitive(new Primitive(">", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return comparisons('>', args);
            }
        });
        addPrimitive(new Primitive("&&", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return logicals('&', args);
            }
        });
        addPrimitive(new Primitive("||", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return logicals('|', args);
            }
        });
        addPrimitive(new Primitive("^^", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return logicals('^', args);
            }
        });
        addPrimitive(new Primitive("!", 1){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return logicals('!', args);
            }
        });
        addPrimitive(new Primitive("environment"){
            public Expression eval(List<Expression> args, Context environment) {
                return environment;
            }
        });
        addPrimitive(new Primitive("length", 1){
            public Expression eval(List<Expression> args, Context environment) {
                return length(args.get(0));
            }
        });
        addPrimitive(new Primitive("out", 1){
            public Expression eval(List<Expression> args, Context environment) {
                System.out.println(args.get(0).stringValue());
                return Literal.UNDEFINED;
            }
        });
        addPrimitive(new Primitive("err", 1){
            public Expression eval(List<Expression> args, Context environment) {
                System.err.println(args.get(0).stringValue());
                return Literal.UNDEFINED;
            }
        });
    }

    private Numeral length(Expression o){
        if (o.isArray()) return new Numeral(((Array)o).elements().size());
        if (o.isContext()) return new Numeral(((Context)o).entrySet().size());
        if (o.isTextual()) return new Numeral(o.stringValue().length());
        return Numeral.ZERO;
    }

    private Expression add(Expression o1, Expression o2) throws DialectException {
        if (o1.isNumeral() && o2.isNumeral()){
            Numeral n1 = o1.toNumeral();
            Numeral n2 = o2.toNumeral();
            if (n1.isInteger() && n2.isInteger()){
                return new Numeral(n1.longValue() + n2.longValue());
            } else{
                return new Numeral(n1.doubleValue() + n2.doubleValue());
            }
        } else{
            return new Textual(o1.stringValue() + o2.stringValue());
        }
    }

    private Expression logicals(char op, List args) throws DialectException {
        Expression arg1 = (Expression)args.get(0);
        if (!arg1.isLogical()) return Literal.UNDEFINED;
        boolean f1 = arg1.toLogical().booleanValue();
        if (op == '!') return f1 ? Logical.FALSE : Logical.TRUE;
        Expression arg2 = (Expression)args.get(1);
        if (!arg2.isLogical()) return Literal.UNDEFINED;
        boolean f2 = arg2.toLogical().booleanValue();
        boolean f = false;
        switch(op){
            case '&': f = f1 && f2; break;
            case '|': f = f1 || f2; break;
            case '^': f = !((f1 && f2) || (!f1 && !f2)); break;
        }
        return f ? Logical.TRUE : Logical.FALSE;
    }

    private Expression comparisons(char op, List args) throws DialectException {
        Expression arg1 = (Expression)args.get(0);
        Expression arg2 = (Expression)args.get(1);
        if (!(arg1.isNumeral() || arg2.isNumeral())) return Literal.UNDEFINED;
        double d1 = arg1.toNumeral().doubleValue();
        double d2 = arg2.toNumeral().doubleValue();
        boolean f = false;
        switch(op){
            case '<': f = (d1 < d2); break;
            case '>': f = (d1 > d2); break;
            case 'L': f = (d1 <= d2); break;
            case 'G': f = (d1 >= d2); break;
        }
        return f ? Logical.TRUE : Logical.FALSE;
    }

    private Expression monadics(char op, List args) throws DialectException {
        Expression arg = (Expression)args.get(0);
        if (!arg.isNumeral()) return Literal.UNDEFINED;
        Numeral n = arg.toNumeral();
        if (n.isInteger()){
            long l = n.longValue();
            switch(op){
                case '+': n = new Numeral(l); break;
                case '-': n = new Numeral(-l); break;
                case '~': n = new Numeral(~l); break;
            }
        } else{
            double d = n.doubleValue();
            switch(op){
                case '+': n = new Numeral(d); break;
                case '-': n = new Numeral(-d); break;
                case '~': return Literal.UNDEFINED;
            }
        }
        return n;
    }

    private Expression dyadics(char op, List args) throws DialectException {
        Expression arg1 = (Expression)args.get(0);
        Expression arg2 = (Expression)args.get(1);
        if (!(arg1.isNumeral() && arg2.isNumeral())) return Literal.UNDEFINED;
        Numeral n1 = arg1.toNumeral();
        Numeral n2 = arg2.toNumeral();
        if (n1.isInteger() && n2.isInteger()){
            long l1 = n1.longValue();
            long l2 = n2.longValue();
            switch(op){
                case '&': n1 = new Numeral(l1 & l2); break;
                case '|': n1 = new Numeral(l1 | l2); break;
                case '-': n1 = new Numeral(l1 - l2); break;
                case '*': n1 = new Numeral(l1 * l2); break;
                case '/':
                    double d1 = n1.doubleValue();
                    double d2 = n2.doubleValue();
                    n1 = (d1 / d2 == l1 / l2) ? new Numeral((long)(l1 / l2)) : new Numeral(d1 / d2);
                    break;
            }
        } else{
            double d1 = n1.doubleValue();
            double d2 = n2.doubleValue();
            switch(op){
                case '&': return Literal.UNDEFINED;
                case '|': return Literal.UNDEFINED;
                case '-': n1 = new Numeral(d1 - d2); break;
                case '*': n1 = new Numeral(d1 * d2); break;
                case '/': n1 = new Numeral(d1 / d2); break;
            }
        }
        return n1;
    }
}
