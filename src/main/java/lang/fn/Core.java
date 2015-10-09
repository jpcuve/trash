package lang.fn;

public class Core extends Pack {
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

	public static final int // symbols
		SYMBOL_QUESTION = 110, SYMBOL_TO_STRING = 111, STRING_TO_SYMBOL = 112;

	public static final int // characters
		CHAR_QUESTION = 113, CHAR_EQUAL_TO = 114, CHAR_LESS_THAN = 115, CHAR_MORE_THAN = 116, CHAR_LESS_OR_EQUAL_TO = 117, CHAR_MORE_OR_EQUAL_TO = 118,
		CHAR_CI_EQUAL_TO = 119, CHAR_CI_LESS_THAN = 120, CHAR_CI_MORE_THAN = 121, CHAR_CI_LESS_OR_EQUAL_TO = 122, CHAR_CI_MORE_OR_EQUAL_TO = 123,
		CHAR_ALPHABETIC_QUESTION = 124, CHAR_NUMERIC_QUESTION = 125, CHAR_WHITESPACE_QUESTION = 126, CHAR_UPPER_CASE_QUESTION = 127, CHAR_LOWER_CASE_QUESTION = 128,
		CHAR_TO_INTEGER = 129, INTEGER_TO_CHAR = 130, CHAR_UPCASE = 131, CHAR_DOWNCASE = 132;

	public static final int // strings
		STRING_QUESTION = 133, MAKE_STRING = 134, STRING = 135, STRING_LENGTH = 136, STRING_REF = 137, STRING_SET_EXCLAMATION = 138,
		STRING_EQUAL_TO = 139, STRING_LESS_THAN = 140, STRING_MORE_THAN = 141, STRING_LESS_OR_EQUAL_TO = 142, STRING_MORE_OR_EQUAL_TO = 143,
		STRING_CI_EQUAL_TO = 144, STRING_CI_LESS_THAN = 145, STRING_CI_MORE_THAN = 146, STRING_CI_LESS_OR_EQUAL_TO = 147, STRING_CI_MORE_OR_EQUAL_TO = 148,
		SUBSTRING = 149, STRING_APPEND = 150, STRING_TO_LIST = 151, LIST_TO_STRING = 152, STRING_COPY = 153, STRING_FILL_EXCLAMATION = 154;

	public static final int // vector
		VECTOR_QUESTION = 155, MAKE_VECTOR = 156, VECTOR = 157, VECTOR_LENGTH = 158, VECTOR_REF = 159, VECTOR_SET_EXCLAMATION = 160,
		VECTOR_TO_LIST = 161, LIST_TO_VECTOR = 162, VECTOR_FILL_EXCLAMATION = 163;

	public static final int // control
		PROCEDURE_QUESTION = 164, APPLY = 165, MAP = 166, FOR_EACH = 167, FORCE = 168, CALL_WITH_CURRENT_CONTINUATION = 169,
		VALUES = 170, CALL_WITH_VALUES = 171, DYNAMIC_WIND = 172, EVAL = 173, SCHEME_REPORT_ENVIRONMENT = 174,
		NULL_ENVIRONMENT = 175, INTERACTION_ENVIRONMENT = 176;
		
	public static final int // io
		CALL_WITH_INPUT_FILE = 177, CALL_WITH_OUTPUT_FILE = 178, INPUT_PORT_QUESTION = 179, OUTPUT_PORT_QUESTION = 180,
		CURRENT_INPUT_PORT = 181, CURRENT_OUTPUT_PORT = 182, WITH_INPUT_FROM_FILE = 183, WITH_OUTPUT_TO_FILE = 184,
		OPEN_INPUT_FILE = 185, OPEN_OUTPUT_FILE = 186, CLOSE_INPUT_PORT = 187, CLOSE_OUTPUT_PORT = 188,
		READ = 189, READ_CHAR = 190, PEEK_CHAR = 191, EOF_OBJECT_QUESTION = 192, CHAR_READY_QUESTION = 193,
		WRITE = 194, DISPLAY = 195, NEWLINE = 196, WRITE_CHAR = 197, LOAD = 198, TRANSCRIPT_ON = 199, TRANSCRIPT_OFF = 200;		

