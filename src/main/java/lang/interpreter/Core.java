package lang.interpreter;

public class Core extends Pack {
	public static final int // equivalence
		EQV_QUESTION = 1, EQ_QUESTION = 2, EQUAL_QUESTION = 3;

	public static final int // numbers
		NUMBER_QUESTION = 4,
		EQUAL_TO = 11, LESS_THAN = 12, MORE_THAN = 13, LESS_OR_EQUAL_TO = 14, MORE_OR_EQUAL_TO = 15,
        ZERO_QUESTION = 16, POSITIVE_QUESTION = 17, NEGATIVE_QUESTION = 18,
		ODD_QUESTION = 19, EVEN_QUESTION = 20, MAX = 21, MIN = 22, ADD = 23, SUB = 24, MUL = 25, ABS = 27,
		QUOTIENT = 28, REMAINDER = 29, MODULO = 30,
        SQRT = 48,
        NUMBER_TO_STRING = 58;

	public static final int // booleans
		NOT = 60, BOOLEAN_QUESTION = 61;

	public static final int // pairs & lists
		PAIR_QUESTION = 62, CONS = 63, CAR = 64, CDR = 65, SET_CAR_EXCLAMATION = 66, SET_CDR_EXCLAMATION = 67,
		CAAAAR = 68, CAAADR = 69, CAAAR = 70, CAADAR = 71, CAADDR = 72, CAADR = 73, CAAR = 74, CADAAR = 75,
		CADADR = 76, CADAR = 77, CADDAR = 78, CADDDR = 79, CADDR = 80, CADR = 81, CDAAAR = 82, CDAADR = 83,
		CDAAR = 84, CDADAR = 85, CDADDR = 86, CDADR = 87, CDAR = 88, CDDAAR = 89, CDDADR = 90, CDDAR = 91,
		CDDDAR = 92, CDDDDR = 93, CDDDR = 94, CDDR = 95, NULL_QUESTION = 96, LIST_QUESTION = 97, LIST = 98,
		LENGTH = 99, APPEND = 100, REVERSE = 101, LIST_TAIL = 102, LIST_REF = 103, MEMQ = 104,
		MEMV = 105, MEMBER = 106, ASSQ = 107, ASSV = 108, ASSOC = 109;

	public static final int // symbols
		SYMBOL_QUESTION = 110;

	public static final int // control
		PROCEDURE_QUESTION = 164, APPLY = 165, MAP = 166, FOR_EACH = 167,
        EVAL = 173;

