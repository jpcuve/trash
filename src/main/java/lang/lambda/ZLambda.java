package lang.lambda;

public class ZLambda extends ZPackage {
	
	public static final int // equivalence
		EQV_QUESTION = 1, EQ_QUESTION = 2, EQUAL_QUESTION = 3;
	
	public static final int // numbers
		NUMBER_QUESTION = 4, COMPLEX_QUESTION = 5, REAL_QUESTION = 6,
		RATIONAL_QUESTION = 7, INTEGER_QUESTION = 8, EXACT_QUESTION = 9, INEXACT_QUESTION = 10, EQUAL_TO = 11, LESS_THAN = 12,
		MORE_THAN = 13, LESS_OR_EQUAL_TO = 14, MORE_OR_EQUAL_TO = 15, ZERO_QUESTION = 16, POSITIVE_QUESTION = 17, NEGATIVE_QUESTION = 18,
		ODD_QUESTION = 19, EVEN_QUESTION = 20, MAX = 21, MIN = 22, ADD = 23, SUB = 24, MUL = 25, DIV = 26, ABS = 27, 
		QUOTIENT = 28, REMAINDER = 29, MODULO = 30, GCD = 31, LCM = 32, NUMERATOR = 33, DENOMINATOR = 34, FLOOR = 35,
		CEILING = 36, TRUNCATE = 37, ROUND = 38, RATIONALIZE = 39, EXP = 40, LOG = 41, SIN = 42, COS = 43, TAN = 44, ASIN = 45,
		ACOS = 46, ATAN = 47, SQRT = 48, EXPT = 49, MAKE_RECTANGULAR = 50, MAKE_POLAR = 51, REAL_PART = 52,	IMAG_PART = 53,
		MAGNITUDE = 54,	ANGLE = 55,	EXACT_TO_INEXACT = 56, INEXACT_TO_EXACT = 57, NUMBER_TO_STRING = 58, STRING_TO_NUMBER = 59;

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

	
	public ZLambda(){
		int n = Integer.MAX_VALUE;
		this.addFunction(ABS, "abs", 1);
		this.addFunction(ACOS, "acos", 1);
		this.addFunction(ADD, "+", 0, n);
		this.addFunction(ANGLE, "angle", 1);
		this.addFunction(APPEND, "append");
		this.addFunction(ASIN, "asin", 1);
		this.addFunction(ASSOC, "assoc", 2);
		this.addFunction(ASSQ, "assq", 2);
		this.addFunction(ASSV, "assv", 2);
		this.addFunction(ATAN, "atan", 1);
		this.addFunction(BOOLEAN_QUESTION, "boolean?", 1);
		this.addFunction(CONS, "cons", 2, 2);
		this.addFunction(CAAAAR, "caaaar", 1);
		this.addFunction(CAAADR, "caaadr", 1);
		this.addFunction(CAAAR, "caaar", 1);
		this.addFunction(CAADAR, "caadar", 1);
		this.addFunction(CAADDR, "caaddr", 1);
		this.addFunction(CAADR, "caadr", 1);
		this.addFunction(CAAR, "caar", 1);
		this.addFunction(CADAAR, "cadaar", 1);
		this.addFunction(CADADR, "cadadr", 1);
		this.addFunction(CADAR, "cadar", 1);
		this.addFunction(CADDAR, "caddar", 1);
		this.addFunction(CADDDR, "cadddr", 1);
		this.addFunction(CADDR, "caddr", 1);
		this.addFunction(CADR, "cadr", 1);
		this.addFunction(CAR, "car", 1);
		this.addFunction(CDAAAR, "cdaaar", 1);
		this.addFunction(CDAADR, "cdaadr", 1);
		this.addFunction(CDAAR, "cdaar", 1);
		this.addFunction(CDADAR, "cdadar", 1);
		this.addFunction(CDADDR, "cdaddr", 1);
		this.addFunction(CDADR, "cdadr", 1);
		this.addFunction(CDAR, "cdar", 1);
		this.addFunction(CDDAAR, "cddaar",1);
		this.addFunction(CDDADR, "cddadr", 1);
		this.addFunction(CDDAR, "cddar", 1);
		this.addFunction(CDDDAR, "cdddar", 1);
		this.addFunction(CDDDDR, "cddddr", 1);
		this.addFunction(CDDDR, "cdddr", 1);
		this.addFunction(CDDR, "cddr", 1);
		this.addFunction(CDR, "cdr", 1);
		this.addFunction(CEILING, "ceiling", 1);
		this.addFunction(COMPLEX_QUESTION, "complex?", 1);
		this.addFunction(COS, "cos", 1);
		this.addFunction(DENOMINATOR, "denominator", 1);
		this.addFunction(DIV, "/", 1, n);
		this.addFunction(EQ_QUESTION, "eq?", 2);
		this.addFunction(EQUAL_QUESTION, "equal?", 2);
		this.addFunction(EQUAL_TO, "=", 2, n);
		this.addFunction(EQV_QUESTION, "eqv?", 2);
		this.addFunction(EVEN_QUESTION, "even?", 1);
		this.addFunction(EXACT_QUESTION, "exact?", 1);
		this.addFunction(EXACT_TO_INEXACT, "exact->inexact", 1);
		this.addFunction(EXP, "exp", 1);
		this.addFunction(EXPT, "expt", 1);
		this.addFunction(FLOOR, "floor", 1);
		this.addFunction(GCD, "gcd", 2);
		this.addFunction(IMAG_PART, "imag-part", 1);
		this.addFunction(INEXACT_QUESTION, "inexact?", 1);
		this.addFunction(INEXACT_TO_EXACT, "inexact->exact", 1);
		this.addFunction(INTEGER_QUESTION, "integer?", 1);
		this.addFunction(LCM, "lcm", 2);
		this.addFunction(LENGTH, "length", 1);
		this.addFunction(LESS_OR_EQUAL_TO, "<=", 2, n);
		this.addFunction(LESS_THAN, "<", 2, n);
		this.addFunction(LIST, "list");
		this.addFunction(LIST_QUESTION, "list?", 1);
		this.addFunction(LIST_TAIL, "list-tail", 2);
		this.addFunction(LIST_REF, "list-ref", 2);
		this.addFunction(LOG, "log", 1);
		this.addFunction(MAGNITUDE, "magnitude", 1);
		this.addFunction(MAKE_RECTANGULAR, "make-rectangular", 1);
		this.addFunction(MAKE_POLAR, "make-polar", 1);
		this.addFunction(MAX, "max", 2, n);
		this.addFunction(MEMBER, "member", 2);
		this.addFunction(MEMQ, "memq", 2);
		this.addFunction(MEMV, "memv", 2);
		this.addFunction(MIN, "min", 2, n);
		this.addFunction(MODULO, "modulo", 2);
		this.addFunction(MORE_OR_EQUAL_TO, ">=", 2, n);
		this.addFunction(MORE_THAN, ">", 2, n);
		this.addFunction(MUL, "*", 0, n);
		this.addFunction(NEGATIVE_QUESTION, "negative?", 1);
		this.addFunction(NOT, "not", 1);
		this.addFunction(NUMBER_QUESTION, "number?", 1);
		this.addFunction(NUMBER_TO_STRING, "number->string", 1);
		this.addFunction(NUMERATOR, "numerator", 1);
		this.addFunction(NULL_QUESTION, "null?" , 1);
		this.addFunction(ODD_QUESTION, "odd?", 1);
		this.addFunction(PAIR_QUESTION, "pair?", 1);
		this.addFunction(POSITIVE_QUESTION, "positive?", 1);
		this.addFunction(QUOTIENT, "quotient", 2);
		this.addFunction(RATIONAL_QUESTION, "rational?", 1);
		this.addFunction(RATIONALIZE, "rationalize", 1);
		this.addFunction(REAL_QUESTION, "real?", 1);
		this.addFunction(REAL_PART, "real-part", 1);
		this.addFunction(REMAINDER, "remainder", 2);
		this.addFunction(REVERSE, "reverse", 1);
		this.addFunction(ROUND, "round", 1);
		this.addFunction(SQRT, "sqrt", 1);
		this.addFunction(SET_CAR_EXCLAMATION, "set-car!", 2);
		this.addFunction(SET_CDR_EXCLAMATION, "set-cdr!", 2);
		this.addFunction(SIN, "sin", 1);
		this.addFunction(STRING_TO_NUMBER, "string->number", 1);
		this.addFunction(SUB, "-", 1, n);
		this.addFunction(TAN, "tan", 1);
		this.addFunction(TRUNCATE, "truncate", 1);
		this.addFunction(ZERO_QUESTION, "zero?", 1);
	}
	