	public Core() {
		int n = Integer.MAX_VALUE;
		addFunction(ABS, "abs", 1);
		addFunction(ACOS, "acos", 1);
		addFunction(ADD, "+", 0, n);
		addFunction(ANGLE, "angle", 1);
		addFunction(APPEND, "append");
		addFunction(APPLY, "apply");
		addFunction(ASIN, "asin", 1);
		addFunction(ASSOC, "assoc", 2);
		addFunction(ASSQ, "assq", 2);
		addFunction(ASSV, "assv", 2);
		addFunction(ATAN, "atan", 1);
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
		addFunction(CEILING, "ceiling", 1);
		addFunction(CHAR_QUESTION, "char?", 1);
		addFunction(CHAR_EQUAL_TO, "char=?", 2);
		addFunction(CHAR_LESS_THAN, "char<?", 2);
		addFunction(CHAR_MORE_THAN, "char>?", 2);
		addFunction(CHAR_LESS_OR_EQUAL_TO, "char<=?", 2);
		addFunction(CHAR_MORE_OR_EQUAL_TO, "char>=?", 2);
		addFunction(CHAR_CI_EQUAL_TO, "char-ci=?", 2);
		addFunction(CHAR_CI_LESS_THAN, "char-ci<?", 2);
		addFunction(CHAR_CI_MORE_THAN, "char-ci>?", 2);
		addFunction(CHAR_CI_LESS_OR_EQUAL_TO, "char-ci<=?", 2);
		addFunction(CHAR_CI_MORE_OR_EQUAL_TO, "char-ci>=?", 2);
		addFunction(CHAR_ALPHABETIC_QUESTION, "char-alphabetic?", 1);
		addFunction(CHAR_NUMERIC_QUESTION, "char-numeric?", 1);
		addFunction(CHAR_WHITESPACE_QUESTION, "char-whitespace?", 1);
		addFunction(CHAR_UPPER_CASE_QUESTION, "char-upper-case?", 1);
		addFunction(CHAR_LOWER_CASE_QUESTION, "char-lower-case?", 1);
		addFunction(CHAR_TO_INTEGER, "char->integer", 1);
		addFunction(INTEGER_TO_CHAR, "integer->char", 1);
		addFunction(CHAR_UPCASE, "char-upcase", 1);
		addFunction(CHAR_DOWNCASE, "char-downcase", 1);
		addFunction(CLOSE_INPUT_PORT, "flush-input-port", 1);
		addFunction(CLOSE_OUTPUT_PORT, "flush-output-port", 1);
		addFunction(COMPLEX_QUESTION, "complex?", 1);
		addFunction(COS, "cos", 1);
		addFunction(DENOMINATOR, "denominator", 1);
		addFunction(DISPLAY, "display", 1, 2);
		addFunction(DIV, "/", 1, n);
		addFunction(EQ_QUESTION, "eq?", 2);
		addFunction(EQUAL_QUESTION, "equal?", 2);
		addFunction(EQUAL_TO, "=", 2, n);
		addFunction(EQV_QUESTION, "eqv?", 2);
		addFunction(EVAL, "eval", 1);
		addFunction(EVEN_QUESTION, "even?", 1);
		addFunction(EXACT_QUESTION, "exact?", 1);
		addFunction(EXACT_TO_INEXACT, "exact->inexact", 1);
		addFunction(EXP, "exp", 1);
		addFunction(EXPT, "expt", 1);
		addFunction(FLOOR, "floor", 1);
		addFunction(FOR_EACH, "for-each", 2,n);
		addFunction(FORCE, "force", 1, 1);
		addFunction(GCD, "gcd", 2);
		addFunction(IMAG_PART, "imag-part", 1);
		addFunction(INEXACT_QUESTION, "inexact?", 1);
		addFunction(INEXACT_TO_EXACT, "inexact->exact", 1);
		addFunction(INPUT_PORT_QUESTION, "input-port?", 1);
		addFunction(INTEGER_QUESTION, "integer?", 1);
		addFunction(LCM, "lcm", 2);
		addFunction(LENGTH, "length", 1);
		addFunction(LESS_OR_EQUAL_TO, "<=", 2, n);
		addFunction(LESS_THAN, "<", 2, n);
		addFunction(LIST, "list");
		addFunction(LIST_QUESTION, "list?", 1);
		addFunction(LIST_TAIL, "list-tail", 2);
		addFunction(LIST_TO_STRING, "list->string", 1);
		addFunction(LIST_TO_VECTOR, "list->vector", 1);
		addFunction(LIST_REF, "list-ref", 2);
		addFunction(LOG, "log", 1);
		addFunction(MAP, "map", 2, n);
		addFunction(MAGNITUDE, "magnitude", 1);
		addFunction(MAKE_RECTANGULAR, "make-rectangular", 1);
		addFunction(MAKE_POLAR, "make-polar", 1);
		addFunction(MAKE_STRING, "make-string", 1, 2);
		addFunction(MAKE_VECTOR, "make-vector", 1, 2);
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
		addFunction(NEWLINE, "newline", 0, 1);
		addFunction(NOT, "not", 1);
		addFunction(NUMBER_QUESTION, "number?", 1);
		addFunction(NUMBER_TO_STRING, "number->string", 1);
		addFunction(NUMERATOR, "numerator", 1);
		addFunction(NULL_QUESTION, "null?" , 1);
		addFunction(ODD_QUESTION, "odd?", 1);
		addFunction(OPEN_INPUT_FILE, "open-input-file", 1);
		addFunction(OPEN_OUTPUT_FILE, "open-output-file", 1);
		addFunction(OUTPUT_PORT_QUESTION, "output-port?", 1);
		addFunction(PAIR_QUESTION, "pair?", 1);
		addFunction(PEEK_CHAR, "peek-char", 0, 1);
		addFunction(POSITIVE_QUESTION, "positive?", 1);
		addFunction(PROCEDURE_QUESTION, "procedure?", 1);
		addFunction(QUOTIENT, "quotient", 2);
		addFunction(RATIONAL_QUESTION, "rational?", 1);
		addFunction(RATIONALIZE, "rationalize", 1);
		addFunction(READ_CHAR, "read-char", 0, 1);
		addFunction(REAL_QUESTION, "real?", 1);
		addFunction(REAL_PART, "real-part", 1);
		addFunction(REMAINDER, "remainder", 2);
		addFunction(REVERSE, "reverse", 1);
		addFunction(ROUND, "round", 1);
		addFunction(SQRT, "sqrt", 1);
		addFunction(SET_CAR_EXCLAMATION, "from-car!", 2);
		addFunction(SET_CDR_EXCLAMATION, "from-cdr!", 2);
		addFunction(SIN, "sin", 1);
		addFunction(STRING, "string", 1, n);
		addFunction(STRING_APPEND, "string-append", 1, n);
		addFunction(STRING_COPY, "string-copy", 1);
		addFunction(STRING_EQUAL_TO, "string=?", 2);
		addFunction(STRING_FILL_EXCLAMATION, "string-fill!", 2);
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
		addFunction(STRING_REF, "string-ref", 2);
		addFunction(STRING_SET_EXCLAMATION, "string-from!", 3);
		addFunction(STRING_TO_LIST, "string->list", 1);
		addFunction(STRING_TO_NUMBER, "string->number", 1);
		addFunction(STRING_TO_SYMBOL, "string->symbol", 1);
		addFunction(SYMBOL_QUESTION, "symbol?", 1);
		addFunction(SYMBOL_TO_STRING, "symbol->string", 1);
		addFunction(SUB, "-", 1, n);
		addFunction(SUBSTRING, "substring", 3);
		addFunction(TAN, "tan", 1);
		addFunction(TRUNCATE, "truncate", 1);
		addFunction(VECTOR, "vector");
		addFunction(VECTOR_FILL_EXCLAMATION, "vector-fill!", 2);
		addFunction(VECTOR_LENGTH, "vector-length", 1);
		addFunction(VECTOR_QUESTION, "vector?", 1);
		addFunction(VECTOR_REF, "vector-ref", 2);
		addFunction(VECTOR_SET_EXCLAMATION, "vector-from!", 3);
		addFunction(VECTOR_TO_LIST, "vector->list", 1);
		addFunction(WRITE, "write", 1, 2);
		addFunction(WRITE_CHAR, "write-char", 1, 2);
		addFunction(ZERO_QUESTION, "zero?", 1);
	}