	public Core() {
		int n = Integer.MAX_VALUE;
		addFunction(ABS, "abs", 1);
		addFunction(ADD, "+", 0, n);
		addFunction(APPEND, "append");
		addFunction(APPLY, "apply");
		addFunction(ASSOC, "assoc", 2);
		addFunction(ASSQ, "assq", 2);
		addFunction(ASSV, "assv", 2);
		addFunction(BOOLEAN_QUESTION, "boolean?", 1);
		addFunction(CONS, "cons", 2, 2);
		addFunction(CAAAAR, "caaaar", 1);
		addFunction(CAAADR, "caaadr", 1);
		addFunction(CAAAR, "caaar", 1);
		addFunction(CAADAR, "caadar", 1);
		addFunction(CAADDR, "caaddr", 1);
		addFunction(CAADR, "caadr", 1);
		addFunction(CAAR, "caar", 1);
		addFunction(CADAAR, "cadaar", 1);
		addFunction(CADADR, "cadadr", 1);
		addFunction(CADAR, "cadar", 1);
		addFunction(CADDAR, "caddar", 1);
		addFunction(CADDDR, "cadddr", 1);
		addFunction(CADDR, "caddr", 1);
		addFunction(CADR, "cadr", 1);
		addFunction(CAR, "car", 1);
		addFunction(CDAAAR, "cdaaar", 1);
		addFunction(CDAADR, "cdaadr", 1);
		addFunction(CDAAR, "cdaar", 1);
		addFunction(CDADAR, "cdadar", 1);
		addFunction(CDADDR, "cdaddr", 1);
		addFunction(CDADR, "cdadr", 1);
		addFunction(CDAR, "cdar", 1);
		addFunction(CDDAAR, "cddaar",1);
		addFunction(CDDADR, "cddadr", 1);
		addFunction(CDDAR, "cddar", 1);
		addFunction(CDDDAR, "cdddar", 1);
		addFunction(CDDDDR, "cddddr", 1);
		addFunction(CDDDR, "cdddr", 1);
		addFunction(CDDR, "cddr", 1);
		addFunction(CDR, "cdr", 1);
		addFunction(EQ_QUESTION, "eq?", 2);
		addFunction(EQUAL_QUESTION, "equal?", 2);
		addFunction(EQUAL_TO, "=", 2, n);
		addFunction(EQV_QUESTION, "eqv?", 2);
		addFunction(EVAL, "eval", 1);
		addFunction(EVEN_QUESTION, "even?", 1);
		addFunction(FOR_EACH, "for-each", 2,n);
		addFunction(LENGTH, "length", 1);
		addFunction(LESS_OR_EQUAL_TO, "<=", 2, n);
		addFunction(LESS_THAN, "<", 2, n);
		addFunction(LIST, "list");
		addFunction(LIST_QUESTION, "list?", 1);
		addFunction(LIST_TAIL, "list-tail", 2);
		addFunction(LIST_REF, "list-ref", 2);
		addFunction(MAP, "map", 2, n);
		addFunction(MAX, "max", 2, n);
		addFunction(MEMBER, "member", 2);
		addFunction(MEMQ, "memq", 2);
		addFunction(MEMV, "memv", 2);
		addFunction(MIN, "min", 2, n);
		addFunction(MODULO, "modulo", 2);
		addFunction(MORE_OR_EQUAL_TO, ">=", 2, n);
		addFunction(MORE_THAN, ">", 2, n);
		addFunction(MUL, "*", 0, n);
		addFunction(NEGATIVE_QUESTION, "negative?", 1);
		addFunction(NOT, "not", 1);
		addFunction(NUMBER_QUESTION, "number?", 1);
		addFunction(NUMBER_TO_STRING, "number->string", 1);
		addFunction(NULL_QUESTION, "null?" , 1);
		addFunction(ODD_QUESTION, "odd?", 1);
		addFunction(PAIR_QUESTION, "pair?", 1);
		addFunction(POSITIVE_QUESTION, "positive?", 1);
		addFunction(PROCEDURE_QUESTION, "procedure?", 1);
		addFunction(QUOTIENT, "quotient", 2);
		addFunction(REMAINDER, "remainder", 2);
		addFunction(REVERSE, "reverse", 1);
		addFunction(SQRT, "sqrt", 1);
		addFunction(SET_CAR_EXCLAMATION, "from-car!", 2);
		addFunction(SET_CDR_EXCLAMATION, "from-cdr!", 2);
		addFunction(SYMBOL_QUESTION, "symbol?", 1);
		addFunction(SUB, "-", 1, n);
		addFunction(ZERO_QUESTION, "zero?", 1);
	}