	public ZExpression f(int index, ZExpression arg) throws ZException {
		switch(index){
			// equivalence
		case EQV_QUESTION:			return ZBoolean.tf(arg.first().isEqv(arg.second()));
		case EQ_QUESTION:			return ZBoolean.tf(arg.first().isEq(arg.second()));
		case EQUAL_QUESTION:		return ZBoolean.tf(arg.first().isEqual(arg.second()));
			// numbers
		case NUMBER_QUESTION:		return ZBoolean.tf(arg.first().isNumber());
		case COMPLEX_QUESTION:		return ZBoolean.tf(arg.first().isNumber() && arg.first().toNumber().isComplex());
		case REAL_QUESTION:			return ZBoolean.tf(arg.first().isNumber() && arg.first().toNumber().isReal());
		case RATIONAL_QUESTION:		return ZBoolean.tf(arg.first().isNumber() && arg.first().toNumber().isRational());
		case INTEGER_QUESTION:		return ZBoolean.tf(arg.first().isNumber() && arg.first().toNumber().isInteger());
		case EQUAL_TO:				return this.compareNumbers_(arg, '=');
		case LESS_THAN:				return this.compareNumbers_(arg, '<');
		case MORE_THAN:				return this.compareNumbers_(arg, '>');
		case LESS_OR_EQUAL_TO:		return this.compareNumbers_(arg, 'L');
		case MORE_OR_EQUAL_TO:		return this.compareNumbers_(arg, 'G');
		case ZERO_QUESTION:			return ZBoolean.tf(arg.first().toNumber().compareTo(ZNumber.ZERO) == 0);
		case POSITIVE_QUESTION:		return ZBoolean.tf(arg.first().toNumber().compareTo(ZNumber.ZERO) > 0);
		case NEGATIVE_QUESTION:		return ZBoolean.tf(arg.first().toNumber().compareTo(ZNumber.ZERO) < 0);
		case ODD_QUESTION:			return ZBoolean.tf(arg.first().toNumber().toInteger().toNatural() % 2 != 0);
		case EVEN_QUESTION:			return ZBoolean.tf(arg.first().toNumber().toInteger().toNatural() % 2 == 0);
		case MAX:					return this.computeNumbers_(arg, 'X', arg.first().toNumber());
		case MIN:					return this.computeNumbers_(arg, 'N', arg.first().toNumber());
		case ADD:					return this.computeNumbers_(arg, '+', arg.first().toNumber());
		case MUL:					return this.computeNumbers_(arg, '*', arg.first().toNumber());
		case SUB:					return this.computeNumbers_(arg, '-', arg.first().toNumber());
		case DIV:					return this.computeNumbers_(arg, '/', arg.first().toNumber());
		case ABS:					return arg.first().toNumber().monOp('A');
		case QUOTIENT:				return arg.first().toNumber().binOp('Q', arg.second().toNumber());
		case REMAINDER:				return arg.first().toNumber().binOp('R', arg.second().toNumber());
		case MODULO:				return arg.first().toNumber().binOp('O', arg.second().toNumber());
			// booleans
		case BOOLEAN_QUESTION:		return ZBoolean.tf(arg.first().isBoolean());
		case NOT:					return ZBoolean.tf(arg.first() == ZBoolean.FALSE);
			// pairs & lists
		case PAIR_QUESTION:			return ZBoolean.tf(arg.first().isPair());
		case CONS:					return new ZPair(arg.first(), arg.second());
		case CAR:					return arg.first().first();
		case CDR:					return arg.first().rest();
		case SET_CAR_EXCLAMATION:	return this.set_cxr_exclamation_(arg, true);
		case SET_CDR_EXCLAMATION:	return this.set_cxr_exclamation_(arg, false);
		case CAAAAR:				return this.cxr_(arg, 4, 0xF);
		case CAAADR:				return this.cxr_(arg, 4, 0xE);
		case CAAAR:					return this.cxr_(arg, 3, 0x7);
		case CAADAR:				return this.cxr_(arg, 4, 0xD);
		case CAADDR:				return this.cxr_(arg, 4, 0xC);
		case CAADR:					return this.cxr_(arg, 3, 0x6);
		case CAAR:					return this.cxr_(arg, 2, 0x3);
		case CADAAR:				return this.cxr_(arg, 4, 0xB);
		case CADADR:				return this.cxr_(arg, 4, 0xA);
		case CADAR:					return this.cxr_(arg, 3, 0x5);
		case CADDAR:				return this.cxr_(arg, 4, 0x9);
		case CADDDR:				return this.cxr_(arg, 4, 0x8);
		case CADDR:					return this.cxr_(arg, 3, 0x4);
		case CADR:					return this.cxr_(arg, 2, 0x2);
		case CDAAAR:				return this.cxr_(arg, 4, 0x7);
		case CDAADR:				return this.cxr_(arg, 4, 0x6);
		case CDAAR:					return this.cxr_(arg, 3, 0x3);
		case CDADAR:				return this.cxr_(arg, 4, 0x5);
		case CDADDR:				return this.cxr_(arg, 4, 0x4);
		case CDADR:					return this.cxr_(arg, 3, 0x2);
		case CDAR:					return this.cxr_(arg, 2, 0x1);
		case CDDAAR:				return this.cxr_(arg, 4, 0x3);
		case CDDADR:				return this.cxr_(arg, 4, 0x2);
		case CDDAR:					return this.cxr_(arg, 3, 0x1);
		case CDDDAR:				return this.cxr_(arg, 4, 0x1);
		case CDDDDR:				return this.cxr_(arg, 4, 0x0);
		case CDDDR:					return this.cxr_(arg, 3, 0x0);
		case CDDR:					return this.cxr_(arg, 2, 0x0);
		case NULL_QUESTION:			return ZBoolean.tf(arg.first() == ZEmpty.EMPTY);
		case LIST_QUESTION:			return ZBoolean.tf(arg.first().isList());
		case LIST:					return (arg.isPair() ? arg : ZEmpty.EMPTY);
		case LENGTH:				return new ZInteger(arg.first().toList().length());
		case APPEND:				return this.append_(arg);
		case REVERSE:				return this.reverse_(arg);
		case LIST_TAIL:				return this.list_tailRef_(arg.first(), arg.second(), true);
		case LIST_REF:				return this.list_tailRef_(arg.first(), arg.second(), false);
		case MEMQ:					return this.memberAssoc_(arg.first(), arg.second(), true, 'Q');
		case MEMV:					return this.memberAssoc_(arg.first(), arg.second(), true, 'V');
		case MEMBER:				return this.memberAssoc_(arg.first(), arg.second(), true, ' ');
		case ASSQ:					return this.memberAssoc_(arg.first(), arg.second(), false, 'Q');
		case ASSV:					return this.memberAssoc_(arg.first(), arg.second(), false, 'V');
		case ASSOC:					return this.memberAssoc_(arg.first(), arg.second(), false, ' ');
		}
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, arg);
	}

	private ZExpression append_(ZExpression arg) throws ZException {
		ZExpression retVal = ZEmpty.EMPTY;
		for(ZExpression cur = arg; cur.isPair(); cur = cur.rest()) retVal = retVal.append(cur.first());
		return retVal;
	}
	
	private ZExpression reverse_(ZExpression arg) throws ZException {
		ZExpression arg1 = arg.first();
		if(!arg1.isList()) throw new ZException(ZError.LIST_EXPECTED, arg1);
		ZExpression retVal = ZEmpty.EMPTY;
		while(arg1.isPair()){
			retVal = arg1.first().cons(retVal);
			arg1 = arg1.rest();
		}
		return retVal;
	}

	private ZExpression cxr_(ZExpression arg, int length, int code) throws ZException {
		ZExpression retVal = arg.first();
		int cp = 1;
		for(int i = 0; i < length; i++){
			retVal = ((code & cp) != 0) ? retVal.first() : retVal.rest();
			cp <<= 1;
		}
		return retVal;
	}
	
	private ZExpression list_tailRef_(ZExpression list, ZExpression k, boolean tail) throws ZException {
		long n = k.toNumber().toInteger().toNatural();
		if(n < 0 || n >= list.toList().length()) throw new ZException(ZError.OUT_OF_RANGE, k);
		ZExpression cur = list;
		for(long l = n; l > 0; l--) cur = cur.rest();
		return (tail) ? cur : cur.first();
	}
	
	private ZExpression memberAssoc_(ZExpression obj, ZExpression list, boolean member, char comp) throws ZException {
		for(ZExpression cur = list; cur.isPair(); cur = cur.rest()){
			ZExpression target = (member) ? cur.first() : cur.first().first();
			boolean found = false;
			switch(comp){
			case 'Q': found = obj.isEq(target); break;
			case 'V': found = obj.isEqv(target); break;
			default: found = obj.isEqual(target); break;
			}
			if(found) return (member) ? cur : cur.first();
		}
		return ZBoolean.FALSE;
	}
	
	private ZExpression set_cxr_exclamation_(ZExpression arg, boolean f) throws ZException {
		if(!arg.first().isPair()) throw new ZException(ZError.PAIR_EXPECTED, arg);
		if(f) ((ZPair)arg.first()).rplca(arg.second());
		else ((ZPair)arg.first()).rplcd(arg.second());
		return ZEmpty.VOID;
	}
	
	private ZExpression compareNumbers_(ZExpression arg, char comp) throws ZException {
		for(ZExpression cur = arg; cur.rest().isPair(); cur = cur.rest()){
			ZNumber x = cur.first().toNumber();
			ZNumber y = cur.second().toNumber();
			switch(comp){
			case '>': if(x.compareTo(y) <= 0) return ZBoolean.FALSE; break;
			case '<': if(x.compareTo(y) >= 0) return ZBoolean.FALSE; break;
			case 'G': if(x.compareTo(y) < 0) return ZBoolean.FALSE; break;
			case 'L': if(x.compareTo(y) > 0) return ZBoolean.FALSE; break;
			default: if(x.compareTo(y) != 0) return ZBoolean.FALSE; break;
			}
		}
		return ZBoolean.TRUE;
	}
	
	private ZExpression computeNumbers_(ZExpression arg, char op, ZNumber seed) throws ZException {
		if(arg.isPair()){
			if(!arg.rest().isPair()){
				ZNumber x = arg.first().toNumber();
				switch(op){
				case '-': return ZNumber.ZERO.binOp(op, seed);
				case '/': return ZNumber.ONE.binOp(op, seed);
				}
				return x;
			}else{
				for(ZExpression cur = arg.rest(); cur.isPair(); cur = cur.rest()){
					ZNumber x = cur.first().toNumber();
					seed = seed.binOp(op, x);
				}
			}	
		}
		return seed;
	}

}