	public Expression f(int index, Interpreter ip, Expression arg, Environment env) throws FnException {
		// System.out.println("arg=" + arg.write());
		switch(index) {
			// equivalence
		case EQV_QUESTION: return Bool.tf(arg.first().isEqv(arg.second()));
		case EQ_QUESTION: return Bool.tf(arg.first().isEq(arg.second()));
		case EQUAL_QUESTION: return Bool.tf(arg.first().isEqual(arg.second()));
		// numbers
		case NUMBER_QUESTION: return Bool.tf(arg.first().isNumber());
//		case COMPLEX_QUESTION: return Bool.tf(arg.first().isNumber() && arg.first().toNumber().isComplex());
		case REAL_QUESTION: return Bool.tf(arg.first().isNumber() && arg.first().toNumber().isReal());
//		case RATIONAL_QUESTION: return Bool.tf(arg.first().isNumber() && arg.first().toNumber().isRational());
		case INTEGER_QUESTION: return Bool.tf(arg.first().isNumber() && arg.first().toNumber().isInteger());
		case EQUAL_TO: return compareNumbers_(arg, '=');
		case LESS_THAN: return compareNumbers_(arg, '<');
		case MORE_THAN: return compareNumbers_(arg, '>');
		case LESS_OR_EQUAL_TO: return compareNumbers_(arg, 'L');
		case MORE_OR_EQUAL_TO: return compareNumbers_(arg, 'G');
		case ZERO_QUESTION: return Bool.tf(arg.first().toNumber().compareTo(Nb.ZERO) == 0);
		case POSITIVE_QUESTION: return Bool.tf(arg.first().toNumber().compareTo(Nb.ZERO) > 0);
		case NEGATIVE_QUESTION: return Bool.tf(arg.first().toNumber().compareTo(Nb.ZERO) < 0);
		case ODD_QUESTION: return Bool.tf(arg.first().toNumber().toInteger().toNatural() % 2 != 0);
		case EVEN_QUESTION: return Bool.tf(arg.first().toNumber().toInteger().toNatural() % 2 == 0);
		case MAX: return computeNumbers_(arg, 'X', arg.first().toNumber());
		case MIN: return computeNumbers_(arg, 'N', arg.first().toNumber());
		case ADD: return computeNumbers_(arg, '+', arg.first().toNumber());
		case MUL: return computeNumbers_(arg, '*', arg.first().toNumber());
		case SUB: return computeNumbers_(arg, '-', arg.first().toNumber());
		case DIV: return computeNumbers_(arg, '/', arg.first().toNumber());
		case ABS: return arg.first().toNumber().monOp('A');
		case QUOTIENT: return arg.first().toNumber().binOp('Q', arg.second().toNumber());
		case REMAINDER: return arg.first().toNumber().binOp('R', arg.second().toNumber());
		case MODULO: return arg.first().toNumber().binOp('O', arg.second().toNumber());
		case FLOOR: return(arg.first().toNumber().monOp('F'));
		case CEILING: return(arg.first().toNumber().monOp('G'));
		case TRUNCATE: return(arg.first().toNumber().monOp('U'));
		case ROUND: return(arg.first().toNumber().monOp('O'));
		case EXP: return(arg.first().toNumber().monOp('E'));
		case LOG: return(arg.first().toNumber().monOp('L'));
		case SIN: return(arg.first().toNumber().monOp('S'));
		case COS: return(arg.first().toNumber().monOp('C'));
		case TAN: return(arg.first().toNumber().monOp('T'));
		case ASIN: return(arg.first().toNumber().monOp('s'));
		case ACOS: return(arg.first().toNumber().monOp('c'));
		case ATAN: return(arg.first().toNumber().monOp('t'));
		case SQRT: return(arg.first().toNumber().monOp('R'));
			// booleans
		case BOOLEAN_QUESTION: return Bool.tf(arg.first().isBoolean());
		case NOT: return Bool.tf(arg.first() == Bool.FALSE);
			// pairs & lists
		case PAIR_QUESTION: return Bool.tf(arg.first().isPair());
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
		case NULL_QUESTION: return Bool.tf(arg.first() == Empty.EMPTY);
		case LIST_QUESTION: return Bool.tf(arg.first().isList());
		case LIST: return (arg.isPair() ? arg : Empty.EMPTY);
		case LENGTH: return new Int(arg.first().toList().length());
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
		case SYMBOL_QUESTION: return Bool.tf(arg.first().isSymbol());
		case SYMBOL_TO_STRING: return new Str(arg.first().toSymbol());
		case STRING_TO_SYMBOL: return Empty.EMPTY;
			// characters
		case CHAR_QUESTION: return Bool.tf(arg.first().isChar());
		case CHAR_EQUAL_TO: return compareChars_(arg, '=', false);
		case CHAR_LESS_THAN: return compareChars_(arg, '<', false);
		case CHAR_MORE_THAN: return compareChars_(arg, '>', false);
		case CHAR_LESS_OR_EQUAL_TO: return compareChars_(arg, 'L', false);
		case CHAR_MORE_OR_EQUAL_TO: return compareChars_(arg, 'G', false);
		case CHAR_CI_EQUAL_TO: return compareChars_(arg, '=', true);
		case CHAR_CI_LESS_THAN: return compareChars_(arg, '<', true);
		case CHAR_CI_MORE_THAN: return compareChars_(arg, '>', true);
		case CHAR_CI_LESS_OR_EQUAL_TO: return compareChars_(arg, 'L', true);
		case CHAR_CI_MORE_OR_EQUAL_TO: return compareChars_(arg, 'G', true);
		case CHAR_ALPHABETIC_QUESTION: return Bool.tf(arg.first().toChar().isAlphabetic());
		case CHAR_NUMERIC_QUESTION: return Bool.tf(arg.first().toChar().isNumeric());
		case CHAR_WHITESPACE_QUESTION: return Bool.tf(arg.first().toChar().isWhitespace());
		case CHAR_UPPER_CASE_QUESTION: return Bool.tf(arg.first().toChar().isUpperCase());
		case CHAR_LOWER_CASE_QUESTION: return Bool.tf(arg.first().toChar().isLowerCase());
		case CHAR_TO_INTEGER: return new Int(arg.first().toChar().charValue());
		case INTEGER_TO_CHAR: return new Char(arg.first().toNumber().toInteger().intValue());
		case CHAR_UPCASE: return Char.toUp(arg.first().toChar());
		case CHAR_DOWNCASE: return Char.toDown(arg.first().toChar());
			// strings
		case STRING_QUESTION: return Bool.tf(arg.first().isString());
		case MAKE_STRING: return new Str(arg.first().toNumber().toInteger(), arg);
		case STRING: return new Str(arg);
		case STRING_LENGTH: return arg.first().toString_().length();
		case STRING_REF: return arg.first().toString_().ref(arg.second().toNumber().toInteger());
		case STRING_SET_EXCLAMATION: return arg.first().toString_().set(arg.second().toNumber().toInteger(), arg.third().toChar());
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
		case SUBSTRING: return arg.first().toString_().subString(arg.second().toNumber().toInteger(), arg.third().toNumber().toInteger());
		case STRING_APPEND: return arg.first().toString_().append(arg.rest());
		case STRING_TO_LIST: return arg.first().toString_().toList_();
		case LIST_TO_STRING: return new Str(arg.first());
		case STRING_COPY: return new Str(arg.first().toString_());
		case STRING_FILL_EXCLAMATION: return arg.first().toString_().fill(arg.second().toChar());
			// vectors
		case VECTOR_QUESTION: return Bool.tf(arg.first().isVector());
		case MAKE_VECTOR: return new Vec(arg.first().toNumber().toInteger(), arg);
		case VECTOR: return (arg.isPair() ? ((Pair)arg).convertToVector() : (new Vec()));
		case VECTOR_LENGTH: return new Int(arg.first().toVector().length());
		case VECTOR_REF: return vector_Ref_(arg.first(), arg.second());
		case VECTOR_SET_EXCLAMATION: return vector_Set_(arg.first(), arg.second(), arg.third());
		case VECTOR_TO_LIST: return arg.first().toVector().convertToList();
		case LIST_TO_VECTOR: return arg.first().toList().convertToVector();
		case VECTOR_FILL_EXCLAMATION: return arg.first().toVector().fill(arg.second());
			// control
		case PROCEDURE_QUESTION: return Bool.tf(arg.first().isProcedure());
		case APPLY: return(apply_(ip, arg, env));
		case MAP: return(map_(ip, arg.first(), arg.rest(), env));
		case FOR_EACH: return(for_each_(ip, arg.first(), arg.rest(), env));
		case FORCE: return(arg.first().toPromise().force(ip, env));
		case CALL_WITH_CURRENT_CONTINUATION: return Bool.FALSE;
		case VALUES: return Bool.FALSE;
		case CALL_WITH_VALUES: return Bool.FALSE;
		case DYNAMIC_WIND: return Bool.FALSE;
		case EVAL: return(ip.eval(ip.eval(arg.first(), env), env));
		case SCHEME_REPORT_ENVIRONMENT: return Bool.FALSE;
		case NULL_ENVIRONMENT: return Bool.FALSE;
		case INTERACTION_ENVIRONMENT: return Bool.FALSE;
			// io
		case CALL_WITH_INPUT_FILE: return Bool.FALSE;
		case CALL_WITH_OUTPUT_FILE: return Bool.FALSE;
		case INPUT_PORT_QUESTION: return Bool.tf(arg.first().toPort().isInputPort());
		case OUTPUT_PORT_QUESTION: return Bool.tf(arg.first().toPort().isOutputPort());
		case CURRENT_INPUT_PORT: return InputPort.CURRENT;
		case CURRENT_OUTPUT_PORT: return OutputPort.CURRENT;
		case WITH_INPUT_FROM_FILE: return Bool.FALSE;
		case WITH_OUTPUT_TO_FILE: return Bool.FALSE;
		case OPEN_INPUT_FILE: return new InputPort(arg.first().toString_());
		case OPEN_OUTPUT_FILE: return new OutputPort(arg.first().toString_());
		case CLOSE_INPUT_PORT: return arg.first().toPort().toInputPort().close();
		case CLOSE_OUTPUT_PORT: return arg.first().toPort().toOutputPort().close();
		case READ: return Bool.FALSE;
		case READ_CHAR: return Port.inputPort(arg, 1).readChar();
		case PEEK_CHAR: return Port.inputPort(arg, 1).peekChar();
		case EOF_OBJECT_QUESTION: return Bool.tf(arg.first().isEOF());
		case CHAR_READY_QUESTION: return Bool.FALSE;
		case WRITE: return Port.outputPort(arg, 2).write(arg.first());
		case DISPLAY: return Port.outputPort(arg, 2).display(arg.first());
		case NEWLINE: return Port.outputPort(arg, 1).newline();
		case WRITE_CHAR: return Port.outputPort(arg, 2).writeChar(arg.first().toChar());
		case LOAD: return Bool.FALSE;
		case TRANSCRIPT_ON: return Bool.FALSE;
		case TRANSCRIPT_OFF: return Bool.FALSE;
		
		}
		throw new FnException(Error.UNIMPLEMENTED_FEATURE, toString() + "#" + index);
	}