	public Expression f(int index, Interpreter ip, Expression arg, Environment env) throws InterpreterException {
		// System.out.println("arg=" + arg.write());
		switch(index) {
			// equivalence
		case EQV_QUESTION: return Bool.tf(arg.first().isEqv(arg.second()));
		case EQ_QUESTION: return Bool.tf(arg.first().isEq(arg.second()));
		case EQUAL_QUESTION: return Bool.tf(arg.first().isEqual(arg.second()));
		// numbers
		case NUMBER_QUESTION: return Bool.tf(arg.first() instanceof Number);
		case EQUAL_TO: return compareNumbers_(arg, '=');
		case LESS_THAN: return compareNumbers_(arg, '<');
		case MORE_THAN: return compareNumbers_(arg, '>');
		case LESS_OR_EQUAL_TO: return compareNumbers_(arg, 'L');
		case MORE_OR_EQUAL_TO: return compareNumbers_(arg, 'G');
		case ZERO_QUESTION: return Bool.tf(((Number)arg.first()).compareTo(Number.ZERO) == 0);
		case POSITIVE_QUESTION: return Bool.tf(((Number)arg.first()).compareTo(Number.ZERO) > 0);
		case NEGATIVE_QUESTION: return Bool.tf(((Number)arg.first()).compareTo(Number.ZERO) < 0);
		case ODD_QUESTION: return Bool.tf(((Number)arg.first()).longValue() % 2 != 0);
		case EVEN_QUESTION: return Bool.tf(((Number)arg.first()).longValue() % 2 == 0);
		case MAX: return computeNumbers_(arg, 'X', (Number)arg.first());
		case MIN: return computeNumbers_(arg, 'N', (Number)arg.first());
		case ADD: return computeNumbers_(arg, '+', (Number)arg.first());
		case MUL: return computeNumbers_(arg, '*', (Number)arg.first());
		case SUB: return computeNumbers_(arg, '-', (Number)arg.first());
		case ABS: return ((Number)arg.first()).monOp('A');
		case QUOTIENT: return ((Number)arg.first()).binOp('Q', (Number)arg.second());
		case REMAINDER: return ((Number)arg.first()).binOp('R', (Number)arg.second());
		case MODULO: return ((Number)arg.first()).binOp('O', (Number)arg.second());
		case SQRT: return(((Number)arg.first()).monOp('R'));
			// booleans
		case BOOLEAN_QUESTION: return Bool.tf(arg.first() instanceof Bool);
		case NOT: return Bool.tf(arg.first() == Bool.FALSE);
			// pairs & lists
		case PAIR_QUESTION: return Bool.tf(arg.first() instanceof Pair);
		case CONS: return new Pair(arg.first(), arg.second());
		case CAR: return arg.first().first();
		case CDR: return arg.first().rest();
		case SET_CAR_EXCLAMATION: return set_cxr_exclamation_(arg, true);
		case SET_CDR_EXCLAMATION: return set_cxr_exclamation_(arg, false);
		case CAAAAR: return cxr_(arg, 4, 0xF);
		case CAAADR: return cxr_(arg, 4, 0xE);
		case CAAAR: return cxr_(arg, 3, 0x7);
		case CAADAR: return cxr_(arg, 4, 0xD);
		case CAADDR: return cxr_(arg, 4, 0xC);
		case CAADR: return cxr_(arg, 3, 0x6);
		case CAAR: return cxr_(arg, 2, 0x3);
		case CADAAR: return cxr_(arg, 4, 0xB);
		case CADADR: return cxr_(arg, 4, 0xA);
		case CADAR: return cxr_(arg, 3, 0x5);
		case CADDAR: return cxr_(arg, 4, 0x9);
		case CADDDR: return cxr_(arg, 4, 0x8);
		case CADDR: return cxr_(arg, 3, 0x4);
		case CADR: return cxr_(arg, 2, 0x2);
		case CDAAAR: return cxr_(arg, 4, 0x7);
		case CDAADR: return cxr_(arg, 4, 0x6);
		case CDAAR: return cxr_(arg, 3, 0x3);
		case CDADAR: return cxr_(arg, 4, 0x5);
		case CDADDR: return cxr_(arg, 4, 0x4);
		case CDADR: return cxr_(arg, 3, 0x2);
		case CDAR: return cxr_(arg, 2, 0x1);
		case CDDAAR: return cxr_(arg, 4, 0x3);
		case CDDADR: return cxr_(arg, 4, 0x2);
		case CDDAR: return cxr_(arg, 3, 0x1);
		case CDDDAR: return cxr_(arg, 4, 0x1);
		case CDDDDR: return cxr_(arg, 4, 0x0);
		case CDDDR: return cxr_(arg, 3, 0x0);
		case CDDR: return cxr_(arg, 2, 0x0);
		case NULL_QUESTION: return Bool.tf(arg.first() == Nil.NIL);
		case LIST_QUESTION: return Bool.tf(arg.first() instanceof List && ((List)arg.first()).isList());
		case LIST: return (arg instanceof Pair ? arg : null);
		case LENGTH: return new Number(((Pair)arg.first()).length());
		case APPEND: return append_(arg);
		case REVERSE: return reverse_(arg);
		case LIST_TAIL: return list_tailRef_(arg.first(), arg.second(), true);
		case LIST_REF: return list_tailRef_(arg.first(), arg.second(), false);
		case MEMQ: return memberAssoc_(arg.first(), arg.second(), true, 'Q');
		case MEMV: return memberAssoc_(arg.first(), arg.second(), true, 'V');
		case MEMBER: return memberAssoc_(arg.first(), arg.second(), true, ' ');
		case ASSQ: return memberAssoc_(arg.first(), arg.second(), false, 'Q');
		case ASSV: return memberAssoc_(arg.first(), arg.second(), false, 'V');
		case ASSOC: return memberAssoc_(arg.first(), arg.second(), false, ' ');
			// symbols
		case SYMBOL_QUESTION: return Bool.tf(arg.first() instanceof Symbol);
			// control
		case PROCEDURE_QUESTION: return Bool.tf(arg.first() instanceof Procedure);
		case APPLY: return(apply_(ip, arg, env));
		case MAP: return(map_(ip, arg.first(), arg.rest(), env));
		case FOR_EACH: return(for_each_(ip, arg.first(), arg.rest(), env));
		case EVAL: return(ip.eval(ip.eval(arg.first(), env), env));
		}
		throw new InterpreterException(Error.UNIMPLEMENTED_FEATURE, toString() + "#" + index);
	}

	private Expression append_(Expression arg)
	throws InterpreterException {
		Expression retVal = Nil.NIL;
		for(Expression cur = arg; cur instanceof Pair; cur = cur.rest()) retVal = retVal.append(cur.first());
		return retVal;
	}

	private Expression reverse_(Expression arg)
	throws InterpreterException {
        Pair arg1 = (Pair)arg.first();
		Expression retVal = Nil.NIL;
		while(arg1 instanceof Pair) {
			retVal = arg1.first().cons(retVal);
			arg1 = (Pair)arg1.rest();
		}
		return retVal;
	}

