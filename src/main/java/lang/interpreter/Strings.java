/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jul 26, 2002
 * Time: 5:40:18 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.interpreter;

public class Strings extends Pack {

    public static final int // strings
		STRING_QUESTION = 133, STRING_LENGTH = 136,
		STRING_EQUAL_TO = 139, STRING_LESS_THAN = 140, STRING_MORE_THAN = 141, STRING_LESS_OR_EQUAL_TO = 142, STRING_MORE_OR_EQUAL_TO = 143,
		STRING_CI_EQUAL_TO = 144, STRING_CI_LESS_THAN = 145, STRING_CI_MORE_THAN = 146, STRING_CI_LESS_OR_EQUAL_TO = 147, STRING_CI_MORE_OR_EQUAL_TO = 148,
		SUBSTRING = 149, STRING_APPEND = 150, STRING_COPY = 153, STRING_TO_NUMBER = 155;

    public Strings() {
		int n = Integer.MAX_VALUE;
		addFunction(STRING_APPEND, "string-append", 1, n);
		addFunction(STRING_COPY, "string-copy", 1);
		addFunction(STRING_EQUAL_TO, "string=?", 2);
		addFunction(STRING_LESS_THAN, "string<?", 2);
		addFunction(STRING_MORE_THAN, "string>?", 2);
		addFunction(STRING_LESS_OR_EQUAL_TO, "string<=?", 2);
		addFunction(STRING_MORE_OR_EQUAL_TO, "string>=?", 2);
		addFunction(STRING_CI_EQUAL_TO, "string-ci=?", 2);
		addFunction(STRING_CI_LESS_THAN, "string-ci<?", 2);
		addFunction(STRING_CI_MORE_THAN, "string-ci>?", 2);
		addFunction(STRING_CI_LESS_OR_EQUAL_TO, "string-ci<=?", 2);
		addFunction(STRING_CI_MORE_OR_EQUAL_TO, "string-ci>=?", 2);
		addFunction(STRING_LENGTH, "string-length", 1);
		addFunction(STRING_QUESTION, "string?", 1);
        addFunction(STRING_TO_NUMBER, "string->number", 1);
		addFunction(SUBSTRING, "substring", 3);
	}

    public Expression f(int index, Interpreter ip, Expression arg, Environment env) throws InterpreterException {
		// System.out.println("arg=" + arg.write());
		switch(index) {
            case STRING_QUESTION: return Bool.tf(arg.first() instanceof Str);
            case STRING_LENGTH: return ((Str)arg.first()).length();
            case STRING_EQUAL_TO: return compareStrings_(arg, '=', false);
            case STRING_LESS_THAN: return compareStrings_(arg, '<', false);
            case STRING_MORE_THAN: return compareStrings_(arg, '>', false);
            case STRING_LESS_OR_EQUAL_TO: return compareStrings_(arg, 'L', false);
            case STRING_MORE_OR_EQUAL_TO: return compareStrings_(arg, 'G', false);
            case STRING_CI_EQUAL_TO: return compareStrings_(arg, '=', true);
            case STRING_CI_LESS_THAN: return compareStrings_(arg, '<', true);
            case STRING_CI_MORE_THAN: return compareStrings_(arg, '>', true);
            case STRING_CI_LESS_OR_EQUAL_TO: return compareStrings_(arg, 'L', true);
            case STRING_CI_MORE_OR_EQUAL_TO: return compareStrings_(arg, 'G', true);
            case SUBSTRING: return ((Str)arg.first()).subString((Number)arg.second(), (Number)arg.third());
            case STRING_APPEND: return ((Str)arg.first()).append(arg.rest());
            case STRING_COPY: return new Str((Str)arg.first());
		}
		throw new InterpreterException(Error.UNIMPLEMENTED_FEATURE, toString() + "#" + index);
	}

    private Expression compareStrings_(Expression arg, char comp, boolean f)
	throws InterpreterException {
		for(Expression cur = arg; cur.rest() instanceof Pair; cur = cur.rest()) {
			Str x = (Str)cur.first();
			Str y = (Str)cur.second();
			switch(comp) {
				case '>': if(x.compareTo(y, f) <= 0) return Bool.FALSE; break;
				case '<': if(x.compareTo(y, f) >= 0) return Bool.FALSE; break;
				case 'G': if(x.compareTo(y, f) < 0) return Bool.FALSE; break;
				case 'L': if(x.compareTo(y, f) > 0) return Bool.FALSE; break;
				default: if(x.compareTo(y, f) != 0) return Bool.FALSE; break;
			}
		}
		return Bool.TRUE;
	}


}