	private Expression append_(Expression arg)
	throws FnException {
		Expression retVal = Empty.EMPTY;
		for(Expression cur = arg; cur.isPair(); cur = cur.rest()) retVal = retVal.append(cur.first());
		return retVal;
	}

	private Expression reverse_(Expression arg)
	throws FnException {
		Expression arg1 = arg.first();
		if(!arg1.isList()) throw new FnException(Error.LIST_EXPECTED, arg1);
		Expression retVal = Empty.EMPTY;
		while(arg1.isPair()) {
			retVal = arg1.first().cons(retVal);
			arg1 = arg1.rest();
		}
		return retVal;
	}

	private Expression cxr_(Expression arg, int length, int code)
	throws FnException {
		Expression retVal = arg.first();
		int cp = 1;
		for(int i = 0; i < length; i++) {
			retVal = ((code & cp) != 0) ? retVal.first() : retVal.rest();
			cp <<= 1;
		}
		return retVal;
	}

	private Expression list_tailRef_(Expression list, Expression k, boolean tail)
	throws FnException {
		long n = k.toNumber().toInteger().toNatural();
		if(n < 0 || n >= list.toList().length()) throw new FnException(Error.OUT_OF_RANGE, k);
		Expression cur = list;
		for(long l = n; l > 0; l--) cur = cur.rest();
		return (tail) ? cur : cur.first();
	}