	private Expression cxr_(Expression arg, int length, int code)
	throws InterpreterException {
		Expression retVal = arg.first();
		int cp = 1;
		for(int i = 0; i < length; i++) {
			retVal = ((code & cp) != 0) ? retVal.first() : retVal.rest();
			cp <<= 1;
		}
		return retVal;
	}

	private Expression list_tailRef_(Expression list, Expression k, boolean tail)
	throws InterpreterException {
		long n = ((Number)k).longValue();
		if(n < 0 || n >= ((Pair)list).length()) throw new InterpreterException(Error.OUT_OF_RANGE, k);
		Expression cur = list;
		for(long l = n; l > 0; l--) cur = cur.rest();
		return (tail) ? cur : cur.first();
	}

	private Expression memberAssoc_(Expression obj, Expression list, boolean member, char comp)
	throws InterpreterException {
		for(Expression cur = list; cur instanceof Pair; cur = cur.rest()) {
			Expression target = (member) ? cur.first() : cur.first().first();
			boolean found = false;
			switch(comp) {
				case 'Q': found = obj.isEq(target); break;
				case 'V': found = obj.isEqv(target); break;
				default: found = obj.isEqual(target); break;
			}
			if(found) return (member) ? cur : cur.first();
		}
		return Bool.FALSE;
	}

	private Expression set_cxr_exclamation_(Expression arg, boolean f)
	throws InterpreterException {
		if(!(arg.first() instanceof Pair)) throw new InterpreterException(Error.PAIR_EXPECTED, arg);
		if(f) ((Pair)arg.first()).rplca(arg.second());
		else ((Pair)arg.first()).rplcd(arg.second());
		return null;
	}

	private Expression compareNumbers_(Expression arg, char comp)
	throws InterpreterException {
		for(Expression cur = arg; cur.rest() instanceof Pair; cur = cur.rest()) {
			Number x = (Number)cur.first();
			Number y = (Number)cur.second();
			switch(comp) {
				case '>': if(x.compareTo(y) <= 0) return Bool.FALSE; break;
				case '<': if(x.compareTo(y) >= 0) return Bool.FALSE; break;
				case 'G': if(x.compareTo(y) < 0) return Bool.FALSE; break;
				case 'L': if(x.compareTo(y) > 0) return Bool.FALSE; break;
				default: if(x.compareTo(y) != 0) return Bool.FALSE; break;
			}
		}
		return Bool.TRUE;
	}

	private Expression computeNumbers_(Expression arg, char op, Number seed)
	throws InterpreterException {
		if(arg instanceof Pair) {
			if(!(arg.rest() instanceof Pair)) {
				Number x = (Number)arg.first();
				switch(op) {
					case '-': return Number.ZERO.binOp(op, seed);
					case '/': return Number.ONE.binOp(op, seed);
				}
				return x;
			} else {
				for(Expression cur = arg.rest(); cur instanceof Pair; cur = cur.rest()) {
					Number x = (Number)cur.first();
					seed = seed.binOp(op, x);
				}
			}
		}
		return seed;
	}

	private Expression apply_(Interpreter ip, Expression arg, Environment env) throws InterpreterException {
		Expression retVal = new Pair(arg.first());
		for (Expression cur = arg.rest(); cur instanceof Pair; cur = cur.rest()) {
			Expression z = cur.first();
			if (cur.rest() instanceof Pair) z = new Pair(z);
			retVal = retVal.append(z);
		}
		return(ip.eval(retVal, env));
	}
	
	private Expression map_(Interpreter ip, Expression op, Expression arg, Environment env) throws InterpreterException {
		if (!(arg.first() instanceof Pair)) return(Nil.NIL);
		return(ip.eval(op.cons(map_1(arg)), env).cons(map_(ip, op, map_2(arg), env)));
	}
	
	private Expression map_1(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(Symbol.QUOTE.cons(arg.first().first().cons(Nil.NIL)).cons(map_1(arg.rest())));
	}
	
	private Expression map_2(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(arg.first().rest().cons(map_2(arg.rest())));
	}

	private Expression for_each_(Interpreter ip, Expression op, Expression arg, Environment env) throws InterpreterException {
		if (!(arg.first() instanceof Pair)) return(null);
		ip.eval(op.cons(for_each_1(arg)), env);
		return(for_each_(ip, op, for_each_2(arg), env));
	}
	
	private Expression for_each_1(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(Symbol.QUOTE.cons(arg.first().first().cons(Nil.NIL)).cons(for_each_1(arg.rest())));
	}
	
	private Expression for_each_2(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(arg.first().rest().cons(for_each_2(arg.rest())));
	}
	
}