	private Expression vector_Ref_(Expression v, Expression k)
	throws FnException {
		long n = k.toNumber().toInteger().toNatural();
		if(n < 0 || n >= v.toVector().length()) throw new FnException(Error.OUT_OF_RANGE, k);
		return v.toVector().elementAt((int)n);
	}

	private Expression vector_Set_(Expression v, Expression k, Expression z)
	throws FnException {
		long n = k.toNumber().toInteger().toNatural();
		if(n < 0 || n >= v.toVector().length()) throw new FnException(Error.OUT_OF_RANGE, k);
		v.toVector().setElementAt(z, (int)n);
		return Void.VOID;
	}

	private Expression memberAssoc_(Expression obj, Expression list, boolean member, char comp)
	throws FnException {
		for(Expression cur = list; cur.isPair(); cur = cur.rest()) {
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
	throws FnException {
		if(!arg.first().isPair()) throw new FnException(Error.PAIR_EXPECTED, arg);
		if(f) ((Pair)arg.first()).rplca(arg.second());
		else ((Pair)arg.first()).rplcd(arg.second());
		return Void.VOID;
	}

	private Expression compareNumbers_(Expression arg, char comp)
	throws FnException {
		for(Expression cur = arg; cur.rest().isPair(); cur = cur.rest()) {
			Nb x = cur.first().toNumber();
			Nb y = cur.second().toNumber();
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

	private Expression computeNumbers_(Expression arg, char op, Nb seed)
	throws FnException {
		if(arg.isPair()) {
			if(!arg.rest().isPair()) {
				Nb x = arg.first().toNumber();
				switch(op) {
					case '-': return Nb.ZERO.binOp(op, seed);
					case '/': return Nb.ONE.binOp(op, seed);
				}
				return x;
			} else {
				for(Expression cur = arg.rest(); cur.isPair(); cur = cur.rest()) {
					Nb x = cur.first().toNumber();
					seed = seed.binOp(op, x);
				}
			}
		}
		return seed;
	}
	
	private Expression compareChars_(Expression arg, char comp, boolean f)
	throws FnException {
		for(Expression cur = arg; cur.rest().isPair(); cur = cur.rest()) {
			Char x = cur.first().toChar();
			Char y = cur.second().toChar();
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

	private Expression compareStrings_(Expression arg, char comp, boolean f)
	throws FnException {
		for(Expression cur = arg; cur.rest().isPair(); cur = cur.rest()) {
			Str x = cur.first().toString_();
			Str y = cur.second().toString_();
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
	
	private Expression apply_(Interpreter ip, Expression arg, Environment env) throws FnException {
		Expression retVal = new Pair(arg.first());
		for (Expression cur = arg.rest(); cur.isPair(); cur = cur.rest()) {
			Expression z = cur.first();
			if (cur.rest().isPair()) z = new Pair(z);
			retVal = retVal.append(z);
		}
		return(ip.eval(retVal, env));
	}
	
	private Expression map_(Interpreter ip, Expression op, Expression arg, Environment env) throws FnException {
		if (!arg.first().isPair()) return(Empty.EMPTY);
		return(ip.eval(op.cons(map_1(arg)), env).cons(map_(ip, op, map_2(arg), env)));
	}
	
	private Expression map_1(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(Symbol.QUOTE.cons(arg.first().first().cons(Empty.EMPTY)).cons(map_1(arg.rest())));
	}
	
	private Expression map_2(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(arg.first().rest().cons(map_2(arg.rest())));
	}

	private Expression for_each_(Interpreter ip, Expression op, Expression arg, Environment env) throws FnException {
		if (!arg.first().isPair()) return(Void.VOID);
		ip.eval(op.cons(for_each_1(arg)), env);
		return(for_each_(ip, op, for_each_2(arg), env));
	}
	
	private Expression for_each_1(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(Symbol.QUOTE.cons(arg.first().first().cons(Empty.EMPTY)).cons(for_each_1(arg.rest())));
	}
	
	private Expression for_each_2(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(arg.first().rest().cons(for_each_2(arg.rest())));
	}
	
}